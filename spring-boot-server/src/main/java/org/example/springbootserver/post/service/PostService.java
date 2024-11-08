package org.example.springbootserver.post.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.onchain.dto.NftMintResponseDTO;
import org.example.springbootserver.onchain.service.NftService;
import org.example.springbootserver.post.dto.PostDTO;
import org.example.springbootserver.post.dto.PostRequestDTO;
import org.example.springbootserver.post.dto.PostWithImgDTO;
import org.example.springbootserver.post.entity.ImageEntity;
import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.post.exception.ImgNotFoundException;
import org.example.springbootserver.post.exception.PostMintFailedException;
import org.example.springbootserver.post.exception.PostNotFoundException;
import org.example.springbootserver.post.exception.PostNotOwnedException;
import org.example.springbootserver.post.repository.ImageRepository;
import org.example.springbootserver.post.repository.PostRepository;
import org.example.springbootserver.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final NftService nftService;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.blockchain-server.contract.NFT_CONTRACT_ADDRESS}")
    private String NFT_CONTRACT_ADDRESS;

    // Create a new Post
    public PostEntity createPost(PostRequestDTO postRequestDTO) {
        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();

        Long initNFTId = -1L;
        Long initLikes = 0L;
        Long initComments = 0L;

        PostEntity postEntity = PostEntity.builder()
                .nftAddress(NFT_CONTRACT_ADDRESS)
                .nftId(initNFTId)
                .text(postRequestDTO.getText())
                .genUserEntity(currentUser)
                .location(postRequestDTO.getLocation())
                .likes(initLikes)
                .commentCounts(initComments)
                .ownerUserId(currentUser)
                .build();
        return postRepository.save(postEntity);
    }

    @Transactional
    public NftMintResponseDTO postImage(MultipartFile image) throws PostMintFailedException, JsonMappingException, IOException {

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();

        // Fetch the most recent post created by the user
        PostEntity latestPost = postRepository.findTopByGenUserEntityOrderByCreatedAtDesc(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("No posts found for user: " + currentUser.getId()));

        // Prepare details for NFT minting
        String accountAddress = "0x53eFa01771483b13D10618D3865791baf84Fe97b";  // Test account set to minting user
        String name = currentUser.getName();
        String description = latestPost.getText();  // Post's description/text

        // Call NFT service to mint an NFT
        String nftMintResponse = nftService.nftMintRequest(accountAddress, name, description, image);

        ObjectMapper objectMapper = new ObjectMapper();
        NftMintResponseDTO nftMintResponseDTO = objectMapper.readValue(nftMintResponse, NftMintResponseDTO.class);

        Long nftId = nftMintResponseDTO.getNftId();

        latestPost.setNftId(nftId);
        postRepository.save(latestPost);

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
                    .orElseThrow(() -> new ImgNotFoundException("Image not found for PostEntity_Id: " + post.getId()));

            // Return PostWithImgDTO combining the post and its image URI
            return PostWithImgDTO.builder()
                    .postDTO(PostDTO.from(post))
                    .nftImgIpfsUri(img.getImgUrl())
                    .build();
        }).toList();
    }

    public List<PostWithImgDTO> getUserOwnedPosts() {
        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        List<PostEntity> posts = postRepository.findAll(); // 모든 게시물 가져오기

        return posts.stream()
                .filter(post -> post.getOwnerUserId().getId().equals(currentUser.getId()))
                .map(post -> {
                    // 게시물에 대한 이미지를 가져오기
                    ImageEntity img = imageRepository.findByPostEntity_Id(post.getId())
                            .orElseThrow(() -> new ImgNotFoundException("Image not found for PostEntity_Id: " + post.getId()));

                    // PostWithImgDTO 반환
                    return PostWithImgDTO.builder()
                            .postDTO(PostDTO.from(post)) // PostEntity를 PostDTO로 변환
                            .nftImgIpfsUri(img.getImgUrl()) // 이미지 URL 설정
                            .build();
                })
                .collect(Collectors.toList()); // 리스트로 수집하여 반환
    }

    public List<PostWithImgDTO> getUserOwnedPosts(Long userId) {
        List<PostEntity> posts = postRepository.findAll(); // 모든 게시물 가져오기

        return posts.stream()
                .filter(post -> post.getOwnerUserId().getId().equals(userId))
                .map(post -> {
                    // 게시물에 대한 이미지를 가져오기
                    ImageEntity img = imageRepository.findByPostEntity_Id(post.getId())
                            .orElseThrow(() -> new ImgNotFoundException("Image not found for PostEntity_Id: " + post.getId()));

                    // PostWithImgDTO 반환
                    return PostWithImgDTO.builder()
                            .postDTO(PostDTO.from(post)) // PostEntity를 PostDTO로 변환
                            .nftImgIpfsUri(img.getImgUrl()) // 이미지 URL 설정
                            .build();
                })
                .collect(Collectors.toList()); // 리스트로 수집하여 반환
    }

    // Read a Post by ID
    public PostWithImgDTO getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        ImageEntity img = imageRepository.findByPostEntity_Id(post.getId())
                .orElseThrow(() -> new ImgNotFoundException("Image not found with PostEntity_Id: " + post.getId()));

        return PostWithImgDTO.builder()
                .postDTO(PostDTO.from(post))
                .nftImgIpfsUri(img.getImgUrl())
                .build();
    }

    // Update a Post
    public void updatePost(Long id, PostRequestDTO updatedPostDTO) {

        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        PostEntity targetPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        if (!currentUser.getId().equals(targetPost.getGenUserEntity().getId())) {
            throw new PostNotOwnedException("Current User is not the owner of the post: " + id);
        }

        postRepository.findById(id)
                .map(post -> {
                    post.setText(updatedPostDTO.getText());
                    post.setLocation(updatedPostDTO.getLocation());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
    }

    // Delete a Post by ID
    public void deletePost(Long id) {
        UserEntity currentUser = userDetailsService.getUserEntityByContextHolder();
        PostEntity targetPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        if (!currentUser.getId().equals(targetPost.getGenUserEntity().getId())) {
            throw new PostNotOwnedException("Current User is not the owner of the post: " + id);
        }

        postRepository.deleteById(id);
    }

}
