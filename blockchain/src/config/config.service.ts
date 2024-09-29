import { Injectable, Logger } from '@nestjs/common';
import { ethers } from 'ethers';
import * as dotenv from 'dotenv';
import * as sampleNFTJson from '../../artifacts/contracts/sampleNFTMint.sol/sampleNFT.json';
import * as platFormTokenJSON from '../../artifacts/contracts/SoGo.sol/SoGo.json';
dotenv.config(); // dotenv.config({ path: '.env.local' }); In case of .env file locations in not in this current directory

@Injectable()
export class ConfigService {
  public getProvider(): ethers.JsonRpcProvider {
    const provider = new ethers.JsonRpcProvider(
      // In case of additional API Key required
      process.env.NETWORK_ENDPOINT_URL + `/${process.env.INFURA_PROJECT_ID}`,
      // In case of API Key not required
      // process.env.METADIUM_TESTNET_URL,
    );
    return provider;
  }

  public getAdminPK() {
    const adminPk = process.env.ADMIN_PRIVATEKEY;
    return adminPk;
  }

  public getAdminAddress() {
    const adminPk = process.env.ADMIN_PUBLICADDRESS;
    return adminPk;
  }

  public getNFTContractAddress() {
    const address = process.env.NFT_CONTRACT_ADDRESS;
    return address;
  }

  public getNFTContractABI() {
    const abi = sampleNFTJson.abi;
    return abi;
  }

  public getTokenContractAddress() {
    const address = process.env.TOKEN_CONTRACT_ADDRESS;
    return address;
  }

  public getTokenContractABI() {
    const abi = platFormTokenJSON.abi;
    return abi;
  }

  public getIpfsJWT() {
    const adminPk = process.env.PINATA_API_JWT;
    return adminPk;
  }

  private readonly logger = new Logger(ConfigService.name);
}
