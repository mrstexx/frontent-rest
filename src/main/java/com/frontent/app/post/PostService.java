package com.frontent.app.post;

import com.frontent.app.auth.AuthService;
import com.frontent.app.comment.CommentRepository;
import com.frontent.app.community.Community;
import com.frontent.app.community.CommunityRepository;
import com.frontent.app.user.User;
import com.frontent.app.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final AuthService authService;

    public void savePost(PostRequest postRequest) {
        Community community = communityRepository.findByName(postRequest.getCommunityName())
                .orElseThrow(() -> new IllegalStateException("No community found by name: " + postRequest.getCommunityName()));
        User currentUser = authService.getCurrentUser();

        postRepository.save(mapPost(postRequest, community, currentUser));
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("No post found with id - " + id));
        return mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByCommunityId(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Community not fount with an ID - " + id));
        List<Post> posts = postRepository.findAllByCommunity(community);
        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new IllegalStateException("User not found with name - " + name));
        return postRepository.findByUser(user).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private Post mapPost(PostRequest postRequest, Community community, User user) {
        return Post.builder()
                .name(postRequest.getPostName())
                .description(postRequest.getDescription())
                .url(postRequest.getUrl())
                .createdAt(LocalDateTime.now())
                .community(community)
                .voteCount(0)
                .user(user)
                .build();
    }

    private PostResponse mapToDto(Post post) {
        if (post == null) {
            return null;
        }
        // TODO: getCommunity might be null
        // TODO: Transactional readOnly?
        return PostResponse.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .communityName(post.getCommunity().getName())
                .postName(post.getName())
                .url(post.getUrl())
                .description(post.getDescription())
                .createAt(post.getCreatedAt())
                .commentCount(commentRepository.findByPost(post).size())
                .build();
    }
}
