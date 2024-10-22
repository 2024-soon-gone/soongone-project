package org.example.springbootserver.onchain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnchainService {
    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;
    private final ObjectMapper objectMapper;

    public OnchainService() {
        this.objectMapper = new ObjectMapper();
    }

    public HttpResponseDTO<Map<String, String>> getNftCount() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-count")
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String nftCount = (String) ((Map<String, Object>) responseMap.get("data")).get("nftCount");

            Map<String, String> data = new HashMap<>();
            data.put("nftCount", nftCount);

            return new HttpResponseDTO<>("success", 200, "NFT Count retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }

    public Long getNftCountLocal() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-count")
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);

            // Retrieve nftCount as a String first
            String nftCountString = (String) ((Map<String, Object>) responseMap.get("data")).get("nftCount");

            // Convert the String to Long
            Long nftCount = Long.valueOf(nftCountString); // Use Long.parseLong(nftCountString) if you prefer

            return nftCount;
        } catch (RestClientException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponseDTO<Map<String, Integer>> getTokenBalance(String address) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/token-balance/" + address)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);

            String tokenBalanceString = (String) ((Map<String, Object>) responseMap.get("data")).get("balance");

            Integer tokenBalance = Integer.valueOf(tokenBalanceString);

            Map<String, Integer> data = new HashMap<>();
            data.put("balance", tokenBalance);

            // Return the HttpResponseDTO
            return new HttpResponseDTO<>("success", 200, "Token Balanced retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            Map<String, Integer> errorData = new HashMap<>();
            return new HttpResponseDTO<>("error", 500, e.getMessage(), errorData, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, Integer>> getNftBalance(String address) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-balance/" + address)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);

            String nftBalanceString = (String) ((Map<String, Object>) responseMap.get("data")).get("balance");
            Integer nftBalance = Integer.valueOf(nftBalanceString);

            Map<String, Integer> data = new HashMap<>();
            data.put("balance", nftBalance);

            return new HttpResponseDTO<>("success", 200, "NFT Balance retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            Map<String, Integer> errorData = new HashMap<>();
            return new HttpResponseDTO<>("error", 500, e.getMessage(), errorData, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, String>> getNftCollectionName() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-collection-name")
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String collectionName = (String) ((Map<String, Object>) responseMap.get("data")).get("collectionName");

            Map<String, String> data = new HashMap<>();
            data.put("collectionName", collectionName);

            return new HttpResponseDTO<>("success", 200, "NFT Collection Name retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }


    public HttpResponseDTO<Map<String, String>> getNftCollectionOwner() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-collection-owner")
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String ownerAddress = (String) ((Map<String, Object>) responseMap.get("data")).get("owner");

            Map<String, String> data = new HashMap<>();
            data.put("owner", ownerAddress);

            return new HttpResponseDTO<>("success", 200, "NFT Collection Owner retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, String>> getNftOwner(String nftId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/nft-owner/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String ownerAddress = (String) ((Map<String, Object>) responseMap.get("data")).get("owner");

            Map<String, String> data = new HashMap<>();
            data.put("owner", ownerAddress);

            return new HttpResponseDTO<>("success", 200, "NFT Owner retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }

    public HttpResponseDTO<Map<String, String>> getTokenUri(String nftId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/token-uri/" + nftId)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String tokenUri = (String) ((Map<String, Object>) responseMap.get("data")).get("tokenURI");

            Map<String, String> data = new HashMap<>();
            data.put("tokenUri", tokenUri);

            return new HttpResponseDTO<>("success", 200, "Token URI retrieved successfully", data, Instant.now());
        } catch (RestClientException | JsonProcessingException e) {
            return new HttpResponseDTO<>("error", 500, e.getMessage(), null, Instant.now());
        }
    }

}
