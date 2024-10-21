package org.example.springbootserver.onchain.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class SignatureDTO {
    private String networkV;
    private String r;
    private String s;
    private Integer v;

    public static SignatureDTO from(Map<String, Object> signatureMap) {
        return SignatureDTO.builder()
                .networkV((String) signatureMap.get("networkV"))
                .r((String) signatureMap.get("r"))
                .s((String) signatureMap.get("s"))
                .v(signatureMap.get("v") != null ? Integer.parseInt(signatureMap.get("v").toString()) : null)
                .build();
    }
}