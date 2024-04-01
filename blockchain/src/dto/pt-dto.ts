import { IsString, IsNumber, Min, IsInt } from 'class-validator';

export class MintDto {
  @IsString({ message: 'addressTo must be a string.' })
  addressTo: string;

  @IsString({ message: 'amount must be a string.' })
  amount: string;
}
