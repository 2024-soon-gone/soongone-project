package org.example.springbootserver.trade.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.onchain.dto.TransactionResponseDTO;
import org.example.springbootserver.trade.dto.BidDTO;
import org.example.springbootserver.trade.dto.BidRequestDTO;
import org.example.springbootserver.trade.dto.BidResponseDTO;
import org.example.springbootserver.trade.service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trade")
@RequiredArgsConstructor
public class TradeController {

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
        ResponseEntity<HttpResponseDTOv2<Map<String, List<BidDTO>>>> response = tradeService.getNFTBids(collection, nftId);
        System.out.println(response.getBody());
        return response;
    }

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
    public ResponseEntity<HttpResponseDTO<Map<String, List<BidResponseDTO>>>> getAllBidsReceived() {
        HttpResponseDTO<Map<String, List<BidResponseDTO>>> bids = tradeService.getAllBidsReceived();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @GetMapping("/nft-bids/proposed")
    public ResponseEntity<HttpResponseDTO<Map<String, List<BidResponseDTO>>>> getAllBidsProposedByUser() {
        HttpResponseDTO<Map<String, List<BidResponseDTO>>> bids = tradeService.getAllBidsProposedByUser();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }
}