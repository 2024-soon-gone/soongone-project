import { Module } from '@nestjs/common';
// import { MongooseModule } from '@nestjs/mongoose'; // Not using MongooseModule anymore
import { SequelizeModule } from '@nestjs/sequelize';

import { AppController } from './app.controller';
import { AppService } from './app.service';
// import { ExtendedEthersModule } from './extended-ethers/extended-ethers.module';
import { DatabaseModule } from './database/database.module';
import { NftModule } from './nft/nft.module';
import { TradeModule } from './trade/trade.module';
import { OnchainModule } from './onchain/onchain.module';
import { ConfigModule } from './config/config.module';

@Module({
  imports: [
    // SequelizeModule.forRoot({
    //   dialect: 'mysql',
    //   host: 'localhost',
    //   port: 3308,
    //   username: 'root',
    //   password: '1234',
    //   database: 'soongone-bc-db',
    //   autoLoadModels: true,
    //   synchronize: true,
    // }),
    DatabaseModule,
    NftModule,
    TradeModule,
    OnchainModule,
    ConfigModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
