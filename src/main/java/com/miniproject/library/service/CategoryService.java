package com.miniproject.library.service;

import com.miniproject.library.dto.category.CategoryRequest;
import com.miniproject.library.dto.category.CategoryResponse;
import com.miniproject.library.entity.Category;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private static final String ID_CATEGORY_NOT_FOUND = "Id Category Not Found";

    public CategoryResponse addCategory(CategoryRequest request){
        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public CategoryResponse updateCategory(CategoryRequest request, Integer id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setId(id);
            category.setName(request.getName());
            categoryRepository.save(category);

            return CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }
        throw new ResourceNotFoundException(ID_CATEGORY_NOT_FOUND);
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id){
        return categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_CATEGORY_NOT_FOUND));
    }
}
