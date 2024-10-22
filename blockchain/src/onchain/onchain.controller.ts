import {
  Controller,
  Get,
  Param,
  Post,
  Body,
  Res,
  HttpStatus,
  Response,
} from '@nestjs/common';
import { OnchainService } from './onchain.service';
import { Response as ExpressResponse } from 'express';

@Controller('onchain')
export class OnchainController {
  constructor(private readonly onchainService: OnchainService) {}

  @Get('nft-count')
  async getNFTCount(@Response() res: ExpressResponse): Promise<any> {
    try {
      const nftCount = await this.onchainService.getNFTCount();
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT Count retrieved successfully',
        data: {
          nftCount,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT Count',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('token-balance/:address')
  async getAddressBalance(
    @Param('address') address: string,
    @Res() res: ExpressResponse,
  ): Promise<any> {
    try {
      const balance = await this.onchainService.getAddressBalance(address);
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Address Token balance retrieved successfully',
        data: {
          balance,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve address token balance',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Post('transfer')
  async transfer(
    @Body('addressTo') addressTo: string,
    @Body('amount') amount: number,
    @Body('privateKey') privateKey: string,
    @Res() res: ExpressResponse,
  ): Promise<any> {
    try {
      const result = await this.onchainService.transfer(
        addressTo,
        amount,
        privateKey,
      );
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Transfer completed successfully',
        data: result,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to complete transfer',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-balance/:address')
  async getAddressNFTBalance(
    @Param('address') address: string,
    @Res() res: ExpressResponse,
  ): Promise<any> {
    try {
      const balance = await this.onchainService.getAddressNFTBalance(address);
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT balance retrieved successfully',
        data: {
          balance,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT balance',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-collection-name')
  async getNftCollectionName(@Res() res: ExpressResponse): Promise<any> {
    try {
      const collectionName = await this.onchainService.getNftCollectionName();
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT collection name retrieved successfully',
        data: {
          collectionName,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT collection name',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-collection-owner')
  async getNftCollectionOwner(@Res() res: ExpressResponse): Promise<any> {
    try {
      const owner = await this.onchainService.getNftCollectionOwner();
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT collection owner retrieved successfully',
        data: {
          owner,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT collection owner',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-owner/:nftId')
  async getNftOwner(
    @Param('nftId') nftId: number,
    @Res() res: ExpressResponse,
  ): Promise<any> {
    try {
      const owner = await this.onchainService.getNftOwner(nftId);
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT owner retrieved successfully',
        data: {
          owner,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT owner',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('token-uri/:nftId')
  async getTokenURI(
    @Param('nftId') nftId: number,
    @Res() res: ExpressResponse,
  ): Promise<any> {
    try {
      const tokenURI = await this.onchainService.getTokenURI(nftId);
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Token URI retrieved successfully',
        data: {
          tokenURI,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve token URI',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }
}
