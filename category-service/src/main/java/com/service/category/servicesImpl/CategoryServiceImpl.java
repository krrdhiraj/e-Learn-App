package com.service.category.servicesImpl;

import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CustomPageResponse;
import com.service.category.entities.Category;
import com.service.category.exceptions.ResourceNotFoundException;
import com.service.category.repositories.CategoryRepo;
import com.service.category.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepo categoryRepo) {
        this.modelMapper = modelMapper;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        String catId = UUID.randomUUID().toString();
        categoryDto.setId(catId);
        categoryDto.setAddedDate(new Date());
        Category savedCategory = categoryRepo.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepo.findAll().stream().map(category ->
                modelMapper.map(category, CategoryDto.class)).toList();
    }

    @Override
    public CustomPageResponse<CategoryDto> getAllPages(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sortedBy = Sort.by(sortBy);
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Category> categoryPages = categoryRepo.findAll(pageRequest);
        List<Category> categoryList = categoryPages.getContent();
        List<CategoryDto> categoryDtoList = categoryList.stream().map(category ->
                modelMapper.map(category, CategoryDto.class)).toList();

        CustomPageResponse<CategoryDto> customPageResponse = new CustomPageResponse<>();

        customPageResponse.setTotalPages(categoryPages.getTotalPages());
        customPageResponse.setLast(categoryPages.isLast());
        customPageResponse.setContent(categoryDtoList);
        customPageResponse.setPageNumber(pageNumber);
        customPageResponse.setPageSize(categoryPages.getSize());
        customPageResponse.setTotalElements(categoryPages.getTotalElements());

        return customPageResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category not found!!"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found , Pls enter the valid category Id"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setBannerImageUrl(categoryDto.getBannerImageUrl());
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category not found!!"));
        categoryRepo.delete(category);
    }

    @Override
    public void addCourseToCategory(String catId, String course) {

    }

    @Override
    public List<CategoryDto> search(String keyword) {
        return this.categoryRepo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword,keyword)
                .stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }
}
