package com.frontent.app.post;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts/")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        postService.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-community/{id}")
    public ResponseEntity<List<PostResponse>> getPostsByCommunityId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByCommunityId(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }

}
