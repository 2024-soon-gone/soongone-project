package org.example.springbootserver.onchain.constant;

import com.google.api.client.util.Value;

public class NftConstant {
    public static final String IPFS_FETCH_SUFFIX = "https://scarlet-unchanged-bobolink-538.mypinata.cloud/ipfs";

//    public static final String BC_SERVER_URL = "http://localhost:3000";
    @Value("${spring.baseUrl.BC_SERVER_URL}")
    public static String BC_SERVER_URL;
}


