// SPDX-License-Identifier: MIT

pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC721/IERC721Receiver.sol";
import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC20/ERC20.sol"; // For name, symbol, decimals fo ERC20 additional importation of ERC20Detailed Required
import "./interfaces/IMarketPlace.sol";

contract MarketPlace is IERC721Receiver,  IMarketPlace{
    uint256 private constant PRECISION = 1e3;
    uint private constant FEE = 100; // FEE set to 100/1e3 = 10%, for Test env
    // Events
    // Events allow 3 indexed attr
    // Should choose wisely for which attr to set 'indexed' for
    // WIP Additional research about 'indexed' usage and 'event'
    event CreateBid(
        address indexed nftCollectionAddress,
        uint256 indexed nftId,
        address addressPaymentToken,
        uint256 amountPaymentToken,
        uint256 endTime,
        address bidder,
        uint256 indexed bidId 
    );

    event CancelBid(
        address indexed nftCollectionAddress,
        uint256 indexed nftId,
        uint256 bidId,
        address indexed bidder
    );

    event AcceptBid(
        address indexed nftCollectionAddress,
        uint256 indexed nftId,
        address addressPaymentToken,
        uint256 amountPaymentToken,
        uint256 endTime,
        address bidder,
        uint256 indexed bidId 
    );

    event ActivateBidding(
        address indexed nftCollectionAddress,
        uint256 indexed nftId
    );

    event DeactivateBidding(
        address indexed nftCollectionAddress,
        uint256 indexed nftId
    );

    // NFT Collection => (NFT ID => Bid[])
    mapping(address => mapping(uint256 => Bid[])) private bids; // Manage with Setter
    // NFT Collection => (NFT ID => Bid Active)
    mapping(address => mapping(uint256 => bool)) private bidState;

    function isContract(address _addr) private view returns (bool) {
        uint256 size;
        assembly {
            size := extcodesize(_addr)
        }
        return size > 0;
    }

    function createBid(
        Bid calldata _bid
    ) external override {

        //Check is addresses are valid
        require(
            isContract(_bid.addressNFTCollection),
            "Invalid NFT Collection contract address"
        );
        require(
            isContract(_bid.addressPaymentToken),
            "Invalid Payment Token contract address"
        );

        ERC721 nftContract = ERC721(_bid.addressNFTCollection);
        ERC20 paymentToken= ERC20(_bid.addressPaymentToken);

        // Check if the NFT ID of certain NFT Collection is valid
        require(
            nftContract.ownerOf(_bid.nftId) != address(0),
            "NFT ID doesn't exist"
        );

        // Check if the Bidding on certain NFT ID is activated
        require(
            bidState[_bid.addressNFTCollection][_bid.nftId] == true,
            "Bidding is deactivated on desired NFT"
        );

        // Check if the msg.sender currently has enough Balance of certain token
        require(
            paymentToken.balanceOf(msg.sender) > _bid.amountPaymentToken,
            "Msg sender's payment balance is not enough"
        );

        // Check if the validTime is a valid time ( Not a wrong value or time already passed)
        require(
            _bid.endTime > block.timestamp,
            "Invalid endTime for bid"
        );

        // Check if the bidder == msg.sender
        require(
            _bid.bidder == msg.sender,
            "Msg sender is not set to bidder"
        );

        // After all necessary validation, push the _bid
        bids[_bid.addressNFTCollection][_bid.nftId].push(_bid);

        uint256 bidId = bids[_bid.addressNFTCollection][_bid.nftId].length - 1;

        emit CreateBid(
            _bid.addressNFTCollection,
            _bid.nftId,
            _bid.addressPaymentToken,
            _bid.amountPaymentToken,
            _bid.endTime,
            _bid.bidder,
            bidId
        );

    }

    function activateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) external override{
        //Check is addresses are valid
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );

        // - check the owner of NFT.
        ERC721 nftContract = ERC721(_addressNFTCollection);
        // Make sure the sender that wants to create a new auction
        // for a specific NFT is the owner of this NFT
        require(
            nftContract.ownerOf(_nftId) == msg.sender,
            "Caller is not the owner of the NFT"
        );
        // - check if bid is already opened
        require(
            bidState[_addressNFTCollection][_nftId] == false,
            "Bidding is already Activated"
        );
        // 
        // *. 심화) 현재 bid가 새로 생기는지 여부 확인 필요

    }

    function deactivateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) external override{
        //Check is addresses are valid
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );

        // - check the owner of NFT.
        ERC721 nftContract = ERC721(_addressNFTCollection);
        // Make sure the sender that wants to create a new auction
        // for a specific NFT is the owner of this NFT
        require(
            nftContract.ownerOf(_nftId) == msg.sender,
            "Caller is not the owner of the NFT"
        );
        // - check if bid is already opened
        require(
            bidState[_addressNFTCollection][_nftId] == true,
            "Bidding is already DeActivated"
        );
        // 
        // *. 심화) 현재 bid가 새로 생기는지 여부 확인 필요

    }


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