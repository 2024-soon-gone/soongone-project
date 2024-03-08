// src/nft/nft.service.ts

import { Injectable, HttpException, HttpStatus, Logger } from '@nestjs/common';
import { AddressLike, BigNumberish, ethers } from 'ethers';
import { ConfigService } from 'src/config/config.service';
import { MintDto } from '../dto/nft-dto';
import { HttpService } from '@nestjs/axios';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { AxiosError } from 'axios';

@Injectable()
export class NftService {
  private readonly contractAddress;
  private readonly adminPrivatekey;
  private readonly ipfsApiJWT;
  private readonly provider;
  private readonly logger = new Logger(NftService.name);

  // 사용자의 privateKey가 넘어오면 그 때 PK와 Provider로 wallet 생성 후 상호작용
  // NFT Mint의 경우에는 서비스 PK로 민팅 시행
  //   private readonly wallet = new ethers.Wallet(this.privateKey, this.provider);
  //   private readonly contract = new ethers.Contract(this.contractAddress, ['function mint(string name)'], this.wallet);

  constructor(
    private configService: ConfigService,
    private httpService: HttpService,
  ) {
    this.provider = this.configService.getProvider();
    this.adminPrivatekey = this.configService.getAdminPK();
    this.ipfsApiJWT = this.configService.getIpfsJWT();
    // console.log('Admin PK form dotenv : ', this.adminPrivatekey);
    // console.log('ipfsApiJWT form dotenv : ', this.ipfsApiJWT);
  }

  //   async mintNft(mintNftDto: MintDto): Promise<string> {
  //     try {
  //       // Call the mint function on your smart contract
  //       const transaction = await this.contract.mint(mintNftDto.name);

  //       // Wait for the transaction to be mined
  //       await transaction.wait();

  //       return `NFT Minted: ${mintNftDto.name}`;
  //     } catch (error) {
  //       throw new HttpException(`Failed to mint NFT: ${error.message}`, HttpStatus.INTERNAL_SERVER_ERROR);
  //     }
  //   }

  async ipfsUpload(
    from: AddressLike,
    to: AddressLike,
    amountIn: BigNumberish,
  ): Promise<string> {
    const uploadApiUrl = 'https://api.pinata.cloud/pinning/pinJSONToIPFS';
    const headers = {
      Authorization: `Bearer ${this.ipfsApiJWT}`,
      // 'Content-Type': 'application/json',
    };

    const body = {
      pinataMetadata: {},
      pinataOptions: {},
    };

    try {
      const pinataResponse = await firstValueFrom(
        this.httpService
          .post(uploadApiUrl, body, {
            headers,
          })
          .pipe(
            catchError((error: AxiosError) => {
              this.logger.error(error.response.data);
              throw new Error('Error uploading to IPFS');
            }),
          ),
      );

      const ipfsHash = pinataResponse.data.IpfsHash;
      console.log('IPFS Hash: ' + ipfsHash);

      return ipfsHash;
    } catch (error) {
      throw error;
    }
  }
}
