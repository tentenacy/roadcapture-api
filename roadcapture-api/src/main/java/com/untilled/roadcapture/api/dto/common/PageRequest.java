package com.untilled.roadcapture.api.dto.common;

import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
public class PageRequest {
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 50;

    private int page;
    private int size;
    private List<String> sort;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public org.springframework.data.domain.PageRequest of() {
        if (sort == null || sort.isEmpty()) {
            return org.springframework.data.domain.PageRequest.of(page - 1, size);
        }

        return org.springframework.data.domain.PageRequest.of(page - 1, size, Sort.by(getOrders(sort)));
    }

    private List<Sort.Order> getOrders(List<String> sort) {
        List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(str ->
                orders.add(
                        new Sort.Order(Sort.Direction.valueOf(str.split("#")[1].toUpperCase(Locale.ROOT)),
                                str.split("#")[0])
                )
        );
        return orders;
    }
}
