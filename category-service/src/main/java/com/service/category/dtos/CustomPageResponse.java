package com.service.category.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageResponse<T> {

    private int pageSize;

    private int pageNumber;

    private long totalElements;

    private boolean isLast;

    private long totalPages;

    private List<T> content;
}