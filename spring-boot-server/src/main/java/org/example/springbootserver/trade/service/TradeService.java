package org.example.springbootserver.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.onchain.dto.TransactionResponseDTO;
import org.example.springbootserver.onchain.service.OnchainService;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.repository.PostRepository;
import org.example.springbootserver.trade.dto.BidDTO;
import org.example.springbootserver.trade.dto.BidRequestDTO;
import org.example.springbootserver.trade.dto.BidResponseDTO;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final RestTemplate restTemplate;
    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;

    @Value("${spring.blockchain-server.contract.NFT_CONTRACT_ADDRESS}")
    private String NFT_CONTRACT_ADDRESS;

    @Value("${spring.blockchain-server.contract.TOKEN_CONTRACT_ADDRESS}")
    private String TOKEN_CONTRACT_ADDRESS;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplateFromConfig;
    private final UserDetailsServiceImpl userDetailsService;
    private final OnchainService onchainService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Get marketplace name
    public ResponseEntity<HttpResponseDTOv2> getMarketPlaceName() {
        String url = "/trade/contract-name";
        return restTemplateFromConfig.getForEntity(url, HttpResponseDTOv2.class);
    }

    // Get NFT active status
    public ResponseEntity<HttpResponseDTOv2> getNFTActive(String addressNFTCollection, int nftId) {
        String url = "/trade/nft-active/" + addressNFTCollection + "/" + nftId;
        return restTemplateFromConfig.getForEntity(url, HttpResponseDTOv2.class);
    }

    // WIP : Request is processed within seconds but Takes too long to fetch actual response in PostMan.
    public ResponseEntity<HttpResponseDTOv2<Map<String, List<BidDTO>>>> getNFTBids(String addressNFTCollection, int nftId) {
        String url = "/trade/nft-bids/" + addressNFTCollection + "/" + nftId;

        // Use ParameterizedTypeReference to specify the response type with generics
        ResponseEntity<HttpResponseDTOv2<Map<String, List<BidDTO>>>> response = restTemplateFromConfig.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response;
    }
//    public HttpResponseDTO<Map<String, List<BidDTO>>> getNFTBids(String addressNFTCollection, int nftId) {
//        try {
//            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
//                    .path("/trade/nft-bids/" + addressNFTCollection + "/" + nftId)
//                    .encode()
//                    .build()
//                    .toUri();
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
//
//            // Parse the JSON response
//            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
//            List<Map<String, Object>> bidsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("data")).get("bidsOnNFT");
//
//            // Convert to BidDTO list
//            List<BidDTO> bidDTOList = bidsList.stream().map(bid -> BidDTO.builder()
//                    .addressNFTCollection((String) bid.get("addressNFTCollection"))
//                    .nftId(Long.parseLong((String) bid.get("nftId")))
//                    .addressPaymentToken((String) bid.get("addressPaymentToken"))
//                    .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
//                    .endTime(Long.parseLong((String) bid.get("endTime")))
//                    .bidder((String) bid.get("bidder"))
//                    .build()
//            ).toList();
//
//            Map<String, List<BidDTO>> data = new HashMap<>();
//            data.put("bidsOnNFT", bidDTOList);
//
//            // Return success response
//            return new HttpResponseDTO<>("success", 200, "NFT bids retrieved successfully", data, Instant.now());
//        } catch (RestClientException | JsonProcessingException e) {
//            return new HttpResponseDTO<>("error", 500, "Failed to retrieve NFT bids", null, Instant.now());
//        }
//    }

    // Get information about a specific bid on an NFT
    public ResponseEntity<HttpResponseDTOv2> getNFTBidInfo(String addressNFTCollection, int nftId, int bidId) {
        String url = "/trade/nft-bid-info/" + addressNFTCollection + "/" + nftId + "/" + bidId;

        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        // Make GET request and return the response directly
        return restTemplateFromConfig.getForEntity(uri, HttpResponseDTOv2.class);
    }

    public BidDTO getNFTBidInfoLocal(int nftId, int bidId) {
        String url = "/trade/nft-bid-info/" + NFT_CONTRACT_ADDRESS + "/" + nftId + "/" + bidId;

        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        // Make GET request and parse the response directly
        ResponseEntity<HttpResponseDTOv2> responseEntity = restTemplateFromConfig.getForEntity(uri, HttpResponseDTOv2.class);
        HttpResponseDTOv2 responseDTO = responseEntity.getBody();

        if (responseDTO != null && responseDTO.getResponse() != null) {
            // Extract bidInfo from the response
            Map<String, Object> bidInfoMap = (Map<String, Object>) ((Map<String, Object>) responseDTO.getResponse()).get("bidInfo");

            // Convert to BidDTO
            return BidDTO.builder()
                    .addressNFTCollection((String) bidInfoMap.get("addressNFTCollection"))
                    .nftId(Long.parseLong((String) bidInfoMap.get("nftId")))
                    .addressPaymentToken((String) bidInfoMap.get("addressPaymentToken"))
                    .amountPaymentToken(Long.parseLong((String) bidInfoMap.get("amountPaymentToken")))
                    .endTime(Long.parseLong((String) bidInfoMap.get("endTime")))
                    .bidder((String) bidInfoMap.get("bidder"))
                    .build();
        }

        return null; // Return null if the response is empty or doesn't contain bidInfo
    }

    // Activate bidding on an NFT
    public ResponseEntity<HttpResponseDTOv2> activateBidding(int nftId) {
        String url = "/trade/activate-bidding/" + NFT_CONTRACT_ADDRESS + "/" + nftId;
        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        String privateKeyDB = currentUser.getWalletPrivateKey();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("privateKey", privateKeyDB);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make POST request and receive a response
        return restTemplateFromConfig.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                HttpResponseDTOv2.class
        );
    }

    public ResponseEntity<HttpResponseDTOv2> deactivateBidding(int nftId) {
        String url = "/trade/deactivate-bidding/" + NFT_CONTRACT_ADDRESS + "/" + nftId;
        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        String privateKeyDB = currentUser.getWalletPrivateKey();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("privateKey", privateKeyDB);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplateFromConfig.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                HttpResponseDTOv2.class
        );
    }

    public ResponseEntity<HttpResponseDTOv2> createBid(BidRequestDTO bidRequest) {
        String url = "/trade/bid";

        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        String privateKey = currentUser.getWalletPrivateKey();
        String walletAddress = currentUser.getWalletAddress();

        // Build the request body with BidDTO and privateKey
        BidDTO bidDTO = BidDTO.builder()
                .addressNFTCollection(NFT_CONTRACT_ADDRESS)
                .nftId(bidRequest.getNftId())
                .addressPaymentToken(TOKEN_CONTRACT_ADDRESS)
                .amountPaymentToken(bidRequest.getAmountPaymentToken())
                .endTime(bidRequest.getEndTime())
                .bidder(walletAddress)
                .build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("bidRequest", bidDTO);
        requestBody.put("privateKey", privateKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request and return the response
        return restTemplateFromConfig.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                HttpResponseDTOv2.class
        );
    }

    // Accept a bid for an NFT
    public ResponseEntity<HttpResponseDTOv2> acceptBid(int nftId, int bidId) {
        String url = "/trade/acceptBid/" + NFT_CONTRACT_ADDRESS + "/" + nftId + "/" + bidId;

        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path(url)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        String privateKey = currentUser.getWalletPrivateKey();

        // Build request body with privateKey
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("privateKey", privateKey);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request and return the response
        ResponseEntity<HttpResponseDTOv2> response = restTemplateFromConfig.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                HttpResponseDTOv2.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {

            BidDTO acceptedBid = getNFTBidInfoLocal(nftId, bidId);

            // Find the post by NFT ID and update the owner
            postRepository.findByNftId((long) nftId).ifPresent(post -> {
                UserEntity newOwner = userRepository.findByWalletAddress(acceptedBid.getBidder());
                if (newOwner != null) {
                    post.setOwnerUserId(newOwner);
                    postRepository.save(post);
                }
            });
        }

        return response;
    }



    // Get bids on NFTs owned by the current user
    public HttpResponseDTO<Map<String, List<BidResponseDTO>>> getAllBidsReceived() {
        System.out.printf("getAllBidsReceived activated \n");
        try {
            // Retrieve the current user
            UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
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

                // Assign a bidId starting from 0 for each nftId
                long bidIdCounter = 0;

                // Convert to BidDTO list for each bid and filter based on owned NFTs
                for (Map<String, Object> bid : bidsList) {
                    BidDTO bidDTO = BidDTO.builder()
                            .addressNFTCollection((String) bid.get("addressNFTCollection"))
                            .nftId(Long.parseLong((String) bid.get("nftId")))
                            .addressPaymentToken((String) bid.get("addressPaymentToken"))
                            .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
                            .endTime(Long.parseLong((String) bid.get("endTime")))
                            .bidder((String) bid.get("bidder"))
                            .build();

                    // Add bids only for NFTs that the user owns and map them to BidResponseDTO
                    if (ownedNftIds.contains(bidDTO.getNftId())) {
                        // Retrieve bidder name from UserEntity
                        UserEntity bidder = userRepository.findByWalletAddress(bidDTO.getBidder());
                        String bidderAccountId = bidder != null ? bidder.getAccountId() : "Unknown";

                        // Retrieve post and image URL
                        Optional<PostEntity> postEntityOptional = postRepository.findByNftId(bidDTO.getNftId());
                        String imgUrl = postEntityOptional.isPresent() && !postEntityOptional.get().getImages().isEmpty()
                                ? postEntityOptional.get().getImages().get(0).getImgUrl()
                                : "No Image";

                        // Build BidResponseDTO with the bidId and other details
                        BidResponseDTO bidResponseDTO = BidResponseDTO.builder()
                                .bidDTO(bidDTO)
                                .bidderAccountId(bidderAccountId)
                                .imgUrl(imgUrl)
                                .bidId(bidIdCounter++)  // Increment bidId for each bid of the current nftId
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

    // Get all bids placed by the current user
    public HttpResponseDTO<Map<String, List<BidResponseDTO>>> getAllBidsProposedByUser() {
        System.out.printf("getAllBidsPlacedByUser activated \n");
        try {
            // Retrieve the current user's wallet address
            UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
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

                // Assign a bidId starting from 0 for each nftId
                long bidIdCounter = 0;

                // Convert to BidDTO list for each bid and filter based on the user's wallet address
                for (Map<String, Object> bid : bidsList) {
                    BidDTO bidDTO = BidDTO.builder()
                            .addressNFTCollection((String) bid.get("addressNFTCollection"))
                            .nftId(Long.parseLong((String) bid.get("nftId")))
                            .addressPaymentToken((String) bid.get("addressPaymentToken"))
                            .amountPaymentToken(Long.parseLong((String) bid.get("amountPaymentToken")))
                            .endTime(Long.parseLong((String) bid.get("endTime")))
                            .bidder((String) bid.get("bidder"))
                            .build();

                    // Add bids where the bidder matches the current user's wallet address
                    if (userWalletAddress.equalsIgnoreCase(bidDTO.getBidder())) {
                        // Retrieve bidder name (wallet address -> account ID)
                        UserEntity bidder = userRepository.findByWalletAddress(bidDTO.getBidder());
                        String bidderAccountId = bidder != null ? bidder.getAccountId() : "Unknown";

                        // Retrieve post and image URL
                        Optional<PostEntity> postEntityOptional = postRepository.findByNftId(bidDTO.getNftId());
                        String imgUrl = postEntityOptional.isPresent() && !postEntityOptional.get().getImages().isEmpty()
                                ? postEntityOptional.get().getImages().get(0).getImgUrl()
                                : "No Image";

                        // Build BidResponseDTO with the bidId and other details
                        BidResponseDTO bidResponseDTO = BidResponseDTO.builder()
                                .bidDTO(bidDTO)
                                .bidderAccountId(bidderAccountId)
                                .imgUrl(imgUrl)
                                .bidId(bidIdCounter++)  // Increment bidId for each bid of the current nftId
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
