package com.service.course.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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