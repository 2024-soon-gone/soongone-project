import { Module } from '@nestjs/common';
import { ExtendedEthersService } from './extended-ethers.service';

@Module({
  controllers: [],
  providers: [ExtendedEthersService],
  exports: [ExtendedEthersService], // Export ExtendedEthersService in order to allow other modules to use
})
export class ExtendedEthersModule {}
