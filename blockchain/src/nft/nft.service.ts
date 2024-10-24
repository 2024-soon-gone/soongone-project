// src/nft/nft.service.ts

import { Injectable, HttpException, HttpStatus, Logger } from '@nestjs/common';
import { AddressLike, BigNumberish, ethers } from 'ethers';
import { ConfigService } from 'src/config/config.service';
import { MintDto } from '../dto/nft-dto';
import { HttpService } from '@nestjs/axios';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { AxiosError } from 'axios';
import { Readable } from 'stream';
import * as dotenv from 'dotenv';

@Injectable()
export class NftService {
  private readonly nftContractAddress;
  private readonly nftContract;
  private readonly marketPlaceAddress;
  private readonly marketPlaceContract;
  private readonly adminPrivatekey;
  private readonly ipfsApiJWT;
  private readonly provider;
  private readonly logger = new Logger(NftService.name);
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
    this.marketPlaceAddress = this.configService.getMarketPlaceAddress();
    this.marketPlaceContract = new ethers.Contract(
      this.marketPlaceAddress,
      this.configService.getMarketPlaceABI(),
      this.provider,
    );
    this.adminPrivatekey = this.configService.getAdminPK();
    this.ipfsApiJWT = this.configService.getIpfsJWT();
    this.adminWallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  }

  async getTokenURI(tokenId): Promise<string> {
    try {
      const tokenURI = await this.nftContract
        .connect(this.adminWallet)
        .tokenURI(tokenId);
      // Wait for the transaction to be mined
      // await tokenURI.wait();
      return tokenURI;
    } catch (error) {
      throw new HttpException(
        `Failed to get tokenURI of ${tokenId} : ${error.message}`,
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  async mintNft(mintDto: MintDto, file: Express.Multer.File): Promise<any> {
    let imgIpfsHash: string;
    let jsonIpfsHash: string;

    // Upload image to IPFS
    try {
      imgIpfsHash = await this.ipfsFileUpload(mintDto, file);
      console.log(`Image IPFS Hash: ${imgIpfsHash}`);
    } catch (error) {
      throw new HttpException(
        `Failed to upload image to IPFS: ${error.message}`,
        HttpStatus.BAD_REQUEST, // Use BAD_REQUEST for client-side issues like file upload failure
      );
    }

    // Upload metadata to IPFS
    try {
      jsonIpfsHash = await this.ipfsJsonUpload(mintDto, imgIpfsHash);
      console.log(`JSON IPFS Hash: ${jsonIpfsHash}`);
    } catch (error) {
      throw new HttpException(
        `Failed to upload JSON metadata to IPFS: ${error.message}`,
        HttpStatus.INTERNAL_SERVER_ERROR, // Server-side error for metadata upload failure
      );
    }

    // Mint the NFT after successful IPFS uploads
    try {
      const tokenURI = 'ipfs://' + jsonIpfsHash;

      const nftId = await this.nftContract._tokenIdCounter();
      console.log('NFT ID : ', nftId);

      const transaction = await this.nftContract
        .connect(this.adminWallet)
        .safeMint(mintDto.accountAddress, tokenURI);

      // Wait for the transaction to be mined
      await transaction.wait();

      // this.adminPrivatekey = this.configService.getAdminPK();
      // this.ipfsApiJWT = this.configService.getIpfsJWT();
      // this.adminWallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
      const userWallet = new ethers.Wallet(
        mintDto.accountPrivateKey,
        this.provider,
      );
      const approveTx = await this.nftContract
        .connect(userWallet)
        .setApprovalForAll(this.marketPlaceAddress, true);

      console.log('NFT approved on MarketPlace Contract');

      const activateTx = await this.marketPlaceContract
        .connect(userWallet)
        .activateBidding(this.nftContract, nftId);

      console.log('NFT Bid Activated on MarketPlace Contract');

      console.log(`NFT Minted with metadata: ${jsonIpfsHash}`);

      const imgIpfsUri = process.env.IPFS_FETCH_SUFFIX + '/' + imgIpfsHash;
      console.log(`NFT IMG uploaded on : ${imgIpfsUri} `);

      // Return a success response
      return {
        statusCode: HttpStatus.CREATED,
        message: 'NFT successfully minted',
        nftIpfsHash: jsonIpfsHash,
        transactionHash: transaction.hash, // Include transaction details
        nftImgIpfsUri: imgIpfsUri,
        nftId: nftId,
      };
    } catch (error) {
      throw new HttpException(
        `Failed to mint NFT: ${error.message}`,
        HttpStatus.INTERNAL_SERVER_ERROR, // Server-side error for minting failure
      );
    }
  }

  async ipfsFileUpload(
    mintDto: MintDto,
    file: Express.Multer.File,
  ): Promise<string> {
    // const uploadApiUrl = 'https://api.pinata.cloud/pinning/pinFileToIPFS';
    const uploadApiUrl = process.env.IPFS_API_URL + '/pinning/pinFileToIPFS';

    const formData = new FormData();
    const fileBlob = new Blob([file.buffer]);
    formData.append('file', fileBlob, file.originalname);

    // Append metadata and options as needed
    formData.append('pinataMetadata', JSON.stringify({ name: mintDto.name }));
    formData.append('pinataOptions', JSON.stringify({ cidVersion: 1 }));

    const headers = {
      Authorization: `Bearer ${this.ipfsApiJWT}`,
    };

    try {
      const pinataResponse = await firstValueFrom(
        this.httpService.post(uploadApiUrl, formData, { headers }).pipe(
          catchError((error: AxiosError) => {
            this.logger.error(error.response.data);
            throw new Error('Error uploading file to IPFS');
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

  async ipfsJsonUpload(mintDto: MintDto, imgIpfsHash: string): Promise<string> {
    // const uploadApiUrl = 'https://api.pinata.cloud/pinning/pinJSONToIPFS';
    const uploadApiUrl = process.env.IPFS_API_URL + '/pinning/pinJSONToIPFS';

    const headers = {
      Authorization: `Bearer ${this.ipfsApiJWT}`,
      'Content-Type': 'application/json', // Explicitly set for JSON data
    };

    const body = {
      pinataContent: {
        description: mintDto.description,
        external_link: 'https://openseacreatures.io/3',
        image: `ipfs://${imgIpfsHash}`,
        name: mintDto.name,
        attributes: [
          {
            trait_type: 'Base',
            value: 'Starfish',
          },
        ],
      },
      pinataMetadata: {
        name: mintDto.name, // Use the name from mintDto or any other relevant metadata
      },
      pinataOptions: {
        cidVersion: 1,
      },
    };

    try {
      const pinataResponse = await firstValueFrom(
        this.httpService.post(uploadApiUrl, body, { headers }).pipe(
          catchError((error: AxiosError) => {
            this.logger.error(error.response.data);
            throw new Error('Error uploading JSON to IPFS');
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
