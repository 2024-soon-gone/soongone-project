package org.example.springbootserver.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.user.dto.UserDTO;

@Builder
@Getter
public class PostRequestDTO {
    private String text;
    private String location;
}
