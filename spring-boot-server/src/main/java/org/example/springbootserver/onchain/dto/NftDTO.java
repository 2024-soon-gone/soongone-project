package org.example.springbootserver.onchain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NftDTO {
    private String accountAddress;
    private String name;
    private String description;
}
