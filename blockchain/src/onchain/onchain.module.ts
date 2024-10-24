import { Module } from '@nestjs/common';
import { OnchainService } from './onchain.service';
import { OnchainController } from './onchain.controller';
import { ConfigModule } from 'src/config/config.module';
import { HttpModule } from '@nestjs/axios';

@Module({
  providers: [OnchainService],
  controllers: [OnchainController],
  imports: [ConfigModule, HttpModule],
})
export class OnchainModule {}
