package org.example.springbootserver.trade.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BidResponseDTO {
    private BidDTO bidDTO;  // Composition with BidDTO
    private String bidderName; // Additional field for bidder's name
    private String imgUrl;     // Additional field for image URL

    @Override
    public String toString() {
        return "BidResponseDTO{" +
                "bidDTO=" + bidDTO + // This will call the toString method of BidDTO
                ", bidderName='" + bidderName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}