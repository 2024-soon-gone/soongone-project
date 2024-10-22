package org.example.springbootserver.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.springbootserver.onchain.dto.NftMintResponseDTO;
import org.example.springbootserver.onchain.service.NftService;
import org.example.springbootserver.post.dto.PostDTO;
import org.example.springbootserver.post.dto.PostRequestDTO;
import org.example.springbootserver.post.dto.PostWithImgDTO;
import org.example.springbootserver.post.entity.ImageEntity;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.repository.ImageRepository;
import org.example.springbootserver.post.repository.PostRepository;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.UserRepository;
import org.example.springbootserver.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final NftService nftService;
    private final UserService userService;

    @Value("${spring.blockchain-server.contract.NFT_CONTRACT_ADDRESS}")
    private String NFT_CONTRACT_ADDRESS;

    // Create a new Post
    public PostDTO createPost(PostRequestDTO postRequestDTO) {
        String sessionSocialUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();

//        String sampleNftAddress = "SAMPLE_NFT_ADDRESS";
        Long sampleNftId = -1L;
        Long initLikes = 0L;
        Long initComments = 0L;

        UserEntity currentUser = userService.getCurrentUserEntity();

        PostEntity postEntity = PostEntity.builder()
                .nftAddress(NFT_CONTRACT_ADDRESS)
                .nftId(sampleNftId)
                .text(postRequestDTO.getText())
                .genUserEntity(currentUser)
                .location(postRequestDTO.getLocation())
                .likes(initLikes)
                .commentCounts(initComments)
                .ownerUserId(currentUser)
                .build();

        PostEntity post = postRepository.save(postEntity);
        return PostDTO.from(post);
    }

    @Transactional
    public NftMintResponseDTO postImage(MultipartFile image) throws IOException {

        UserEntity currentUser = userService.getCurrentUserEntity();

        // Fetch the most recent post created by the user
        PostEntity latestPost = postRepository.findTopByGenUserEntityOrderByCreatedAtDesc(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("No posts found for user: " + currentUser.getId()));

        // Prepare details for NFT minting
        String accountAddress = "0x53eFa01771483b13D10618D3865791baf84Fe97b";  // Test account set to minting user
        String name = currentUser.getName();
        String description = latestPost.getText();  // Post's description/text

        // Call NFT service to mint an NFT
        String nftMintResponse = nftService.nftMintRequest(accountAddress, name, description, image);

        // Assuming the response is in JSON format, parse it
        ObjectMapper objectMapper = new ObjectMapper();
        NftMintResponseDTO nftMintResponseDTO = objectMapper.readValue(nftMintResponse, NftMintResponseDTO.class);

        Long nftId = nftMintResponseDTO.getNftId();

        // Update the latestPost's nftId attribute
        latestPost.setNftId(nftId); // Assuming the method exists in PostEntity
        postRepository.save(latestPost); // Save the updated post

        String nftImgIpfsUri = nftMintResponseDTO.getNftImgIpfsUri();
        ImageEntity newImage = new ImageEntity(nftImgIpfsUri, latestPost);
        imageRepository.save(newImage);

        return nftMintResponseDTO;
    }

    // Read all Posts
    public List<PostWithImgDTO> getAllPosts() {
        List<PostEntity> posts = postRepository.findAll();

        // Map each PostEntity to PostWithImgDTO
        return posts.stream().map(post -> {
            // Fetch the associated image for each post
            ImageEntity img = imageRepository.findByPostEntity_Id(post.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Image not found for PostEntity_Id: " + post.getId()));

            // Return PostWithImgDTO combining the post and its image URI
            return PostWithImgDTO.builder()
                    .postDTO(PostDTO.from(post))
                    .nftImgIpfsUri(img.getImgUrl())
                    .build();
        }).toList();
    }

    // Read a Post by ID
    public PostWithImgDTO getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        ImageEntity img = imageRepository.findByPostEntity_Id(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("Image not found with PostEntity_Id: " + post.getId()));

//        return PostDTO.from(post);
        return PostWithImgDTO.builder()
                .postDTO(PostDTO.from(post))
                .nftImgIpfsUri(img.getImgUrl())
                .build();
    }

    // Update a Post
    public PostDTO updatePost(Long id, PostRequestDTO updatedPostDTO) throws UserNotFoundException, IllegalArgumentException {

        UserEntity currentUser = userService.getCurrentUserEntity();
        PostEntity targetPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        if (!currentUser.getId().equals(targetPost.getGenUserEntity().getId())) {
            throw new UserNotFoundException("Current User is not the owner of the post: " + id);
        }

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
    public String deletePost(Long id) throws UserNotFoundException, IllegalArgumentException {
        UserEntity currentUser = userService.getCurrentUserEntity();
        PostEntity targetPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        if (!currentUser.getId().equals(targetPost.getGenUserEntity().getId())) {
            throw new UserNotFoundException("Current User is not the owner of the post: " + id);
        }

        postRepository.deleteById(id);
        return "Post deleted successfully";
    }



}
