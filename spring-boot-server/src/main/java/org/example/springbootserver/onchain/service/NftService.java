package org.example.springbootserver.onchain.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.onchain.constant.NftConstant;
import org.example.springbootserver.onchain.dto.NftMintResponseDTO;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class NftService {

    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;

    private final UserDetailsServiceImpl userDetailsService;
    private final RestTemplate restTemplate;

    public String pingResponse() {
        URI uri =
                UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                        .path("/nft/ping")
                        .queryParam("sender", "fromSpring")
                        .encode()
                        .build()
                        .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public NftMintResponseDTO nftMintRequest(String description, MultipartFile file) throws IOException, RestClientException {

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path("/nft/mint")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("accountAddress", String.valueOf(currentUser.getWalletAddress()));
        body.add("accountPrivateKey", String.valueOf(currentUser.getWalletPrivateKey()));
        body.add("name", String.valueOf(currentUser.getName()));
        body.add("description", String.valueOf(description));
        body.add("file", createImgFile(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<HttpResponseDTOv2<NftMintResponseDTO>> response = restTemplate.exchange(
                uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

        System.out.println(response.getBody().getResponse().getTxResult());

        return response.getBody().getResponse();
    }

    public static Resource createImgFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = file.getOriginalFilename().substring(fileName.lastIndexOf(".") + 1);

        InputStream inputStream = file.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        Path imgFile = Files.createTempFile("img", "." + extension);
        System.out.println("Creating and Uploading  File : " + imgFile);
        Files.write(imgFile, bytes);
        return new FileSystemResource(imgFile.toFile());
    }

}
