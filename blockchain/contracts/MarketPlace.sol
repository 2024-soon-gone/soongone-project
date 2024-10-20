// SPDX-License-Identifier: MIT

pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC721/IERC721Receiver.sol";
import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC20/ERC20.sol"; // For name, symbol, decimals fo ERC20 additional importation of ERC20Detailed Required
import "./interfaces/IMarketPlace.sol";

contract MarketPlace is IERC721Receiver,  IMarketPlace{

    string public name = "SoonGone Market Place";
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

    function isContract(address _addr) public view returns (bool) {
        uint256 size;
        assembly {
            size := extcodesize(_addr)
        }
        return size > 0;
    }

    function createBid(
        Bid calldata _bid
    ) external override {

        //Check if addresses are valid
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

        // bidder should approve the MarketPlace as 'spender' for a bidded amount
        paymentToken.approve(address(this), _bid.amountPaymentToken);

        // If trying to approve the amount exceeding msg.sender's balance will be reverted
        // Thus below require() is not mandated
        // Check if the msg.sender currently has enough Balance of certain token
        // require(
        //     paymentToken.balanceOf(msg.sender) > _bid.amountPaymentToken,
        //     "Msg sender's payment balance is not enough"
        // );

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

    function cancelBid(
        address _addressNFTCollection,
        uint256 _nftId,
        uint256 _bidId
    ) external override {
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );
        ERC721 nftContract = ERC721(_addressNFTCollection);
        require(
            nftContract.ownerOf(_nftId) != address(0),
            "NFT ID doesn't exist"
        );
        // Check if _bidId is present
        require(
            _bidId < bids[_addressNFTCollection][_nftId].length,
            "bid ID doesn' exist"
        );
        // Check if msg.sender == bidder
        // Later this should be modified if a service can cancel the bid on behalf of original bidder
        require(
            msg.sender == bids[_addressNFTCollection][_nftId][_bidId].bidder,
            "msg.sender is not a bidder of this bid"
        );
         
        // delete bids[_addressNFTCollection][_nftId][_bidId];
        Bid storage bidToCancel = bids[_addressNFTCollection][_nftId][_bidId];
        bidToCancel.endTime = 0; // if endTime == 0 => NFT SOLD
        
    }


    function acceptBid(
        address _addressNFTCollection,
        uint256 _nftId,
        uint256 _bidId
    ) external override {
        //Check if addresses are valid
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );

        Bid storage bidToAccept = bids[_addressNFTCollection][_nftId][_bidId];
        ERC721 nftContract = ERC721(_addressNFTCollection);
        ERC20 paymentToken= ERC20(bidToAccept.addressPaymentToken);

        // Check if the NFT ID of certain NFT Collection is valid
        require(
            nftContract.ownerOf(_nftId) != address(0),
            "NFT ID doesn't exist"
        );
        // Check if _bidId is present
        require(
            _bidId < bids[_addressNFTCollection][_nftId].length,
            "bid ID doesn' exist"
        );
        // Check if endTime is not passed
        require(
             block.timestamp < bidToAccept.endTime,
            "Bid's endTime is already timeout"
        );
        // Check if the original bidder has enough Payment Token Amount in current time period
        require(
            paymentToken.balanceOf(bidToAccept.bidder) > bidToAccept.amountPaymentToken,
            "Bidder doesn't have enough Payment Token"
        );


        // If a approval of MarketPlace.sol on ERC20, ERC721 are not met => below process will fail
        // Transfer of Payment Token
        paymentToken.transferFrom(bidToAccept.bidder, msg.sender, bidToAccept.amountPaymentToken);

        // Transfer of owner ship of NFT
        nftContract.safeTransferFrom(msg.sender,bidToAccept.bidder,_nftId);

        emit AcceptBid(
            bidToAccept.addressNFTCollection,
            bidToAccept.nftId,
            bidToAccept.addressPaymentToken,
            bidToAccept.amountPaymentToken,
            bidToAccept.endTime,
            bidToAccept.bidder,
            _bidId
        );

        // Delete Accepted Bid Logic
        // delete bids[_addressNFTCollection][_nftId]; => missing trie node occurs
        bidToAccept.endTime = 0; // if endTime == 0 => NFT SOLD
        // Deactivate bidding on sold NFT
    }

    function activateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) external override{
        // Check is addresses are valid
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );

        // Check the owner of NFT.
        ERC721 nftContract = ERC721(_addressNFTCollection);

        // Check if the msg.sender is the owner of certain NFT
        // If a certain NFTID doesn't exist the owner will have a address of 0x0. This case also handled by require() 
        require(
            nftContract.ownerOf(_nftId) == msg.sender,
            "Caller is not the owner of the NFT"
        );
        // Check if bid is already opened -> Is this require() necessary?
        require(
            bidState[_addressNFTCollection][_nftId] == false,
            "Bidding is already Activated"
        );

        // Approval for MarketPlace.sol
        nftContract.approve(address(this),_nftId);

        // Activate Bidding
        bidState[_addressNFTCollection][_nftId] = true;

        // *. 심화) 현재 bid가 새로 생기는지 여부 확인 필요
        emit ActivateBidding(
            _addressNFTCollection,
            _nftId
        );
    }

    function deactivateBidding(
        address _addressNFTCollection,
        uint256 _nftId
    ) public override{
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

        // Deactivate Bidding
        bidState[_addressNFTCollection][_nftId] = false;

        // 
        // *. 심화) 현재 bid가 새로 생기는지 여부 확인 필요
        emit DeactivateBidding(
            _addressNFTCollection,
            _nftId
        );
    }

    // 현재 특정 NFT에 Bidding이 가능한지 반환
    function getNFTActive(
        address _addressNFTCollection,
        uint256 _nftId
    ) external view returns(bool) {
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );
        ERC721 nftContract = ERC721(_addressNFTCollection);
        require(
            nftContract.ownerOf(_nftId) != address(0),
            "NFT ID doesn't exist"
        );
        return bidState[_addressNFTCollection][_nftId];
    }

    // 특정 NFT Collection의 특정 NFT에 제안된 Bid를 Get
    function getNFTBids(
        address  _addressNFTCollection,
        uint256  _nftId
    ) external view returns(Bid[] memory){ // Returns Bid structure Array
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );
        ERC721 nftContract = ERC721(_addressNFTCollection);
        require(
            nftContract.ownerOf(_nftId) != address(0),
            "NFT ID doesn't exist"
        );
        return bids[_addressNFTCollection][_nftId];
    }
    
    function getNFTBidInfo(
        address _addressNFTCollection,
        uint256 _nftId,
        uint256 _bidId
    ) external view returns(Bid memory){
        require(
            isContract(_addressNFTCollection),
            "Invalid NFT Collection contract address"
        );
        ERC721 nftContract = ERC721(_addressNFTCollection);
        require(
            nftContract.ownerOf(_nftId) != address(0),
            "NFT ID doesn't exist"
        );
        // Check if _bidId is present
        require(
            _bidId < bids[_addressNFTCollection][_nftId].length,
            "bid ID doesn' exist"
        );

        return bids[_addressNFTCollection][_nftId][_bidId];
    }

    //계정이(if _addressAccount == 0x0 All Bids) 제안한 Bids를 모두(address == 0x0) 혹은 NFT Collection Address를 매개로
    // WIP
    function getAccountBids(
        address _addressAccount,
        address _addressNFTCollection
    ) external view returns(Bid[] memory){
        Bid[] memory dummyBids = new Bid[](1); // 더미 배열의 크기를 1로 설정
        // 더미 Bid 구조체 생성
        Bid memory dummyBid = Bid(address(0), 0, address(0), 0, 0, address(0));
        dummyBids[0] = dummyBid; // 더미 Bid를 배열에 추가

        return dummyBids;
    } // Returns Bid structure Array

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