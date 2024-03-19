import { Module } from '@nestjs/common';
import { OnchainService } from './onchain.service';
import { OnchainController } from './onchain.controller';

@Module({
  providers: [OnchainService],
  controllers: [OnchainController]
})
export class OnchainModule {}
