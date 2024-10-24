package org.example.springbootserver.trade.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BidResponseDTO {
    private BidDTO bidDTO;  // Composition with BidDTO
    private String bidderAccountId; // Additional field for bidder's name
    private String imgUrl;     // Additional field for image URL
    private Long bidId;

    @Override
    public String toString() {
        return "BidResponseDTO{" +
                "bidDTO=" + bidDTO + // This will call the toString method of BidDTO
                ", bidderName='" + bidderAccountId + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}