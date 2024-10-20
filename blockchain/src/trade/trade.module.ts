import { Module } from '@nestjs/common';
import { TradeService } from './trade.service';
import { TradeController } from './trade.controller';
import { ConfigModule } from 'src/config/config.module';
import { HttpModule } from '@nestjs/axios';

@Module({
  providers: [TradeService],
  controllers: [TradeController],
  imports: [ConfigModule, HttpModule],
})
export class TradeModule {}
