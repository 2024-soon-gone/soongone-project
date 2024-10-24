import {
  Controller,
  Post,
  Body,
  Get,
  Query,
  HttpException,
  HttpStatus,
  UseInterceptors,
  UploadedFile,
  Param,
} from '@nestjs/common';

// import { ethers } from 'ethers';
import { NftService } from './nft.service';
import { FileInterceptor } from '@nestjs/platform-express/multer';
import { MintDto } from 'src/dto/nft-dto';

@Controller('nft')
export class NftController {
  constructor(private nftService: NftService) {}

  @Post('mint')
  @UseInterceptors(FileInterceptor('file'))
  async uploadFile(
    @Body() mintDto: MintDto,
    @UploadedFile() file: Express.Multer.File,
  ) {
    try {
      return await this.nftService.mintNft(mintDto, file);
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

  @Get('tokenURI')
  async getTokenURI(@Query('tokenId') tokenId: number): Promise<string> {
    try {
      const tokenURI = await this.nftService.getTokenURI(tokenId);
      return tokenURI;
    } catch (error) {
      throw new HttpException(
        {
          status: HttpStatus.INTERNAL_SERVER_ERROR,
          error: `Failed to get tokenURI for token ${tokenId}`,
          details: error.message,
        },
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
  @Get('ping')
  responsePing(@Query('sender') sender: String): String {
    console.log(' Block Chain Server Ping Request');
    return 'Ping Returned and Sender caught : ' + sender;
  }
}
