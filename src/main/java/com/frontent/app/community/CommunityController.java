package com.frontent.app.community;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityDto> createSubreddit(@RequestBody CommunityDto community) {
        return ResponseEntity.status(HttpStatus.CREATED).body(communityService.saveCommunity(community));
    }

    @GetMapping("/")
    public ResponseEntity<List<CommunityDto>> getAllCommunities() {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.getAllCommunities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityDto> getCommunityById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.getCommunityById(id));
    }

}
