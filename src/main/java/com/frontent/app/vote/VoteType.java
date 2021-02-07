package com.frontent.app.vote;

import java.util.Arrays;

public enum VoteType {
    UP_VOTE(1),
    DOWN_VOTE(-1);

    private final int direction;

    VoteType(int direction) {
        this.direction = direction;
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny().orElseThrow(() -> new IllegalStateException("Vote could not be found."));
    }

    public Integer getDirection() {
        return direction;
    }
}
