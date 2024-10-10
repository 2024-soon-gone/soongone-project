package org.example.springbootserver.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
                .genUserEntity(sampleGenUser)
                .location(postRequestDTO.getLocation())
                .likes(initLikes)
                .commentCounts(initComments)
                .ownerUserId(sampleOwnerUser)
                .build();

        PostEntity post = postRepository.save(postEntity);
        return PostDTO.from(post);
    }

    @Transactional
    public NftMintResponseDTO postImage(MultipartFile image) throws IOException {
        // Retrieve the current user - assuming you have a method to get the logged-in user's ID
        Long sampleUserId = 1L;  // Replace with actual logic for current user
        UserEntity currentUser = userRepository.findById(sampleUserId)
                .orElseThrow(() -> new UserNotFoundException(sampleUserId));

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
        String nftImgIpfsUri = nftMintResponseDTO.getNftImgIpfsUri();

        ImageEntity newImage = new ImageEntity(nftImgIpfsUri, latestPost);
        imageRepository.save(newImage);

        return nftMintResponseDTO;
    }

    // Read all Posts
    public List<PostDTO> getAllPosts() {
        List<PostEntity> posts = postRepository.findAll();
        return posts.stream().map(PostDTO::from).toList();
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
