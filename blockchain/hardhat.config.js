require('@nomicfoundation/hardhat-toolbox');
require('@nomicfoundation/hardhat-ethers');
require('@nomicfoundation/hardhat-ignition-ethers'); // Ignition Module for Deploying Smart Contract
require('dotenv').config();

const INFURA_PROJECT_ID = process.env.INFURA_PROJECT_ID;
const DEPLOYER_PK = process.env.ADMIN_PRIVATEKEY;

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  defaultNetwork: 'hardhat',
  networks: {
    hardhat: {
      // loggingEnabled: true,
      allowUnlimitedContractSize: true,
    },
    sepolia: {
      loggingEnabled: true,
      url: `https://sepolia.infura.io/v3/${INFURA_PROJECT_ID}`,
      accounts: [DEPLOYER_PK],
    },
    polygon: {
      loggingEnabled: true,
      url: `https://polygon-mumbai.infura.io/v3/${INFURA_PROJECT_ID}`,
      accounts: [DEPLOYER_PK],
    },
    tb: {
      loggingEnabled: true,
      url: `http://106.240.238.226:10188`,
      accounts: [DEPLOYER_PK],
      gasPrice: 80000000000,
    },
    metadium: {
      loggingEnabled: true,
      url: `https://api.metadium.com/dev`,
      accounts: [DEPLOYER_PK],
      gasPrice: 80000000000,
    },
  },
  solidity: {
    compilers: [
      {
        version: '0.8.20',
        settings: {
          optimizer: {
            enabled: true,
            runs: 200,
          },
          viaIR: true,
        },
      },
      {
        version: '0.8.6',
        settings: {
          optimizer: {
            enabled: true,
            details: {
              yulDetails: {
                optimizerSteps: 'u',
              },
            },
            runs: 200,
          },
          viaIR: true,
        },
      },
    ],
  },
};
