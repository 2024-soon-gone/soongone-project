// SPDX-License-Identifier: MIT

pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC721/IERC721Receiver.sol";

contract MarketPlace is IERC721Receiver {
    uint256 private constant PRECISION = 1e3;
    uint private constant FEE = 100; // FEE set to 100/1e3 = 10%, for Test env

    struct Bid {
        address addressNFTCollection;
        uint256 nftId;
        address addressPaymentToken;
        uint256 amountPaymentToken;
        uint256 validTime;
    }
    // NFT Collection => (NFT ID => Bid[])
    mapping(address => mapping(uint256 => Bid[])) private bids; // Manage with Setter

    function onERC721Received(
        address,
        address,
        uint256,
        bytes calldata
    ) external pure override returns (bytes4) {
        // Can add extra logic on ERC721 Received.
        return this.onERC721Received.selector;
    }
}