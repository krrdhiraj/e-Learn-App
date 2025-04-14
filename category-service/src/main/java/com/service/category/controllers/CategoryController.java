package com.service.category.controllers;

import com.service.category.config.AppConstants;
import com.service.category.dtos.CategoryDto;
import com.service.category.dtos.CustomMessage;
import com.service.category.dtos.CustomPageResponse;
import com.service.category.entities.Category;
import com.service.category.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper){
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }
    // get all category
    @GetMapping
    public List<Category> getAllCategory(){
        List<CategoryDto> categoryDtoList = categoryService.getAll();
        return categoryDtoList.stream().map(categoryDto ->
                modelMapper.map(categoryDto, Category.class)).toList();
    }
    // get PageNum & pageSize of Category
    @GetMapping("/pages")
    public CustomPageResponse<CategoryDto> getAllPages(
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", required = false,defaultValue = AppConstants.DEFAULT_PAGE_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", required = false,defaultValue = AppConstants.DEFAULT_PAGE_SORT_DIR) String sortDir
    ){
        return categoryService.getAllPages(pageNumber, pageSize, sortBy,sortDir);
    }

    // create Category
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto){
        CategoryDto createdCatDto = categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCatDto);
    }
    // get single category
    @GetMapping("/{categoryId}")
    public CategoryDto getSingleCategory(
            @PathVariable String categoryId){
        return categoryService.get(categoryId);
    }

    // delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CustomMessage> delete(
            @PathVariable String categoryId
    ){
        categoryService.delete(categoryId);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Category Deleted !");
        customMessage.setSuccess(true);
        return ResponseEntity.status(HttpStatus.OK).body(customMessage);
    }
    // update Category
    @PutMapping("/{categoryId}")
    public CategoryDto update(
            @PathVariable String categoryId,
            @RequestBody CategoryDto categoryDto
    ){
        return categoryService.update(categoryDto, categoryId);
    }

    @GetMapping("/search")
    public List<CategoryDto> searchCategory(
            @RequestParam("q") String q
    ){
        if(q.isBlank()){
            return new ArrayList<>();
        }
        return categoryService.search(q);
    }
}

