package org.example.springbootserver.trade.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BidRequestDTO {
    private Long nftId;
    private Long amountPaymentToken;
    private Long endTime;

    @Override
    public String toString() {
        return "BidRequestDTO{" +
                "nftId=" + nftId +
                ", amountPaymentToken=" + amountPaymentToken +
                ", endTime=" + endTime +
                '}';
    }
}
