import { IsString, IsNumber, Min } from 'class-validator';

export class MintDto {
  @IsString({ message: 'accountAddress must be a string.' })
  accountAddress: string;

  @IsString({ message: 'accountPrivateKey must be a string.' })
  accountPrivateKey: string;

  @IsString({ message: 'name must be a string.' })
  name: string;

  @IsString({ message: 'description must be a string.' })
  description: string;
}
