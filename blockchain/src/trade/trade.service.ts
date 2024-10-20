import { Injectable, HttpException, HttpStatus, Logger } from '@nestjs/common';
import { AddressLike, BigNumberish, ethers } from 'ethers';
import { ConfigService } from 'src/config/config.service';
import { MintDto } from '../dto/nft-dto';
import { HttpService } from '@nestjs/axios';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { AxiosError } from 'axios';
import { Readable } from 'stream';
import * as dotenv from 'dotenv';
import { BidDto } from 'src/dto/trade-bid-dto';

@Injectable()
export class TradeService {
  private readonly nftContractAddress;
  private readonly nftContract;
  private readonly marketplaceAddress;
  private readonly marketplaceContract;
  private readonly adminPrivatekey;
  private readonly ipfsApiJWT;
  private readonly provider;
  private readonly logger = new Logger(TradeService.name);
  private readonly adminWallet;

  // 사용자의 privateKey가 넘어오면 그 때 PK와 Provider로 wallet 생성 후 상호작용
  // NFT Mint의 경우에는 서비스 PK로 민팅 시행
  // private readonly wallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  //   private readonly contract = new ethers.Contract(this.nftContractAddress, ['function mint(string name)'], this.wallet);

  constructor(
    private configService: ConfigService,
    private httpService: HttpService,
  ) {
    this.provider = this.configService.getProvider();

    this.nftContractAddress = this.configService.getNFTContractAddress();
    this.nftContract = new ethers.Contract(
      this.nftContractAddress,
      this.configService.getNFTContractABI(),
      this.provider,
    );

    this.marketplaceAddress = this.configService.getMarketPlaceAddress();
    this.marketplaceContract = new ethers.Contract(
      this.marketplaceAddress,
      this.configService.getMarketPlaceABI(),
      this.provider,
    );

    this.adminPrivatekey = this.configService.getAdminPK();
    this.ipfsApiJWT = this.configService.getIpfsJWT();
    this.adminWallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  }

  async getMarketPlaceName(): Promise<string> {
    const marketplaceName = await this.marketplaceContract
      .connect(this.adminWallet)
      .name();
    return marketplaceName;
  }

  async getNFTActive(
    addressNFTCollection: string,
    nftId: number,
  ): Promise<string> {
    const nftAllowedBid = await this.marketplaceContract.getNFTActive(
      addressNFTCollection,
      nftId,
    );
    return nftAllowedBid;
  }

  async getNFTBids(
    addressNFTCollection: string,
    nftId: number,
  ): Promise<any[]> {
    // Promise<string>에서 Promise<any[]>로 변경
    const bidsOnNFT = await this.marketplaceContract.getNFTBids(
      addressNFTCollection,
      nftId,
    );

    // bidsOnNFT가 문자열인 경우, JSON 파싱하여 배열로 변환
    if (typeof bidsOnNFT === 'string') {
      return JSON.parse(bidsOnNFT);
    }

    return bidsOnNFT; // 이미 배열인 경우 그대로 반환
  }

  async getNFTBidInfo(
    addressNFTCollection: string,
    nftId: number,
    bidId: number,
  ): Promise<any> {
    const bidInfo = await this.marketplaceContract.getNFTBidInfo(
      addressNFTCollection,
      nftId,
      bidId,
    );

    if (typeof bidInfo === 'string') {
      return JSON.parse(bidInfo);
    }

    return bidInfo;
  }

  // Activate Bidding for a given NFT
  async activateBidding(
    addressNFTCollection: string,
    nftId: number,
    userPrivateKey: string,
  ): Promise<string> {
    const reqUserWallet = new ethers.Wallet(userPrivateKey, this.provider);
    const tx = await this.marketplaceContract
      .connect(reqUserWallet)
      .activateBidding(addressNFTCollection, nftId);
    await tx.wait();
    this.logger.log(
      `Bidding activated for NFT ${nftId} in collection ${addressNFTCollection}`,
    );
    return tx;
  }

  // Activate Bidding for a given NFT
  async deactivateBidding(
    addressNFTCollection: string,
    nftId: number,
    userPrivateKey: string,
  ): Promise<string | Error> {
    const reqUserWallet = new ethers.Wallet(userPrivateKey, this.provider);
    const tx = await this.marketplaceContract
      .connect(reqUserWallet)
      .deactivateBidding(addressNFTCollection, nftId);
    await tx.wait();
    this.logger.log(
      `Bidding de-activated for NFT ${nftId} in collection ${addressNFTCollection}`,
    );
    return tx;
  }

  // Create Bid on certain NFT
  async createBid(bidRequest: BidDto, userPrivateKey: string): Promise<string> {
    const reqUserWallet = new ethers.Wallet(userPrivateKey, this.provider);
    const tx = await this.marketplaceContract
      .connect(reqUserWallet)
      .createBid([
        bidRequest.addressNFTCollection,
        bidRequest.nftId,
        bidRequest.addressPaymentToken,
        bidRequest.amountPaymentToken,
        bidRequest.endTime,
        bidRequest.bidder,
      ]);
    await tx.wait();

    const paymentTokenContract: any = new ethers.Contract(
      bidRequest.addressPaymentToken,
      this.configService.getTokenContractABI(),
      this.provider,
    );

    const approveTx = await paymentTokenContract
      .connect(reqUserWallet)
      .approve(this.marketplaceAddress, bidRequest.amountPaymentToken);
    await approveTx.wait();

    this.logger.log(
      `Create Bid for NFT ${bidRequest.nftId} in collection ${bidRequest.addressNFTCollection}`,
    );
    return tx;
  }

  // Create Bid on certain NFT
  async acceptBid(
    addressNFTCollection: string,
    nftId: number,
    bidId: number,
    userPrivateKey: string,
  ): Promise<string> {
    const reqUserWallet = new ethers.Wallet(userPrivateKey, this.provider);
    const tx = await this.marketplaceContract
      .connect(reqUserWallet)
      .acceptBid(addressNFTCollection, nftId, bidId);
    await tx.wait();

    this.logger.log(
      `Accept Bid Id : ${bidId} for NFT ${nftId} in collection ${addressNFTCollection}`,
    );
    return tx;
  }
}
