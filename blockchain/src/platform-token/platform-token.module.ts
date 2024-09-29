import { Module } from '@nestjs/common';
import { PlatformTokenController } from './platform-token.controller';
import { PlatformTokenService } from './platform-token.service';
import { ConfigModule } from 'src/config/config.module';
import { HttpModule } from '@nestjs/axios';

@Module({
  controllers: [PlatformTokenController],
  providers: [PlatformTokenService],
  imports: [ConfigModule, HttpModule],
})
export class PlatformTokenModule {}
