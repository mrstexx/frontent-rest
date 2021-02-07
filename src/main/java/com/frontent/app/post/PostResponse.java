package com.frontent.app.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String username;
    private String communityName;
    private String postName;
    private String url;
    private String description;
    private LocalDateTime createAt;

    private Integer voteCount;
    private Integer commentCount;
    // private String duration; // TODO: check library timeago
}
