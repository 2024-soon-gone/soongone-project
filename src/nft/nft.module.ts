import { Module } from '@nestjs/common';
import { NftService } from './nft.service';
import { NftController } from './nft.controller';
import { ConfigModule } from 'src/config/config.module';
import { HttpModule } from '@nestjs/axios';

@Module({
  providers: [NftService],
  controllers: [NftController],
  imports: [ConfigModule, HttpModule],
})
export class NftModule {}
