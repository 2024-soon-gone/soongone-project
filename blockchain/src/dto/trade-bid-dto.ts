export class BidDto {
  addressNFTCollection: string; // Address of the NFT collection
  nftId: number; // NFT ID
  addressPaymentToken: string; // Address of the ERC20 payment token
  amountPaymentToken: number; // Amount of payment token (in string to handle BigNumberish values)
  endTime: number; // End time for the bid (Unix timestamp)
  bidder: string; // Address of the bidder
}
