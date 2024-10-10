package org.example.springbootserver.post.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostWithImgDTO {
    PostDTO postDTO;
    private String nftImgIpfsUri;
}
