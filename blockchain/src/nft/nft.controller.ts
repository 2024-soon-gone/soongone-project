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
} from '@nestjs/common';
import { NftService } from './nft.service';
import { FileInterceptor } from '@nestjs/platform-express/multer';
import { MintDto } from 'src/dto/nft-dto';
import { ResponseDTO } from 'src/dto/ResponseDTO';

@Controller('nft')
export class NftController {
  constructor(private nftService: NftService) {}

  @Post('mint')
  @UseInterceptors(FileInterceptor('file'))
  async uploadFile(
    @Body() mintDto: MintDto,
    @UploadedFile() file: Express.Multer.File,
  ): Promise<ResponseDTO<{ txResult: any }>> {
    try {
      const txResult = await this.nftService.mintNft(mintDto, file);
      return new ResponseDTO('NFT minted successfully', { txResult });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO('Error: nft/mint', { details: error.message }),
        HttpStatus.BAD_REQUEST,
      );
    }
  }

  @Get('tokenURI')
  async getTokenURI(
    @Query('tokenId') tokenId: number,
  ): Promise<ResponseDTO<{ tokenURI: string }>> {
    try {
      const tokenURI = await this.nftService.getTokenURI(tokenId);
      return new ResponseDTO(`Token URI retrieved for token ID ${tokenId}`, {
        tokenURI,
      });
    } catch (error) {
      throw new HttpException(
        new ResponseDTO(`Failed to get tokenURI for token ${tokenId}`, {
          details: error.message,
        }),
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Get('ping')
  async responsePing(
    @Query('sender') sender: string,
  ): Promise<ResponseDTO<{ message: string }>> {
    console.log('Block Chain Server Ping Request');
    return new ResponseDTO('Ping Returned', {
      message: `Sent Msg: ${sender}`,
    });
  }
}
