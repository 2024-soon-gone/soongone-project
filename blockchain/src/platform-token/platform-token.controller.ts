import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Param,
  Post,
} from '@nestjs/common';
import { PlatformTokenService } from './platform-token.service';
import { MintDto } from 'src/dto/pt-dto';

@Controller('platform-token')
export class PlatformTokenController {
  constructor(private tokenService: PlatformTokenService) {}

  @Get('totalSupply')
  async getTotalSupply(): Promise<string> {
    try {
      const tokenURI = await this.tokenService.totalSupply();
      return tokenURI;
    } catch (error) {
      throw new HttpException(
        {
          status: HttpStatus.INTERNAL_SERVER_ERROR,
          error: `Failed to get totalSupply for SoGo Token`,
          details: error.message,
        },
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('mint')
  async mintToken(@Body() mintDto: MintDto) {
    try {
      return await this.tokenService.mint(mintDto);
    } catch (error) {
      throw new HttpException(
        {
          status: HttpStatus.BAD_REQUEST,
          error: 'Error : nft/mint',
          details: error.message,
        },
        HttpStatus.BAD_REQUEST,
      );
    }
  }
}
