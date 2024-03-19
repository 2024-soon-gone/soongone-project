async function main() {
  const [deployer] = await ethers.getSigners();
  console.log('Deploying contracts with the account:', deployer.address);

  // Get the ContractFactory for your contract
  const SampleNFT = await ethers.getContractFactory('sampleNFT');

  // Deploy the contract
  const token = await SampleNFT.deploy(
    '0xF9a2E8a6021b60Ea81a9CFA727cc22c8Baf07e54',
  );

  // Wait for the contract to be deployed
  //   await token.deployed();

  console.log('NFT Contract Address :', token.target); // token.target == Contract deployed address
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
