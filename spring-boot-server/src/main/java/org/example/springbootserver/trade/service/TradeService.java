package org.example.springbootserver.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.onchain.dto.TransactionResponseDTO;
import org.example.springbootserver.onchain.service.OnchainService;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.repository.PostRepository;
import org.example.springbootserver.trade.dto.BidDTO;
import org.example.springbootserver.trade.dto.BidResponseDTO;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.repository.UserRepository;
import org.example.springbootserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class TradeService {

    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;

    @Value("${spring.blockchain-server.contract.NFT_CONTRACT_ADDRESS}")
    private String NFT_CONTRACT_ADDRESS;

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final OnchainService onchainService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public TradeService(UserService userService, OnchainService onchainService, PostRepository postRepository, UserRepository userRepository) {
        this.userService = userService;
        this.onchainService = onchainService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }
    // Get marketplace name
    public HttpResponseDTO<Map<String, String>> getMarketPlaceName() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/contract-name")
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);

            String marketplaceName = (String) ((Map<String, Object>) responseMap.get("data")).get("marketplaceName");

            Map<String, String> data = new HashMap<>();
            data.put("marketplaceName", marketplaceName);

            // Return the HttpResponseDTO
            return new HttpResponseDTO<>("success", 200, "Marketplace name retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            Map<String, String> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());

            return new HttpResponseDTO<>("error", 500, "Failed to retrieve marketplace name", errorData, Instant.now());
        }
    }

    // Get NFT active status
    public HttpResponseDTO<Map<String, Boolean>> getNFTActive(String addressNFTCollection, int nftId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/nft-active/" + addressNFTCollection + "/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);

            Boolean nftAllowedBid = (Boolean) ((Map<String, Object>) responseMap.get("data")).get("nftAllowedBid");

            Map<String, Boolean> data = new HashMap<>();
            data.put("nftAllowedBid", nftAllowedBid);

            // Return the HttpResponseDTO
            return new HttpResponseDTO<>("success", 200, "NFT active status retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            Map<String, Boolean> errorData = new HashMap<>();
            errorData.put("error", true);

            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT active status", errorData, Instant.now());
        }
    }

    // Get bids on an NFT
    public HttpResponseDTO<Map<String, List<BidDTO>>> getNFTBids(String addressNFTCollection, int nftId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/nft-bids/" + addressNFTCollection + "/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            List<Map<String, Object>> bidsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("data")).get("bidsOnNFT");

            // Convert to BidDTO list
            List<BidDTO> bidDTOList = bidsList.stream().map(bid -> BidDTO.builder()
                    .addressNFTCollection((String) bid.get("addressNFTCollection"))
                    .nftId(Long.parseLong((String) bid.get("nftId")))
                    .addressPaymentToken((String) bid.get("addressPaymentToken"))
                    .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
                    .endTime(Long.parseLong((String) bid.get("endTime")))
                    .bidder((String) bid.get("bidder"))
                    .build()
            ).toList();

            Map<String, List<BidDTO>> data = new HashMap<>();
            data.put("bidsOnNFT", bidDTOList);

            // Return success response
            return new HttpResponseDTO<>("success", 200, "NFT bids retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT bids", null, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, BidDTO>> getNFTBidInfo(String addressNFTCollection, int nftId, int bidId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/nft-bid-info/" + addressNFTCollection + "/" + nftId + "/" + bidId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            Map<String, Object> bidInfoMap = (Map<String, Object>) ((Map<String, Object>) responseMap.get("data")).get("bidInfo");

            // Convert to BidDTO
            BidDTO bidDTO = BidDTO.builder()
                    .addressNFTCollection((String) bidInfoMap.get("addressNFTCollection"))
                    .nftId(Long.parseLong((String) bidInfoMap.get("nftId")))
                    .addressPaymentToken((String) bidInfoMap.get("addressPaymentToken"))
                    .amountPaymentToken(Long.parseLong((String) bidInfoMap.get("amountPaymentToken")))
                    .endTime(Long.parseLong((String) bidInfoMap.get("endTime")))
                    .bidder((String) bidInfoMap.get("bidder"))
                    .build();

            Map<String, BidDTO> data = new HashMap<>();
            data.put("bidInfo", bidDTO);

            // Return success response
            return new HttpResponseDTO<>("success", 200, "NFT bid info retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT bid info", null, Instant.now());
        }
    }

    public HttpResponseDTO<TransactionResponseDTO> activateBidding(int nftId) {

        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/activate-bidding/" + NFT_CONTRACT_ADDRESS + "/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            UserEntity currentUser = userService.getCurrentUserEntity();
            String privateKeyDB = currentUser.getWalletPrivateKey();
            // Create a map to hold the private key
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("privateKey", privateKeyDB);

            // Convert the request body to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            Map<String, Object> responseMap;
            responseMap = objectMapper.readValue(response.getBody(), HashMap.class);

            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
            TransactionResponseDTO transactionResponse = TransactionResponseDTO.from(dataMap);

            return new HttpResponseDTO<>("success", 200, "Bidding activated", transactionResponse, Instant.now());
        } catch (RestClientException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        } catch (JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to Parse Json from Response", null, Instant.now());
        }
    }


    // Deactivate bidding
    public HttpResponseDTO<TransactionResponseDTO> deactivateBidding(int nftId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/deactivate-bidding/" + NFT_CONTRACT_ADDRESS + "/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            UserEntity currentUser = userService.getCurrentUserEntity();
            String privateKeyDB = currentUser.getWalletPrivateKey();
            // Create a map to hold the private key
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("privateKey", privateKeyDB);

            // Convert the request body to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            Map<String, Object> responseMap;
            responseMap = objectMapper.readValue(response.getBody(), HashMap.class);

            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
            TransactionResponseDTO transactionResponse = TransactionResponseDTO.from(dataMap);

            return new HttpResponseDTO<>("success", 200, "Bidding deactivated", transactionResponse, Instant.now());
        } catch (RestClientException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        } catch (JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to Parse Json from Response", null, Instant.now());
        }
    }

    // Create Bid
    public HttpResponseDTO<TransactionResponseDTO> createBid(BidDTO bidRequest, String privateKey) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/bid")
                    .encode()
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the body of the request
            Map<String, Object> body = new HashMap<>();
            body.put("bidRequest", bidRequest);  // BidDTO is already structured
            body.put("privateKey", privateKey);  // Adding the private key

            // Convert the body to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(body);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            // Parse the JSON response
            Map<String, Object> responseMap;
            try {
                responseMap = objectMapper.readValue(response.getBody(), HashMap.class);
            } catch (JsonProcessingException e) {
                return new HttpResponseDTO<>("error", 500, "Failed to Parse JSON from Response", null, Instant.now());
            }

            // Extract the data field from the response and map it to TransactionResponseDTO
            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
            TransactionResponseDTO transactionResponse = TransactionResponseDTO.from(dataMap);

            return new HttpResponseDTO<>("success", 200, "Bidding created successfully", transactionResponse, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }
    // Accept a bid
    public HttpResponseDTO<TransactionResponseDTO> acceptBid(String collection, int nftId, int bidId, String privateKey) {
        try{
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/acceptBid/" + collection + "/" + nftId + "/" + bidId)
                    .encode()
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

//            System.out.println(privateKey); // privateKey가 { "privateKey" : " " } 형태로 넘어온다.

            HttpEntity<String> requestEntity = new HttpEntity<>(privateKey, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            Map<String, Object> responseMap;
            try{
                responseMap = objectMapper.readValue(response.getBody(), HashMap.class);
            } catch (JsonProcessingException e) {
                return new HttpResponseDTO<>("error", 500, "Failed to Parse Json from Response", null, Instant.now());
            }
            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");

            TransactionResponseDTO transactionResponse = TransactionResponseDTO.from(dataMap);

            return new HttpResponseDTO<>("success", 200, "Bidding Accepted Successfully", transactionResponse, Instant.now());
        } catch (RestClientException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }

    // Get bids on an NFT
    public HttpResponseDTO<Map<String, List<BidResponseDTO>>> getAllBidsReceived() {
        System.out.printf("getAllBidsReceived activated \n");
        try {
            // Retrieve the current user
            UserEntity currentUser = userService.getCurrentUserEntity();
            List<PostEntity> userOwnedPosts = postRepository.findAllByOwnerUserId(currentUser);
            Set<Long> ownedNftIds = userOwnedPosts.stream()
                    .map(PostEntity::getNftId)
                    .collect(Collectors.toSet());

            if (ownedNftIds.isEmpty()) {
                // If the user doesn't own any NFTs, return an empty response
                return new HttpResponseDTO<>("success", 200, "No NFTs owned by the user", Map.of("bidsOnNFT", new ArrayList<BidResponseDTO>()), Instant.now());
            }

            // Get the total count of NFTs
            Long nftCount = onchainService.getNftCountLocal();

            RestTemplate restTemplate = new RestTemplate();
            List<BidResponseDTO> allBidsForOwnedNFTs = new ArrayList<>();

            // Loop through each NFT ID from 0 to nftCount-1
            for (long nftId = 0; nftId < nftCount; nftId++) {
                System.out.println("Sending BC Http Request for nftID: " + nftId);
                URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                        .path("/trade/nft-bids/" + NFT_CONTRACT_ADDRESS + "/" + nftId)
                        .encode()
                        .build()
                        .toUri();

                // Send HTTP request for each NFT ID
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

                // Parse the JSON response
                Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
                List<Map<String, Object>> bidsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("data")).get("bidsOnNFT");

                // Convert to BidDTO list for each bid
                List<BidDTO> bidDTOList = bidsList.stream().map(bid -> BidDTO.builder()
                        .addressNFTCollection((String) bid.get("addressNFTCollection"))
                        .nftId(Long.parseLong((String) bid.get("nftId")))
                        .addressPaymentToken((String) bid.get("addressPaymentToken"))
                        .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
                        .endTime(Long.parseLong((String) bid.get("endTime")))
                        .bidder((String) bid.get("bidder"))
                        .build()
                ).toList();

                // Add bids only for NFTs that the user owns and map them to BidResponseDTO
                for (BidDTO bidDTO : bidDTOList) {
                    if (ownedNftIds.contains(bidDTO.getNftId())) {
                        // Retrieve bidder name from UserEntity
                        UserEntity bidder = userRepository.findByWalletAddress(bidDTO.getBidder());
                        String bidderAccountId = bidder != null ? bidder.getAccountId() : "Unknown";

                        // Retrieve post and image URL
                        Optional<PostEntity> postEntityOptional = postRepository.findByNftId(bidDTO.getNftId());
                        String imgUrl = postEntityOptional.isPresent() && !postEntityOptional.get().getImages().isEmpty()
                                ? postEntityOptional.get().getImages().get(0).getImgUrl()
                                : "No Image";

                        // Build BidResponseDTO
                        BidResponseDTO bidResponseDTO = BidResponseDTO.builder()
                                .bidDTO(bidDTO)
                                .bidderAccountId(bidderAccountId)
                                .imgUrl(imgUrl)
                                .build();

                        allBidsForOwnedNFTs.add(bidResponseDTO);
                    }
                }
            }

            Map<String, List<BidResponseDTO>> data = new HashMap<>();
            data.put("bidsOnNFT", allBidsForOwnedNFTs);

            // Return success response
            return new HttpResponseDTO<>("success", 200, "NFT bids retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT bids", null, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, List<BidResponseDTO>>> getAllBidsProposedByUser() {
        System.out.printf("getAllBidsPlacedByUser activated \n");
        try {
            // Retrieve the current user's wallet address
            UserEntity currentUser = userService.getCurrentUserEntity();
            String userWalletAddress = currentUser.getWalletAddress();

            if (userWalletAddress == null || userWalletAddress.isEmpty()) {
                // If the user doesn't have a wallet address, return an empty response
                return new HttpResponseDTO<>("success", 200, "User does not have a wallet address", Map.of("bidsPlacedByUser", new ArrayList<BidResponseDTO>()), Instant.now());
            }

            // Get the total count of NFTs
            Long nftCount = onchainService.getNftCountLocal();

            RestTemplate restTemplate = new RestTemplate();
            List<BidResponseDTO> allBidsPlacedByUser = new ArrayList<>();

            // Loop through each NFT ID from 0 to nftCount-1
            for (long nftId = 0; nftId < nftCount; nftId++) {
                System.out.println("Sending BC Http Request for nftID : " + nftId);
                URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                        .path("/trade/nft-bids/" + NFT_CONTRACT_ADDRESS + "/" + nftId)
                        .encode()
                        .build()
                        .toUri();

                // Send HTTP request for each NFT ID
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

                // Parse the JSON response
                Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
                List<Map<String, Object>> bidsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("data")).get("bidsOnNFT");

                // Convert to BidDTO list for each bid
                List<BidDTO> bidDTOList = bidsList.stream().map(bid -> BidDTO.builder()
                        .addressNFTCollection((String) bid.get("addressNFTCollection"))
                        .nftId(Long.parseLong((String) bid.get("nftId")))
                        .addressPaymentToken((String) bid.get("addressPaymentToken"))
                        .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
                        .endTime(Long.parseLong((String) bid.get("endTime")))
                        .bidder((String) bid.get("bidder"))
                        .build()
                ).toList();

                // Add bids where the bidder matches the current user's wallet address
                for (BidDTO bidDTO : bidDTOList) {
                    if (userWalletAddress.equalsIgnoreCase(bidDTO.getBidder())) {
                        // Retrieve bidder name from UserEntity
                        UserEntity bidder = userRepository.findByWalletAddress(bidDTO.getBidder());
                        String bidderAccountId = bidder != null ? bidder.getAccountId() : "Unknown";

                        // Retrieve post and image URL
                        Optional<PostEntity> postEntityOptional = postRepository.findByNftId(bidDTO.getNftId());
                        String imgUrl = postEntityOptional.isPresent() && !postEntityOptional.get().getImages().isEmpty()
                                ? postEntityOptional.get().getImages().get(0).getImgUrl()
                                : "No Image";

                        // Build BidResponseDTO
                        BidResponseDTO bidResponseDTO = BidResponseDTO.builder()
                                .bidDTO(bidDTO)
                                .bidderAccountId(bidderAccountId)
                                .imgUrl(imgUrl)
                                .build();

                        allBidsPlacedByUser.add(bidResponseDTO);
                    }
                }
            }

            Map<String, List<BidResponseDTO>> data = new HashMap<>();
            data.put("bidsPlacedByUser", allBidsPlacedByUser);

            // Return success response
            return new HttpResponseDTO<>("success", 200, "NFT bids placed by the user retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT bids placed by the user", null, Instant.now());
        }
    }

}
