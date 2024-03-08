import { Injectable, Logger } from '@nestjs/common';
import { ethers } from 'ethers';

@Injectable()
export class ExtendedEthersService {
  // Provider to Config Module
  //   public getProvider(): ethers.JsonRpcProvider {
  //     const ethTestnetProvider = new ethers.JsonRpcProvider(
  //       'https://api.test.wemix.com/',
  //     );
  //     return ethTestnetProvider;
  //   }

  private readonly logger = new Logger(ExtendedEthersService.name);
}
