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

import { ethers } from 'ethers';
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
}

// @Controller('lend-and-borrow')
// export class LendAndBorrowController {
// constructor(private lendAndBorrowService: LendAndBorrowService) {}

// @Get('snapshotWemix')
// async getAccountSnapshot(@Query() params: AccountSnapshotDto): Promise<any> {
//     try {
//     return await this.lendAndBorrowService.getAccountSnapshot(
//         params.accountAddress,
//     );
//     } catch (error) {
//     throw new HttpException(
//         {
//         status: HttpStatus.INTERNAL_SERVER_ERROR,
//         error: 'There was a problem getting the account snapshot',
//         details: error.message,
//         },
//         HttpStatus.INTERNAL_SERVER_ERROR,
//     );
//     }
// }

// @Get('liquidationInfo')
// async getLiquidationInfo(@Query() params: AccountSnapshotDto): Promise<any> {
//     try {
//     return await this.lendAndBorrowService.getLiquidationInfo(
//         params.accountAddress,
//     );
//     } catch (error) {
//     throw new HttpException(
//         {
//         status: HttpStatus.INTERNAL_SERVER_ERROR,
//         error: 'There was a problem getting the liquidation info',
//         details: error.message,
//         },
//         HttpStatus.INTERNAL_SERVER_ERROR,
//     );
//     }
// }

// @Post('depositAsset')
// async depositAsset(
//     @Body() dto: DepositAssetDto,
// ): Promise<any> {
//     try {
//     return await this.lendAndBorrowService.depositAsset(
//         dto.senderAddress,
//         dto.amount,
//         dto.assetAddress,
//     );
//     } catch (error) {
//     throw new HttpException(
//         {
//         status: HttpStatus.BAD_REQUEST,
//         error: 'There was a problem with the deposit',
//         details: error.message,
//         },
//         HttpStatus.BAD_REQUEST,
//     );
//     }
// }

// @Post('borrowAsset')
// async borrowAsset(
//     @Body() dto: BorrowAssetDto,
// ): Promise<any> {
//     try {
//     return await this.lendAndBorrowService.borrowAsset(
//         dto.borrowerAddress,
//         dto.amount,
//         dto.assetAddress,
//     );
//     } catch (error) {
//     throw new HttpException(
//         {
//         status: HttpStatus.BAD_REQUEST,
//         error: 'There was a problem with the Borrowing',
//         details: error.message,
//         },
//         HttpStatus.BAD_REQUEST,
//     );
//     }
// }

// @Post('liquidateAsset')
// async liquidateAsset(
//     @Body() dto: LiquidateAssetDto,
// ): Promise<any> {
//     try {
//     return await this.lendAndBorrowService.liquidateAsset(
//         dto.liquidatorAddress,
//         dto.borrowerAddress,
//         dto.repayAmount,
//         dto.liquidateAssetAddress,
//         dto.collateralAddress,
//     );
//     } catch (error) {
//     throw new HttpException(
//         {
//         status: HttpStatus.BAD_REQUEST,
//         error: 'There was a problem with the Liquidating',
//         details: error.message,
//         },
//         HttpStatus.BAD_REQUEST,
//     );
//     }
// }
// }
