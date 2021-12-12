package com.untilled.roadcapture.api.dto.base;

import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotEmpty;

public final class PageRequest {
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 50;

    private int page = 1;
    private int size = 10;
    private Sort.Direction direction = Sort.Direction.ASC;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }
    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createdAt");
    }
}
