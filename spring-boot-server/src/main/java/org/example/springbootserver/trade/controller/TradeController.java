package org.example.springbootserver.trade.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.trade.dto.BidDTO;
import org.example.springbootserver.trade.dto.BidRequestDTO;
import org.example.springbootserver.trade.dto.BidResponseDTO;
import org.example.springbootserver.trade.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trade")
@RequiredArgsConstructor
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    private final TradeService tradeService;

    @GetMapping("/contract-name")
    public ResponseEntity<HttpResponseDTOv2> getMarketPlaceName() {
        return tradeService.getMarketPlaceName();
    }

    @GetMapping("/nft-active/{collection}/{nftId}")
    public  ResponseEntity<HttpResponseDTOv2>  getNFTActive(
            @PathVariable String collection,
            @PathVariable int nftId) {
        return tradeService.getNFTActive(collection, nftId);
    }

    @GetMapping("/nft-bids/{collection}/{nftId}")
    public ResponseEntity<HttpResponseDTOv2<Map<String, List<BidDTO>>>> getNFTBids(
            @PathVariable String collection,
            @PathVariable int nftId) {
        long startTime = System.currentTimeMillis();
        logger.info("Starting getNFTBids request for collection: {}, nftId: {}", collection, nftId);

        ResponseEntity<HttpResponseDTOv2<Map<String, List<BidDTO>>>> response = tradeService.getNFTBids(collection, nftId);

        logger.info("Received response from tradeService after {} ms", System.currentTimeMillis() - startTime);
        logger.debug("Response body: {}", response.getBody());

        long endTime = System.currentTimeMillis();
        logger.info("Completed getNFTBids request in {} ms", endTime - startTime);

        return response;
    }

//    @GetMapping("/nft-bids/{collection}/{nftId}")
//    public ResponseEntity<HttpResponseDTO<Map<String, List<BidDTO>>>> getNFTBids(
//            @PathVariable String collection,
//            @PathVariable int nftId) {
//        HttpResponseDTO<Map<String, List<BidDTO>>> bids = tradeService.getNFTBids(collection, nftId);
//        return new ResponseEntity<>(bids, HttpStatus.OK);
//    }

    @GetMapping("/nft-bid-info/{collection}/{nftId}/{bidId}")
    public ResponseEntity<HttpResponseDTOv2> getNFTBidInfo(
            @PathVariable String collection,
            @PathVariable int nftId,
            @PathVariable int bidId) {
        return tradeService.getNFTBidInfo(collection, nftId, bidId);
    }

    @PostMapping("/activateBidding/{nftId}")
    public ResponseEntity<HttpResponseDTOv2> activateBidding(@PathVariable int nftId) {
        return tradeService.activateBidding(nftId);
    }

    @PostMapping("/deactivateBidding/{nftId}")
    public ResponseEntity<HttpResponseDTOv2> deactivateBidding(@PathVariable int nftId) {
        return tradeService.deactivateBidding(nftId);
    }

    @PostMapping("/bid")
    public ResponseEntity<HttpResponseDTOv2> createBid(@RequestBody BidRequestDTO bidRequest) {
        return tradeService.createBid(bidRequest);
    }

    @PostMapping("/acceptBid/{nftId}/{bidId}")
    public ResponseEntity<HttpResponseDTOv2> acceptBid(
            @PathVariable int nftId,
            @PathVariable int bidId) {
        return tradeService.acceptBid(nftId, bidId);
    }

    // Get all bids for an NFT owned by Current User
    @GetMapping("/nft-bids/received")
    public ResponseEntity<HttpResponseDTOv2<List<BidResponseDTO>>> getAllBidsReceived() {
        List<BidResponseDTO> bids = tradeService.getAllBidsReceived();
        HttpResponseDTOv2<List<BidResponseDTO>> response = new HttpResponseDTOv2<>(
                "NFT bids Received retrieved successfully",
                bids
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/nft-bids/proposed")
    public ResponseEntity<HttpResponseDTOv2<List<BidResponseDTO>>> getAllBidsProposedByUser() {
        List<BidResponseDTO> bids = tradeService.getAllBidsProposedByUser();
        HttpResponseDTOv2<List<BidResponseDTO>> response = new HttpResponseDTOv2<>(
                "NFT bids Proposed retrieved successfully",
                bids
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/nft-bids/received_deprecated")
    public ResponseEntity<HttpResponseDTO<Map<String, List<BidResponseDTO>>>> getAllBidsReceivedDeprecated() {
        HttpResponseDTO<Map<String, List<BidResponseDTO>>> bids = tradeService.getAllBidsReceivedDeprecated();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @GetMapping("/nft-bids/proposed_deprecated")
    public ResponseEntity<HttpResponseDTO<Map<String, List<BidResponseDTO>>>> getAllBidsProposedByUserDeprecated() {
        HttpResponseDTO<Map<String, List<BidResponseDTO>>> bids = tradeService.getAllBidsProposedByUserDeprecated();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }
}