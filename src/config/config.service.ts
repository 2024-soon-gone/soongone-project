import { Injectable, Logger } from '@nestjs/common';
import { ethers } from 'ethers';
import * as dotenv from 'dotenv';

dotenv.config(); // dotenv.config({ path: '.env.local' }); In case of .env file locations in not in this current directory

@Injectable()
export class ConfigService {
  public getProvider(): ethers.JsonRpcProvider {
    const ethTestnetProvider = new ethers.JsonRpcProvider(
      'https://api.test.wemix.com/',
    );
    return ethTestnetProvider;
  }

  public getAdminPK() {
    const adminPk = process.env.ADMIN_PRIVATEKEY;
    return adminPk;
  }

  public getIpfsJWT() {
    const adminPk = process.env.PINATA_API_JWT;
    return adminPk;
  }

  private readonly logger = new Logger(ConfigService.name);
}
