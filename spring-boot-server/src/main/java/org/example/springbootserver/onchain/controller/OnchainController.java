package org.example.springbootserver.onchain.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.onchain.service.OnchainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/onchain")
@RequiredArgsConstructor
public class OnchainController {

    private final OnchainService onchainService;

    @GetMapping("/nft-count")
    public ResponseEntity<HttpResponseDTO<Map<String, String>>> getNftCount() {
        HttpResponseDTO<Map<String, String>> response = onchainService.getNftCount();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/token-balance/{address}")
    public ResponseEntity<HttpResponseDTO<Map<String, Long>>> getTokenBalance(@PathVariable String address) {
        HttpResponseDTO<Map<String, Long>> response = onchainService.getTokenBalance(address);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/token-balance")
    public ResponseEntity<HttpResponseDTO<Map<String, Long>>> getTokenBalance() {
        HttpResponseDTO<Map<String, Long>> response = onchainService.getCurUserTokenBalance();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/nft-balance/{address}")
    public ResponseEntity<HttpResponseDTO<Map<String, Integer>>> getNftBalance(@PathVariable String address) {
        HttpResponseDTO<Map<String, Integer>> response = onchainService.getNftBalance(address);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/nft-collection-name")
    public ResponseEntity<HttpResponseDTO<Map<String,String>>> getNftCollectionName() {
        HttpResponseDTO<Map<String,String>> response = onchainService.getNftCollectionName();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/nft-collection-owner")
    public ResponseEntity<HttpResponseDTO<Map<String,String>>> getNftCollectionOwner() {
        HttpResponseDTO<Map<String,String>> response = onchainService.getNftCollectionOwner();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/nft-owner/{nftId}")
    public ResponseEntity<HttpResponseDTO<Map<String,String>>> getNftOwner(@PathVariable String nftId) {
        HttpResponseDTO<Map<String,String>> response = onchainService.getNftOwner(nftId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/token-uri/{nftId}")
    public ResponseEntity<HttpResponseDTO<Map<String,String>>> getTokenUri(@PathVariable String nftId) {
        HttpResponseDTO<Map<String,String>> response = onchainService.getTokenUri(nftId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
