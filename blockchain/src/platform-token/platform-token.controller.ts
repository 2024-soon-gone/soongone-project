import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Post,
} from '@nestjs/common';
import { PlatformTokenService } from './platform-token.service';
import { MintDto } from 'src/dto/pt-dto';
import { ResponseDTO } from 'src/dto/ResponseDTO';

@Controller('platform-token')
export class PlatformTokenController {
  constructor(private tokenService: PlatformTokenService) {}

  @Get('totalSupply')
  async getTotalSupply(): Promise<ResponseDTO<{ totalSupply: string }>> {
    try {
      const totalSupply = await this.tokenService.totalSupply();
      return new ResponseDTO('Total supply retrieved successfully', {
        totalSupply,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Failed to get total supply for SoGo Token', {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Post('mint')
  async mintToken(
    @Body() mintDto: MintDto,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.tokenService.mint(mintDto);
      return new ResponseDTO('Token minted successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Error: token/mint', { details: error.message }),
        HttpStatus.BAD_REQUEST,
      );
    }
  }
}
