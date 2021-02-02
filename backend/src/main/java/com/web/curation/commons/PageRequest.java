package com.web.curation.commons;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class PageRequest {
    private int page;
    private int size;
    private Sort sort;

    public void setPage(int page) {
        this.page = page < 0 ? 0 : page;
    }

    public void setSize(int size) {
        final int MAX_SIZE = 500;
        this.size = size > MAX_SIZE ? MAX_SIZE : size;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public PageRequest(){};

    public org.springframework.data.domain.PageRequest of(){
        return org.springframework.data.domain.PageRequest.of(page, size, Sort.unsorted());
    }

    public org.springframework.data.domain.PageRequest of(Sort sort){
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }
}
