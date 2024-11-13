import { HttpService } from '@nestjs/axios';
import { HttpException, HttpStatus, Injectable, Logger } from '@nestjs/common';
import { ethers } from 'ethers';
import { ConfigService } from 'src/config/config.service';
import { MintDto } from 'src/dto/pt-dto';

@Injectable()
export class PlatformTokenService {
  private readonly tokenContractAddress;
  private readonly tokenContract;
  private readonly adminPrivatekey;
  private readonly provider;
  private readonly logger = new Logger(PlatformTokenService.name);
  private readonly adminWallet;

  constructor(
    private configService: ConfigService,
    private httpService: HttpService,
  ) {
    this.provider = this.configService.getProvider();
    this.tokenContractAddress = this.configService.getTokenContractAddress();
    this.tokenContract = new ethers.Contract(
      this.tokenContractAddress,
      this.configService.getTokenContractABI(),
      this.provider,
    );
    this.adminPrivatekey = this.configService.getAdminPK();
    this.adminWallet = new ethers.Wallet(this.adminPrivatekey, this.provider);
  }

  async totalSupply(): Promise<string> {
    try {
      const totalSupply = await this.tokenContract.totalSupply();
      return totalSupply;
    } catch (error) {
      throw new HttpException(
        `Failed to get totalSupply of ${this.tokenContractAddress} : ${error.message}`,
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  async mint(mintDto: MintDto): Promise<string> {
    try {
      await this.tokenContract
        .connect(this.adminWallet)
        .mint(mintDto.addressTo, mintDto.amount);
      return `${mintDto.amount} SOGO token minted`;
    } catch (error) {
      throw new HttpException(
        `Failed to get mint in ${this.tokenContractAddress} with amount ${mintDto.amount}: ${error.message}`,
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
