package org.example.springbootserver.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.user.dto.UserDTO;

@Builder
@Getter
public class PostDTO {
    private Long id;
    private String nftAddress;
    private Long nftId;
    private String text;
    private UserDTO genUser; // UserDTO로 변경
    private String location;
    private Long likes;
    private Long comments;
    private UserDTO ownerUser; // UserDTO로 변경

    public static PostDTO from(PostEntity postEntity) {
        return PostDTO.builder()
                .id(postEntity.getId())
                .nftAddress(postEntity.getNftAddress())
                .nftId(postEntity.getNftId())
                .text(postEntity.getText())
                .genUser(UserDTO.from(postEntity.getGenUserEntity())) // UserDTO로 변환
                .location(postEntity.getLocation())
                .likes(postEntity.getLikes())
                .comments(postEntity.getCommentCounts())
                .ownerUser(UserDTO.from(postEntity.getOwnerUserId())) // UserDTO로 변환
                .build();
    }
}
