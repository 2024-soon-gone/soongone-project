package org.example.springbootserver.onchain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TransactionResponseDTO {

    @JsonProperty("_type")
    private String _type;

    private List<Object> accessList;
    private Long blockNumber;
    private String blockHash;
    private String blobVersionedHashes;
    private String chainId;
    private String data;
    private String from;
    private String gasLimit;
    private String gasPrice;
    private String hash;
    private String maxFeePerGas;
    private String maxPriorityFeePerGas;
    private String maxFeePerBlobGas;
    private int nonce;
    private SignatureDTO signature;
    private String to;
    private int type;
    private String value;


    public static TransactionResponseDTO from(Map<String, Object> dataMap) {
        return TransactionResponseDTO.builder()
                ._type((String) dataMap.get("_type"))
                .accessList((List<Object>) dataMap.get("accessList"))
                .blockNumber(dataMap.get("blockNumber") != null ? Long.parseLong(dataMap.get("blockNumber").toString()) : null)
                .blockHash((String) dataMap.get("blockHash"))
                .blobVersionedHashes((String) dataMap.get("blobVersionedHashes"))
                .chainId((String) dataMap.get("chainId"))
                .data((String) dataMap.get("data"))
                .from((String) dataMap.get("from"))
                .gasLimit((String) dataMap.get("gasLimit"))
                .gasPrice((String) dataMap.get("gasPrice"))
                .hash((String) dataMap.get("hash"))
                .maxFeePerGas((String) dataMap.get("maxFeePerGas"))
                .maxPriorityFeePerGas((String) dataMap.get("maxPriorityFeePerGas"))
                .maxFeePerBlobGas((String) dataMap.get("maxFeePerBlobGas"))
                .nonce(dataMap.get("nonce") != null ? Integer.parseInt(dataMap.get("nonce").toString()) : null)
                .signature(SignatureDTO.from((Map<String, Object>) dataMap.get("signature")))
                .to((String) dataMap.get("to"))
                .type(dataMap.get("type") != null ? Integer.parseInt(dataMap.get("type").toString()) : null)
                .value((String) dataMap.get("value"))
                .build();
    }
}