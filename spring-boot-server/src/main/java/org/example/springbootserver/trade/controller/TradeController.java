package org.example.springbootserver.trade.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.HttpResponseDTO;
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

    public static class CreateBidRequest {
        private BidDTO bidRequest;
        private String privateKey;

        // Getters and Setters
        public BidDTO getBidRequest() {
            return bidRequest;
        }

        public void setBidRequest(BidDTO bidRequest) {
            this.bidRequest = bidRequest;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

    // Get the name of the marketplace
    @GetMapping("/contract-name")
    public ResponseEntity<HttpResponseDTO<Map<String, String>>> getMarketPlaceName() {
        HttpResponseDTO<Map<String, String>> response = tradeService.getMarketPlaceName();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    // Get the active status of an NFT
    @GetMapping("/nft-active/{collection}/{nftId}")
    public ResponseEntity<HttpResponseDTO<Map<String, Boolean>>> getNFTActive(
            @PathVariable String collection,
            @PathVariable int nftId) {
        HttpResponseDTO<Map<String, Boolean>> response = tradeService.getNFTActive(collection, nftId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    // Get all bids for an NFT
    @GetMapping("/nft-bids/{collection}/{nftId}")
    public ResponseEntity<HttpResponseDTO<Map<String, List<BidDTO>>>> getNFTBids(
            @PathVariable String collection,
            @PathVariable int nftId) {
        HttpResponseDTO<Map<String, List<BidDTO>>> bids = tradeService.getNFTBids(collection, nftId);
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    // Get specific bid information for an NFT
    @GetMapping("/nft-bid-info/{collection}/{nftId}/{bidId}")
    public ResponseEntity<HttpResponseDTO<Map<String,BidDTO>>> getNFTBidInfo(
            @PathVariable String collection,
            @PathVariable int nftId,
            @PathVariable int bidId) {
        HttpResponseDTO<Map<String,BidDTO>> bidInfo = tradeService.getNFTBidInfo(collection, nftId, bidId);
        return new ResponseEntity<>(bidInfo, HttpStatus.OK);
    }

    // Activate bidding for an NFT
    @PostMapping("/activateBidding/{nftId}")
    public ResponseEntity<HttpResponseDTO<TransactionResponseDTO>> activateBidding(
            @PathVariable int nftId) {
        HttpResponseDTO<TransactionResponseDTO> response = tradeService.activateBidding(nftId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Deactivate bidding for an NFT
    @PostMapping("/deactivateBidding/{nftId}")
    public ResponseEntity<HttpResponseDTO<TransactionResponseDTO>> deactivateBidding(
            @PathVariable int nftId) {
        HttpResponseDTO<TransactionResponseDTO> response = tradeService.deactivateBidding(nftId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Create a new bid for an NFT
    @PostMapping("/bid")
    public ResponseEntity<HttpResponseDTO<TransactionResponseDTO>> createBid(@RequestBody BidRequestDTO bidRequest) {
        // Extract the bidRequest and privateKey from the combined DTO
        HttpResponseDTO<TransactionResponseDTO> response = tradeService.createBid(bidRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Accept a bid for an NFT
    @PostMapping("/acceptBid/{nftId}/{bidId}")
    public ResponseEntity<HttpResponseDTO<TransactionResponseDTO>> acceptBid(
            @PathVariable int nftId,
            @PathVariable int bidId) {
        HttpResponseDTO<TransactionResponseDTO> response = tradeService.acceptBid(nftId, bidId);
        return new ResponseEntity<>(response, HttpStatus.OK);
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