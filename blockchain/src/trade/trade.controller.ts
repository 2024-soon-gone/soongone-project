import {
  Controller,
  Get,
  Post,
  HttpException,
  HttpStatus,
  Response,
  Param,
  Body,
} from '@nestjs/common';
import { TradeService } from './trade.service';
import { Response as ExpressResponse } from 'express';
import { BidDto } from '../dto/trade-bid-dto';

@Controller('trade')
export class TradeController {
  constructor(private tradeService: TradeService) {}

  @Get('contract-name')
  async getMarketPlaceName(@Response() res: ExpressResponse): Promise<any> {
    try {
      const marketplaceName = await this.tradeService.getMarketPlaceName();
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'MarketPlace Name retrieved successfully',
        data: {
          marketplaceName,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve MarketPlace Name',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-active/:addressNFTCollection/:nftId')
  async getNFTActive(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
    @Response() res: ExpressResponse,
  ): Promise<any> {
    try {
      const nftAllowedBid = await this.tradeService.getNFTActive(
        addressNFTCollection,
        nftId,
      );
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT active status retrieved successfully',
        data: {
          nftAllowedBid,
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT active status',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-bids/:addressNFTCollection/:nftId')
  async getNFTBids(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
    @Response() res: ExpressResponse,
  ): Promise<any> {
    try {
      const bidsOnNFT = await this.tradeService.getNFTBids(
        addressNFTCollection,
        nftId,
      );

      // bidsOnNFT를 BidDto로 매핑
      const mappedBids: BidDto[] = bidsOnNFT.map((bid) => ({
        addressNFTCollection: bid[0],
        nftId: bid[1],
        addressPaymentToken: bid[2],
        amountPaymentToken: bid[3], // 5번째 요소
        endTime: bid[4],
        bidder: bid[5],
      }));

      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT bids retrieved successfully',
        data: {
          bidsOnNFT: mappedBids, // 매핑된 BidDto 배열 반환
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT bids',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Get('nft-bid-info/:addressNFTCollection/:nftId/:bidId')
  async getNFTBidInfo(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
    @Param('bidId') bidId: number,
    @Response() res: ExpressResponse,
  ): Promise<any> {
    try {
      const bidInfo = await this.tradeService.getNFTBidInfo(
        addressNFTCollection,
        nftId,
        bidId,
      );

      // bidInfo를 BidDto로 매핑
      const mappedBidInfo: BidDto = {
        addressNFTCollection: bidInfo[0],
        nftId: bidInfo[1],
        addressPaymentToken: bidInfo[2],
        amountPaymentToken: bidInfo[3], // 5번째 요소
        endTime: bidInfo[4],
        bidder: bidInfo[5],
      };

      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'NFT bid info retrieved successfully',
        data: {
          bidInfo: mappedBidInfo, // 매핑된 BidDto 객체 반환
        },
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to retrieve NFT bid info',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Post('activate-bidding/:collection/:nftId')
  async activateBidding(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Body('privateKey') privateKey: string,
    @Response() res: ExpressResponse, // Response 객체 추가
  ): Promise<any> {
    try {
      const result = await this.tradeService.activateBidding(
        collection,
        nftId,
        privateKey,
      );

      // 성공적인 결과를 클라이언트에 응답
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Bidding activated successfully',
        data: result,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      // 에러 발생 시 클라이언트에 응답
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to activate bidding',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Post('deactivate-bidding/:collection/:nftId')
  async deactivateBidding(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Body('privateKey') privateKey: string,
    @Response() res: ExpressResponse, // Response 객체 추가
  ): Promise<any> {
    try {
      const result = await this.tradeService.deactivateBidding(
        collection,
        nftId,
        privateKey,
      );

      // 성공적인 결과를 클라이언트에 응답
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Bidding de-activated successfully',
        data: result,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      // 에러 발생 시 클라이언트에 응답
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to de-activate bidding',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Post('bid')
  async createBid(
    @Body('BidRequest') bidRequest: BidDto,
    @Body('privateKey') privateKey: string,
    @Response() res: ExpressResponse, // Response 객체 추가
  ): Promise<any> {
    try {
      const result = await this.tradeService.createBid(bidRequest, privateKey);

      // 성공적인 결과를 클라이언트에 응답
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Bid created successfully',
        data: result,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      // 에러 발생 시 클라이언트에 응답
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to create bidding',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }

  @Post('acceptBid/:collection/:nftId/:bidId')
  async acceptBid(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Param('bidId') bidId: number,
    @Body('privateKey') privateKey: string,
    @Response() res: ExpressResponse, // Response 객체 추가
  ): Promise<any> {
    try {
      const result = await this.tradeService.acceptBid(
        collection,
        nftId,
        bidId,
        privateKey,
      );

      // 성공적인 결과를 클라이언트에 응답
      return res.status(HttpStatus.OK).json({
        status: 'success',
        statusCode: HttpStatus.OK,
        message: 'Bid Accepted successfully',
        data: result,
        timestamp: new Date().toISOString(),
      });
    } catch (error) {
      // 에러 발생 시 클라이언트에 응답
      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
        status: 'error',
        statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Failed to accept bidding',
        error: error.message,
        timestamp: new Date().toISOString(),
      });
    }
  }
}
