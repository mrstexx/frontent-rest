package com.frontent.app.comment;

import com.frontent.app.auth.AuthService;
import com.frontent.app.post.Post;
import com.frontent.app.post.PostRepository;
import com.frontent.app.user.User;
import com.frontent.app.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final AuthService authService;

    public void createComment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new IllegalStateException("No post found with id: " + commentDto.getPostId()));
        Comment comment = mapComment(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        // TODO: send comment notification
    }

    public List<CommentDto> getAllCommentsByPostId(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("No post found with id: " + id));
        return commentRepository.findByPost(post).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("No user found with username: " + username));
        return commentRepository.findAllByUser(user).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private Comment mapComment(CommentDto commentDto, Post post, User user) {
        return Comment.builder()
                .id(commentDto.getId())
                .post(post)
                .text(commentDto.getText())
                .user(user)
                .createdAt(commentDto.getCreatedAt())
                .build();
    }

    private CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .postId(comment.getPost().getId())
                .username(comment.getUser().getUsername())
                .build();
    }
}
