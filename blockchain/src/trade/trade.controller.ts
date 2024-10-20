import {
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Response,
} from '@nestjs/common';
import { TradeService } from './trade.service';
import { Response as ExpressResponse } from 'express';

@Controller('trade')
export class TradeController {
  constructor(private tradeService: TradeService) {}

  @Get('contract_name')
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
}
