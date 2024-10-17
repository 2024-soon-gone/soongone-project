const { buildModule } = require('@nomicfoundation/hardhat-ignition/modules');

module.exports = buildModule('SGMarketPlace', (m) => {
  //   const apollo = m.contract("Rocket", ["Saturn V"]);

  //   m.call(apollo, "launch", []);

  const NFTAddress = '0x0EC0e5A0B9D8e005054226E72F5222CE3f9FF155';
  const SGMarketPlace = m.contract('MarketPlace');

  m.call(SGMarketPlace, 'getNFTActive', [NFTAddress, 100]);
  return { SGMarketPlace };
});
