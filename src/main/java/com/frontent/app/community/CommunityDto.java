package com.frontent.app.community;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
