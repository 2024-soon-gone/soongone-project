package org.example.springbootserver.post.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.post.dto.PostDTO;
import org.example.springbootserver.post.dto.PostRequestDTO;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Create a new Post
    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostRequestDTO postRequestDTO) {
        PostDTO createdPostDTO = postService.createPost(postRequestDTO);
        return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
    }

//    @PostMapping("/image")
//    public void postImage(@RequestParam("image") MultipartFile file) throws IOException {
//
//    }

    // 일단 아래는 PostEntity -> PostDTO 아직 안함

    // Get all Posts
    @GetMapping
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get a Post by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostEntity> getPostById(@PathVariable Long id) {
        Optional<PostEntity> postEntity = postService.getPostById(id);
        return postEntity
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a Post
    @PutMapping("/{id}")
    public ResponseEntity<PostEntity> updatePost(
            @PathVariable Long id,
            @RequestBody PostEntity updatedPost) {
        try {
            PostEntity post = postService.updatePost(id, updatedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a Post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
