package org.example.springbootserver.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.onchain.dto.TransactionResponseDTO;
import org.example.springbootserver.trade.dto.BidDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeService {

    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;
    private final ObjectMapper objectMapper;

    public TradeService() {
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


    // Activate bidding
    public HttpResponseDTO<TransactionResponseDTO> activateBidding(String collection, int nftId, String privateKey) {
        try{
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/activate-bidding/" + collection + "/" + nftId)
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

            return new HttpResponseDTO<>("success", 200, "Bidding activated", transactionResponse, Instant.now());
        } catch (RestClientException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }

    }

    // Deactivate bidding
    public HttpResponseDTO<TransactionResponseDTO> deactivateBidding(String collection, int nftId, String privateKey) {
        try{
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/trade/deactivate-bidding/" + collection + "/" + nftId)
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

            return new HttpResponseDTO<>("success", 200, "Bidding activated", transactionResponse, Instant.now());
        } catch (RestClientException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
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

}
