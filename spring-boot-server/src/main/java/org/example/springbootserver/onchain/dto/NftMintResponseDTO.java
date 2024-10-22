package org.example.springbootserver.onchain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NftMintResponseDTO {
    private String statusCode;
    private String message;
    private String nftIpfsHash;
    private String transactionHash;
//    private String nftImgHash;
    private String nftImgIpfsUri;
    private Long nftId;
}
