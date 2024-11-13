import {
  Controller,
  Get,
  Post,
  HttpException,
  HttpStatus,
  Param,
  Body,
} from '@nestjs/common';
import { TradeService } from './trade.service';
import { ResponseDTO } from '../dto/ResponseDTO';
import { BidDto } from '../dto/trade-bid-dto';

@Controller('trade')
export class TradeController {
  constructor(private tradeService: TradeService) {}

  @Get('contract-name')
  async getMarketPlaceName(): Promise<
    ResponseDTO<{ marketplaceName: string }>
  > {
    try {
      const marketplaceName = await this.tradeService.getMarketPlaceName();
      return new ResponseDTO('MarketPlace Name retrieved successfully', {
        marketplaceName,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve MarketPlace Name', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-active/:addressNFTCollection/:nftId')
  async getNFTActive(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
  ): Promise<ResponseDTO<{ nftAllowedBid: boolean }>> {
    try {
      const nftAllowedBid = await this.tradeService.getNFTActive(
        addressNFTCollection,
        nftId,
      );
      return new ResponseDTO('NFT active status retrieved successfully', {
        nftAllowedBid,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT active status', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-bids/:addressNFTCollection/:nftId')
  async getNFTBids(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
  ): Promise<ResponseDTO<{ bidsOnNFT: BidDto[] }>> {
    try {
      const bidsOnNFT = await this.tradeService.getNFTBids(
        addressNFTCollection,
        nftId,
      );
      const mappedBids: BidDto[] = bidsOnNFT.map((bid) => ({
        addressNFTCollection: bid[0],
        nftId: bid[1],
        addressPaymentToken: bid[2],
        amountPaymentToken: bid[3],
        endTime: bid[4],
        bidder: bid[5],
      }));
      return new ResponseDTO('NFT bids retrieved successfully', {
        bidsOnNFT: mappedBids,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT bids', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('nft-bid-info/:addressNFTCollection/:nftId/:bidId')
  async getNFTBidInfo(
    @Param('addressNFTCollection') addressNFTCollection: string,
    @Param('nftId') nftId: number,
    @Param('bidId') bidId: number,
  ): Promise<ResponseDTO<{ bidInfo: BidDto }>> {
    try {
      const bidInfo = await this.tradeService.getNFTBidInfo(
        addressNFTCollection,
        nftId,
        bidId,
      );
      const mappedBidInfo: BidDto = {
        addressNFTCollection: bidInfo[0],
        nftId: bidInfo[1],
        addressPaymentToken: bidInfo[2],
        amountPaymentToken: bidInfo[3],
        endTime: bidInfo[4],
        bidder: bidInfo[5],
      };
      return new ResponseDTO('NFT bid info retrieved successfully', {
        bidInfo: mappedBidInfo,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to retrieve NFT bid info', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('activate-bidding/:collection/:nftId')
  async activateBidding(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Body('privateKey') privateKey: string,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.tradeService.activateBidding(
        collection,
        nftId,
        privateKey,
      );
      return new ResponseDTO('Bidding activated successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to activate bidding', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('deactivate-bidding/:collection/:nftId')
  async deactivateBidding(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Body('privateKey') privateKey: string,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.tradeService.deactivateBidding(
        collection,
        nftId,
        privateKey,
      );
      return new ResponseDTO('Bidding de-activated successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to deactivate bidding', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('bid')
  async createBid(
    @Body('bidRequest') bidRequest: BidDto,
    @Body('privateKey') privateKey: string,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.tradeService.createBid(
        bidRequest,
        privateKey,
      );
      return new ResponseDTO('Bid created successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to create bidding', { details: error.message }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('acceptBid/:collection/:nftId/:bidId')
  async acceptBid(
    @Param('collection') collection: string,
    @Param('nftId') nftId: number,
    @Param('bidId') bidId: number,
    @Body('privateKey') privateKey: string,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.tradeService.acceptBid(
        collection,
        nftId,
        bidId,
        privateKey,
      );
      return new ResponseDTO('Bid accepted successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to accept bidding', { details: error.message }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
