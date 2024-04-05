// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721Burnable.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract SGNFT is ERC721, ERC721URIStorage, ERC721Burnable, Ownable {
    uint256 private _tokenIdCounter; // Simple counter to track token IDs

    constructor(address deployer) ERC721("MyToken", "MTK") Ownable(deployer) {
        _tokenIdCounter = 0; // Initialize the counter
    }
    

    function safeMint(address to, string memory uri) public onlyOwner {
        _tokenIdCounter++; // Increment the counter before minting to start from 1
        uint256 newItemId = _tokenIdCounter; // Use the counter as the new token ID
        _safeMint(to, newItemId); // Mint the new item
        _setTokenURI(newItemId, uri); // Set the token URI
    }

    // The following functions are overrides required by Solidity.
    function tokenURI(uint256 tokenId)
        public
        view
        override(ERC721, ERC721URIStorage)
        returns (string memory)
    {
        return super.tokenURI(tokenId);
    }

    function supportsInterface(bytes4 interfaceId)
        public
        view
        override(ERC721, ERC721URIStorage)
        returns (bool)
    {
        return super.supportsInterface(interfaceId);
    }

}
