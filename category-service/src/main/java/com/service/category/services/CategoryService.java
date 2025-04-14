package com.service.category.services;


import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CustomPageResponse;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    List<CategoryDto> getAll();

    CustomPageResponse<CategoryDto> getAllPages(int pageNumber, int pageSize, String sortBy,String sortDir);

    CategoryDto get(String categoryId);

    CategoryDto update(CategoryDto categoryDto, String categoryId);

    void delete(String categoryId);

    public void addCourseToCategory(String catId, String course);

    List<CategoryDto> search(String keyword);

}
