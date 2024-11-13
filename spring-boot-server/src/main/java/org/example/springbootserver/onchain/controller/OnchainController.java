package org.example.springbootserver.onchain.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
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
    public ResponseEntity<HttpResponseDTOv2> getNftCount() {
        return onchainService.getNftCount();
    }

    @GetMapping("/token-balance/{address}")
    public ResponseEntity<HttpResponseDTOv2> getTokenBalance(@PathVariable String address) {
        return onchainService.getTokenBalance(address);
    }

    @GetMapping("/token-balance")
    public ResponseEntity<HttpResponseDTOv2>  getTokenBalance() {
        return onchainService.getTokenBalance();
    }

    @GetMapping("/nft-balance/{address}")
    public ResponseEntity<HttpResponseDTOv2> getNftBalance(@PathVariable String address) {
        return onchainService.getNftBalance(address);
    }

    @GetMapping("/nft-collection-name")
    public ResponseEntity<HttpResponseDTOv2> getNftCollectionName() {
        return onchainService.getNftCollectionName();
    }

    @GetMapping("/nft-collection-owner")
    public ResponseEntity<HttpResponseDTOv2> getNftCollectionOwner() {
        return onchainService.getNftCollectionOwner();
    }

    @GetMapping("/nft-owner/{nftId}")
    public ResponseEntity<HttpResponseDTOv2> getNftOwner(@PathVariable String nftId) {
        return onchainService.getNftOwner(nftId);
    }

    @GetMapping("/token-uri/{nftId}")
    public ResponseEntity<HttpResponseDTOv2> getTokenUri(@PathVariable String nftId) {
        return onchainService.getTokenUri(nftId);
    }

}
