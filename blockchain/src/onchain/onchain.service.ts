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
  private readonly marketplaceAddress;
  private readonly marketplaceContract;
  private readonly adminPrivatekey;
  private readonly ipfsApiJWT;
  private readonly provider;
  private readonly logger = new Logger(OnchainService.name);
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
}
