package org.example.springbootserver.onchain.service;


import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.user.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OnchainService {

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
