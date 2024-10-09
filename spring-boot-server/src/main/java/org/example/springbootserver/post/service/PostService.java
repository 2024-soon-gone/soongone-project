package org.example.springbootserver.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.post.dto.PostDTO;
import org.example.springbootserver.post.dto.PostRequestDTO;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.repository.PostRepository;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Create a new Post
    public PostDTO createPost(PostRequestDTO postRequestDTO) {
        String sampleNftAddress = "SAMPLE_NFT_ADDRESS";
        Long sampleNftId = -1L;
        Long sampleGenUserId = 1L; // 추후에 Post의 genUserId와 ownerUserId를 Wallet Address로 검토하는 것을 고려해야..
        Long sampleOwnerUserId = 1L;
        Long initLikes = 0L;
        Long initComments = 0L;

        UserEntity sampleGenUser = userRepository.findById(sampleGenUserId)
                .orElseThrow(() -> new UserNotFoundException(sampleGenUserId));

        UserEntity sampleOwnerUser = userRepository.findById(sampleOwnerUserId)
                .orElseThrow(() -> new UserNotFoundException(sampleOwnerUserId));

        PostEntity postEntity = PostEntity.builder()
                .nftAddress(sampleNftAddress)
                .nftId(sampleNftId)
                .text(postRequestDTO.getText())
                .genUserId(sampleGenUser)
                .location(postRequestDTO.getLocation())
                .likes(initLikes)
                .commentCounts(initComments)
                .ownerUserId(sampleOwnerUser)
                .build();

        PostEntity post = postRepository.save(postEntity);
        return PostDTO.from(post);
    }

//    @Transactional
//    public void postImage(MultipartFile image) throws IOException {
//        String new
//    }


//    @Transactional
//    public void postImage(MultipartFile image) throws IOException {
//        // Get Current User
//        String newImagePath =
//                image == null ? profileImageGenerator.getDefaultProfileImagePath() : updatePostImage(image);
//        String newImagePath = updatePostImage(image);
//
//    }

//    private String updatePostImage(MultipartFile image) throws IOException {
//        return s3Service.uploadImageFile(image, PROFILE_IMAGE_BUCKET_NAME);
//    }

    // Read all Posts
    public List<PostDTO> getAllPosts() {
        List<PostEntity> posts = postRepository.findAll();
        return posts.stream().map(PostDTO::from).toList();
    }

    // Read a Post by ID
    public PostDTO getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return PostDTO.from(post);
    }


    // Update a Post
    public PostDTO updatePost(Long id, PostRequestDTO updatedPostDTO) {
        PostEntity updatedPostEntity = postRepository.findById(id)
                .map(post -> {
                    post.setText(updatedPostDTO.getText());
                    post.setLocation(updatedPostDTO.getLocation());
                    // Add additional fields here if required
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        return PostDTO.from(updatedPostEntity);
    }


    // Delete a Post by ID
    public String deletePost(Long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        postRepository.deleteById(id);
        return "Post deleted successfully";
    }

}
