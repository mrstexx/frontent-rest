package com.frontent.app.comment;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        commentService.createComment(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByPostId(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByUsername(username));
    }
}
