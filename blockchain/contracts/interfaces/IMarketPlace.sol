//SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

interface IMarketPlace {
    // Getter returns (contents~), Setter doesnt return

    // Events allow 3 indexed attr
    // Should choose wisely for which attr to set 'indexed' for
    // WIP Additional research about 'indexed' usage and 'event'
    event CreateBid(
        address indexed nftCollectionAddress,
        uint256 indexed nftId,
        address addressPaymentToken,
        uint256 amountPaymentToken,
        uint256 validTime,
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
        uint256 validTime,
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

    struct Bid {
        address addressNFTCollection;
        uint256 nftId;
        address addressPaymentToken;
        uint256 amountPaymentToken;
        uint256 validTime;
        address bidder;
    }

    // Should return BidId with Event
    function createBid(
        Bid calldata _bid
    ) external;

    // Bid.bidder == msg.sender의 확인 작업 필요
    function cancelBid(
        address _addressNFTCollection,
        uint256 _nftId,
        uint256 _bidId
    ) external;

    function acceptBid(
        Bid calldata _bid
    ) external;


    function activateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) external;

    function deactivateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) external;

    // 현재 특정 NFT에 Bidding이 가능한지 반환
    function getNFTActive(
        address _addressNFTCollection,
        uint256 _nftId
    ) external view returns(bool);

    // 특정 NFT Collection의 특정 NFT에 제안된 Bid를 Get
    function getNFTBids(
        address  _addressNFTCollection,
        uint256  _nftId
    ) external view returns(address); // Returns Bid structure Array

    // 계정이(if _addressAccount == 0x0 All Bids) 제안한 Bids를 모두(address == 0x0) 혹은 NFT Collection Address를 매개로
    function getAccountBids(
        address _addressAccount,
        address _addressNFTCollection
    ) external view returns(address); // Returns Bid structure Array

}