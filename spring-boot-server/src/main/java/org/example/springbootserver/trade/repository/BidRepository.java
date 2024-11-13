package org.example.springbootserver.trade.repository;

import org.example.springbootserver.trade.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BidRepository extends JpaRepository<BidEntity, Long> {
    BidEntity findByAddressNFTCollectionAndNftIdAndNftBidId(String addressNFTCollection, Long nftId, Long nftBidId);
    List<BidEntity> findAllByNftIdIn(Set<Long> nftIds);
    List<BidEntity> findAllByBidder(String bidder);
}
