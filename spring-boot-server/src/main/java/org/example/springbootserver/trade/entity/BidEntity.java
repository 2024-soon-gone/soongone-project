package org.example.springbootserver.trade.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.example.springbootserver.user.entity.BaseEntity;

@Entity
@Data
public class BidEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressNFTCollection;
    private Long nftId;
    private Long nftBidId;
    private String addressPaymentToken;
    private Long amountPaymentToken;
    private Long endTime;
    private String bidder;
}
