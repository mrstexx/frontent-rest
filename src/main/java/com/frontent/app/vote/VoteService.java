package com.frontent.app.vote;

import com.frontent.app.auth.AuthService;
import com.frontent.app.post.Post;
import com.frontent.app.post.PostRepository;
import com.frontent.app.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new IllegalStateException("Post not found with ID: " + voteDto.getPostId()));
        User user = authService.getCurrentUser();
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new IllegalStateException("You have already " + voteDto.getVoteType() + "d for this post");
        }
        if (VoteType.UP_VOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(this.mapToVote(voteDto, post, user));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post, User currentUser) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(currentUser)
                .build();
    }
}
