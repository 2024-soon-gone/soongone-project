import { BytesLike } from 'ethers';
import {
  Table,
  Column,
  Model,
  DataType,
  CreatedAt,
  UpdatedAt,
} from 'sequelize-typescript';

@Table
export class Account extends Model<Account> {
  @Column({
    allowNull: false,
    primaryKey: true,
    type: DataType.STRING,
  })
  accountAddress: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  privateKey: string;

  @Column(DataType.STRING)
  description: string;
}

@Table
export class TransferTx extends Model<TransferTx> {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  senderAddress: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  receiverAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(10, 2), // or another appropriate type based on your requirements
  })
  amount: number;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  contractAddress: string;

  @Column(DataType.TEXT) // 'TEXT' for potentially large JSON data
  data: string;

  // @CreatedAt
  // createdAt: Date;

  // @UpdatedAt
  // updatedAt: Date;
}

@Table
export class TxInfo extends Model<TxInfo> {
  @Column({
    allowNull: false,
    type: DataType.INTEGER,
  })
  block_number: number;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  block_timestamp: string;

  @Column({
    allowNull: false,
    primaryKey: true,
    type: DataType.STRING,
  })
  tx_hash: string;

  @Column(DataType.STRING)
  contract_name: string;

  @Column(DataType.STRING)
  func_name: string;

  @Column(DataType.STRING)
  func_sig: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  from: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  to: string;

  @Column(DataType.TEXT) // 'TEXT' type for potentially large JSON data
  input: string;

  @Column({
    allowNull: true,
    type: DataType.DECIMAL(65), // if Wemix sent with Tx
  })
  value: bigint;

  // @CreatedAt
  // createdAt: Date;

  // @UpdatedAt
  // updatedAt: Date;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class LendAndBorrowTx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  assetAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  assetAmount: bigint;

  // received~ columns are for LiquidateBorrow cases
  @Column({
    allowNull: true,
    type: DataType.STRING,
  })
  receivedAssetAddress: string;

  @Column({
    allowNull: true,
    type: DataType.DECIMAL(65),
  })
  receivedAssetAmount: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class PoolV2Tx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  assetAAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  assetAAmount: bigint;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  assetBAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  assetBAmount: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  liquidityAdded: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  liquidityRemoved: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class SwapV2Tx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  swapInAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  swapInAmount: bigint;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  swapOutAddress: string;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  swapOutAmount: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class PoolV3Tx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  token0: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  token1: string;

  @Column({
    allowNull: false,
    type: DataType.INTEGER,
  })
  tokenId: number;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  liquidity: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  amount0: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  amount1: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class SwapV3Tx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  tokenIn: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  tokenOut: string;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  path: BytesLike;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  amountIn: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  amountOut: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class WonderStakingTx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.INTEGER,
  })
  pid: number;

  @Column({
    allowNull: false,
    type: DataType.INTEGER,
  })
  toPid: number;

  @Column({
    allowNull: false,
    type: DataType.STRING,
  })
  receiverAddress: string; // receiverAddress : input param 'payable 'to''

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  amount: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  rewardAmount: bigint;
}

@Table({
  defaultScope: {
    order: [['block_timestamp', 'ASC']], // or 'DESC' for descending
  },
})
export class LiquidStakingTx extends TxInfo {
  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  wemixAmount: bigint;

  @Column({
    allowNull: false,
    type: DataType.DECIMAL(65),
  })
  stWemixAmount: bigint;
}
