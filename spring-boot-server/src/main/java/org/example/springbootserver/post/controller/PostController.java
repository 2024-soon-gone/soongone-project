package org.example.springbootserver.post.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.global.dto.BaseResponse;
import org.example.springbootserver.onchain.dto.NftMintResponseDTO;
import org.example.springbootserver.post.dto.PostRequestDTO;
import org.example.springbootserver.post.dto.PostResponseDTO;
import org.example.springbootserver.post.dto.PostWithImgDTO;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private static final String POST_CREATED_SUCCESS_MESSAGE = "Post without Img Created Successfully";
    private static final String POST_IMG_CREATED_SUCCESS_MESSAGE = "Post Img Created Successfully";
    private static final String POST_UPDATED_SUCCESS_MESSAGE = "Post Updated Successfully";
    private static final String POST_DELETED_SUCCESS_MESSAGE = "Post Deleted Successfully";

    private final PostService postService;

    // Create a new Post
    @PostMapping("/createPost")
    public ResponseEntity<BaseResponse> createPost(@RequestBody PostRequestDTO postRequestDTO) {
        PostEntity postCreated = postService.createPost(postRequestDTO);
        return ResponseEntity.ok(BaseResponse.builder().message(POST_CREATED_SUCCESS_MESSAGE).build());
    }

    // Mint Img as NFT
    @PostMapping("/postImage")
    public ResponseEntity<BaseResponse> postImage(@RequestPart("image") MultipartFile file) throws IOException {
        NftMintResponseDTO nftMintResponse = postService.postImage(file);
        return ResponseEntity.ok(BaseResponse.builder().message(POST_IMG_CREATED_SUCCESS_MESSAGE).build());
    }

    // Get posts owned by a specific user
    @GetMapping("/userOwned")
    public ResponseEntity<PostResponseDTO> getUserOwnedPosts() {
        List<PostWithImgDTO> userOwnedPosts = postService.getUserOwnedPosts();
        return ResponseEntity.ok(PostResponseDTO.builder().posts(userOwnedPosts).build());
    }

    // Get all Posts
    @GetMapping
    public ResponseEntity<PostResponseDTO> getAllPosts() {
        List<PostWithImgDTO> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(PostResponseDTO.builder().posts(allPosts).build());
    }

    // Get a Post by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostWithImgDTO postWithImgFound = postService.getPostById(id);
        return ResponseEntity.ok(PostResponseDTO.builder()
                .posts(List.of(postWithImgFound)) // Wrap in a list
                .build());
    }

    // Update a Post
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePost (
            @PathVariable Long id,
            @RequestBody PostRequestDTO updatedPostRequest) {
        postService.updatePost(id, updatedPostRequest);
        return ResponseEntity.ok(BaseResponse.builder().message(POST_UPDATED_SUCCESS_MESSAGE).build());
    }

    // Delete a Post
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(BaseResponse.builder().message(POST_DELETED_SUCCESS_MESSAGE).build());
    }
}
