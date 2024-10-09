package org.example.springbootserver.onchain.service;

import org.example.springbootserver.onchain.constant.NftConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class NftService {

    public String pingResponse() {
        URI uri =
                UriComponentsBuilder.fromUriString(NftConstant.BC_SERVER_URL)
                        .path("/nft/ping")
                        .queryParam("sender", "fromSpring")
                        .encode()
                        .build()
                        .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }


}
