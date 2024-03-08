import { IsString, IsNumber, Min } from 'class-validator';

export class MintDto {
  @IsString({ message: 'Account address must be a string.' })
  accountAddress: string;

  @IsString()
  name: string;

  @IsString()
  description: string;
}
