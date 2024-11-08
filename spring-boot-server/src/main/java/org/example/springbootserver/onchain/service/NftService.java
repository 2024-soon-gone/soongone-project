package org.example.springbootserver.onchain.service;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.onchain.constant.NftConstant;
import org.example.springbootserver.onchain.dto.NftMintResponseDTO;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
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

    public String nftMintRequest(String accountAddress, String name, String description, MultipartFile file) throws IOException, RestClientException {

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        // Build the URI
        URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                .path("/nft/mint")
                .encode()
                .build()
                .toUri();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.setBearerAuth("your_auth_token"); // Set authorization token, adjust as needed

        // Prepare the body using MultiValueMap for multipart/form-data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("accountAddress", String.valueOf(currentUser.getWalletAddress()));
        body.add("accountPrivateKey", String.valueOf(currentUser.getWalletPrivateKey()));
        body.add("name", String.valueOf(name));
        body.add("description", String.valueOf(description));
        body.add("file", createImgFile(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Initialize RestTemplate and send the POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        // Return the response body
        return response.getBody();
    }

    public static Resource createImgFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = file.getOriginalFilename().substring(fileName.lastIndexOf(".") + 1);

        InputStream inputStream = file.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Path imgFile = Files.createTempFile("img", "." + extension);
        System.out.println("Creating and Uploading Test file : " + imgFile);
        Files.write(imgFile, bytes);
        return new FileSystemResource(imgFile.toFile());
    }

}
