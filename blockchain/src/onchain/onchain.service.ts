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
export class OnchainService {
  private readonly nftContractAddress;
  private readonly nftContract;
  private readonly tokenContractAddress;
  private readonly tokenContract;

  private readonly adminPrivatekey;
  private readonly provider;
  private readonly logger = new Logger(OnchainService.name);
  private readonly adminWallet;

  // 사용자의 privateKey가 넘어오면 그 때 PK와 Provider로 wallet 생성 후 상호작용
  // NFT Mint의 경우에는 서비스 PK로 민팅 시행
  // private readonly wallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  //   private readonly contract = new ethers.Contract(this.nftContractAddress, ['function mint(string name)'], this.wallet);

  constructor(private configService: ConfigService) {
    this.provider = this.configService.getProvider();

    this.nftContractAddress = this.configService.getNFTContractAddress();
    this.nftContract = new ethers.Contract(
      this.nftContractAddress,
      this.configService.getNFTContractABI(),
      this.provider,
    );

    this.tokenContractAddress = this.configService.getTokenContractAddress();
    this.tokenContract = new ethers.Contract(
      this.tokenContractAddress,
      this.configService.getTokenContractABI(),
      this.provider,
    );

    this.adminPrivatekey = this.configService.getAdminPK();
    this.adminWallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  }
  // SoGo.sol
  async getAddressBalance(address: string): Promise<string> {
    const balance = await this.tokenContract.balanceOf(address);
    return balance;
  }

  async transfer(
    addressTo: string,
    amount: number,
    userPrivateKey: string,
  ): Promise<any> {
    const reqUserWallet = new ethers.Wallet(userPrivateKey, this.provider);
    const tx = await this.tokenContract
      .connect(reqUserWallet)
      .transfer(addressTo, amount);
    await tx.wait();
    return tx;
  }

  // SGNFT.sol
  async getAddressNFTBalance(address: string): Promise<string> {
    const balance = await this.nftContract.balanceOf(address);
    return balance;
  }

  async getNftCollectionName(): Promise<string> {
    const nftCollectionName = await this.nftContract.name();
    return nftCollectionName;
  }

  async getNftCollectionOwner(): Promise<string> {
    const nftCollectionOwner = await this.nftContract.owner();
    return nftCollectionOwner;
  }

  async getNftOwner(nftId: number): Promise<string> {
    const nftCollectionOwner = await this.nftContract.ownerOf(nftId);
    return nftCollectionOwner;
  }

  async getTokenURI(nftId: number): Promise<string> {
    const nftCollectionOwner = await this.nftContract.tokenURI(nftId);
    return nftCollectionOwner;
  }
}
