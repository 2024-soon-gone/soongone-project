package org.example.springbootserver.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.onchain.service.OnchainService;
import org.example.springbootserver.trade.entity.BidEntity;
import org.example.springbootserver.trade.repository.BidRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BidFetchService {

    @Value("${spring.blockchain-server.contract.NFT_CONTRACT_ADDRESS}")
    private String NFT_CONTRACT_ADDRESS;

    private final RestTemplate restTemplate;
    private final OnchainService onchainService;
    private final BidRepository bidRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 1000)
    public void fetch() {
        System.out.println("Periodic Fetch on MarketPlace Bid");
        Long nftCount = onchainService.getNftCountLocal();
        try{
            for (long nftId = 0; nftId < nftCount; nftId++) {
                System.out.println("Fetching bids for NFT ID: " + nftId);
                String url = "/trade/nft-bids/" + NFT_CONTRACT_ADDRESS + "/" + nftId;

                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

                parseAndUpdateBids(responseEntity.getBody());
            }
            System.out.println("All NFT bids fetched and stored successfully.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void parseAndUpdateBids(String responseBody) throws JsonProcessingException {
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        List<Map<String, Object>> bidsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("response")).get("bidsOnNFT");

        // Track fetched bids to delete redundant ones later
        Set<Long> fetchedBidIds = new HashSet<>();

        long nftBidIdCounter = 0;
        for (Map<String, Object> bidMap : bidsList) {
            Long nftBidId = nftBidIdCounter++;
            fetchedBidIds.add(nftBidId);

            String addressNFTCollection = (String) bidMap.get("addressNFTCollection");
            Long nftId = Long.parseLong((String) bidMap.get("nftId"));
            String addressPaymentToken = (String) bidMap.get("addressPaymentToken");
            Long amountPaymentToken = Long.parseLong((String) bidMap.get("amountPaymentToken"));
            Long endTime = Long.parseLong((String) bidMap.get("endTime"));
            String bidder = (String) bidMap.get("bidder");

            BidEntity existingBid = bidRepository.findByAddressNFTCollectionAndNftIdAndNftBidId(addressNFTCollection, nftId, nftBidId);

            if (existingBid == null) {
                BidEntity bidEntity = new BidEntity();
                bidEntity.setAddressNFTCollection(addressNFTCollection);
                bidEntity.setNftId(nftId);
                bidEntity.setNftBidId(nftBidId);
                bidEntity.setAddressPaymentToken(addressPaymentToken);
                bidEntity.setAmountPaymentToken(amountPaymentToken);
                bidEntity.setEndTime(endTime);
                bidEntity.setBidder(bidder);

                bidRepository.save(bidEntity);
            } else if (!existingBid.getEndTime().equals(endTime)) {
                existingBid.setEndTime(endTime);
                bidRepository.save(existingBid);
            }
        }
//
//        // Delete redundant bids not present in the latest fetched bids
//        bidRepository.deleteByNftIdAndNftBidIdNotIn(nftId, fetchedBidIds);
    }
}

