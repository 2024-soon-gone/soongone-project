import {
  Controller,
  Get,
  Param,
  Post,
  Body,
  HttpStatus,
  HttpException,
} from '@nestjs/common';
import { OnchainService } from './onchain.service';
import { ResponseDTO } from '../dto/ResponseDTO';

@Controller('onchain')
export class OnchainController {
  constructor(private readonly onchainService: OnchainService) {}

  @Get('nft-count')
  async getNFTCount(): Promise<ResponseDTO<{ nftCount: string }>> {
    try {
      const nftCount = await this.onchainService.getNFTCount();
      return new ResponseDTO('NFT Count retrieved successfully', { nftCount });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT Count', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('token-balance/:address')
  async getAddressBalance(
    @Param('address') address: string,
  ): Promise<ResponseDTO<{ balance: string }>> {
    try {
      const balance = await this.onchainService.getAddressBalance(address);
      return new ResponseDTO('Address Token balance retrieved successfully', {
        balance,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve address token balance', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('transfer')
  async transfer(
    @Body('addressTo') addressTo: string,
    @Body('amount') amount: number,
    @Body('privateKey') privateKey: string,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.onchainService.transfer(
        addressTo,
        amount,
        privateKey,
      );
      return new ResponseDTO('Transfer completed successfully', txResult);
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to complete transfer', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-balance/:address')
  async getAddressNFTBalance(
    @Param('address') address: string,
  ): Promise<ResponseDTO<{ balance: string }>> {
    try {
      const balance = await this.onchainService.getAddressNFTBalance(address);
      return new ResponseDTO('NFT balance retrieved successfully', { balance });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT balance', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-collection-name')
  async getNftCollectionName(): Promise<
    ResponseDTO<{ collectionName: string }>
  > {
    try {
      const collectionName = await this.onchainService.getNftCollectionName();
      return new ResponseDTO('NFT collection name retrieved successfully', {
        collectionName,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT collection name', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-collection-owner')
  async getNftCollectionOwner(): Promise<ResponseDTO<{ owner: string }>> {
    try {
      const owner = await this.onchainService.getNftCollectionOwner();
      return new ResponseDTO('NFT collection owner retrieved successfully', {
        owner,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT collection owner', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-owner/:nftId')
  async getNftOwner(
    @Param('nftId') nftId: number,
  ): Promise<ResponseDTO<{ owner: string }>> {
    try {
      const owner = await this.onchainService.getNftOwner(nftId);
      return new ResponseDTO('NFT owner retrieved successfully', { owner });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT owner', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('token-uri/:nftId')
  async getTokenURI(
    @Param('nftId') nftId: number,
  ): Promise<ResponseDTO<{ tokenURI: string }>> {
    try {
      const tokenURI = await this.onchainService.getTokenURI(nftId);
      return new ResponseDTO('Token URI retrieved successfully', { tokenURI });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve token URI', {
          error: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
