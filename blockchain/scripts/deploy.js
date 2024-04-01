async function main() {
  const [deployer] = await ethers.getSigners();
  console.log('Deploying contracts with the account:', deployer.address);

  // SoGo(ERC20) for SoonGone Deploy
  const SoGoContract = await ethers.getContractFactory('SoGo');
  const SoGo = await SoGoContract.deploy(
    '0xF9a2E8a6021b60Ea81a9CFA727cc22c8Baf07e54',
  );
  // await SoGo.initialize('0xF9a2E8a6021b60Ea81a9CFA727cc22c8Baf07e54', {
  //   gasLimit: 3000000,
  // }); // Init with TestAccount 1 to SGNFT Deployer
  console.log('SoGo Contract Address : ', SoGo.target);
  // const SoGoSupply = await SoGo.balanceOf( //
  //   '0xF9a2E8a6021b60Ea81a9CFA727cc22c8Baf07e54',
  // );
  // console.log('Initial SoGo mint amount : ', SoGoSupply);

  // SGNFT Deploy
  const SGNFTContract = await ethers.getContractFactory('SGNFT');
  const SGNFT = await SGNFTContract.deploy(
    '0xF9a2E8a6021b60Ea81a9CFA727cc22c8Baf07e54', // Set TestAccount 1 to SGNFT Deployer
  );

  console.log('SGNFT Contract Address : ', SGNFT.target); // SGNFT.target == Contract deployed address
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
