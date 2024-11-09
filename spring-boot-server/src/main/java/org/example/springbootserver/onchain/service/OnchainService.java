package org.example.springbootserver.onchain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.config.BlockchainApiConfig;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.service.UserService;
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
@RequiredArgsConstructor
public class OnchainService {
    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;

    private final ObjectMapper objectMapper = new ObjectMapper();
    //private final ObjectMapper objectMapper
    private final UserDetailsServiceImpl userDetailsService;
    private final RestTemplate restTemplate;

    public ResponseEntity<HttpResponseDTOv2> getNftCount() {
        String url = "/onchain/nft-count";
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public Long getNftCountLocal() {
        ResponseEntity<HttpResponseDTOv2> responseEntity = getNftCount();

        HttpResponseDTOv2 responseDTO = responseEntity.getBody();
        if (responseDTO != null && responseDTO.getResponse() != null) {
            Map<String, Object> responseMap = (Map<String, Object>) responseDTO.getResponse();
            String nftCountString = (String) responseMap.get("nftCount");

            return Long.valueOf(nftCountString);
        } else {
            throw new RuntimeException("NFT count response or data is null");
        }
    }

//    public Long getNftCountLocal() {
//        try {
//            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
//                    .path("/onchain/nft-count")
//                    .encode()
//                    .build()
//                    .toUri();
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
//
//            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
//
//            // Retrieve nftCount as a String first
//            String nftCountString = (String) ((Map<String, Object>) responseMap.get("data")).get("nftCount");
//
//            // Convert the String to Long
//            Long nftCount = Long.valueOf(nftCountString); // Use Long.parseLong(nftCountString) if you prefer
//
//            return nftCount;
//        } catch (RestClientException | JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public ResponseEntity<HttpResponseDTOv2> getTokenBalance() {
        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        String url = "/onchain/token-balance/" + currentUser.getWalletAddress();
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getTokenBalance(String address) {
        String url = "/onchain/token-balance/" + address;
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getNftBalance(String address) {
        String url = "/onchain/nft-balance/" + address;
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getNftCollectionName() {
        String url = "/onchain/nft-collection-name";
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getNftCollectionOwner() {
        String url = "/onchain/nft-collection-owner";
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getNftOwner(String nftId) {
        String url = "/onchain/nft-owner/" + nftId;
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }

    public ResponseEntity<HttpResponseDTOv2> getTokenUri(String nftId) {
        String url = "/onchain/token-uri/" + nftId;
        return restTemplate.getForEntity(url, HttpResponseDTOv2.class);
    }
}
