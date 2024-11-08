package org.example.springbootserver.post.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostResponseDTO {
    List<PostWithImgDTO> posts;
}
