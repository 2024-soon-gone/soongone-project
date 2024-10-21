package org.example.springbootserver.trade.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BidDTO {
    private String addressNFTCollection;
    private Long nftId;
    private String addressPaymentToken;
    private Long amountPaymentToken;
    private Long endTime;
    private String bidder;

    // Getters and Setters

    @Override
    public String toString() {
        return "BidDTO{" +
                "addressNFTCollection='" + addressNFTCollection + '\'' +
                ", nftId=" + nftId +
                ", addressPaymentToken='" + addressPaymentToken + '\'' +
                ", amountPaymentToken=" + amountPaymentToken +
                ", endTime=" + endTime +
                ", bidder='" + bidder + '\'' +
                '}';
    }
}

