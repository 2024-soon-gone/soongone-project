package org.example.springbootserver.onchain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NftMintResponseDTO {

    private TxResult txResult;

    @Getter
    @NoArgsConstructor
    public static class TxResult {
        private int statusCode;
        private String message;
        private String nftIpfsHash;
        private String transactionHash;
        private String nftImgIpfsUri;
        private Long nftId;

        @Override
        public String toString() {
            return "TxResult{" +
                    "statusCode=" + statusCode +
                    ", message='" + message + '\'' +
                    ", nftIpfsHash='" + nftIpfsHash + '\'' +
                    ", transactionHash='" + transactionHash + '\'' +
                    ", nftImgIpfsUri='" + nftImgIpfsUri + '\'' +
                    ", nftId=" + nftId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NftMintResponseDTO{" +
                "txResult=" + txResult +
                '}';
    }
}