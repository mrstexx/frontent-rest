package com.frontent.app.community;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional
    public CommunityDto saveCommunity(CommunityDto communityDto) {
        Community community = communityRepository.save(mapCommunityDto(communityDto));
        communityDto.setId(community.getId());
        return communityDto;
    }

    @Transactional(readOnly = true)
    public List<CommunityDto> getAllCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommunityDto getCommunityById(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No community found with the id: " + id));
        return mapToDto(community);
    }

    private CommunityDto mapToDto(Community community) {
        return CommunityDto.builder().name(community.getName())
                .id(community.getId())
                .description(community.getDescription())
                .numberOfPosts(community.getPosts().size())
                .build();
    }

    private Community mapCommunityDto(CommunityDto communityDto) {
        return Community.builder().name(communityDto.getName())
                .description(communityDto.getDescription())
                .build();
    }
}
