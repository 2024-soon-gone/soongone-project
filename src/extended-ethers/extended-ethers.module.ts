import { Module } from '@nestjs/common';
import { ExtendedEthersController } from './extended-ethers.controller';

@Module({
  controllers: [ExtendedEthersController]
})
export class ExtendedEthersModule {}
