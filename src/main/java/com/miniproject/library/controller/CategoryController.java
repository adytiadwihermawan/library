package com.miniproject.library.controller;

import com.miniproject.library.dto.category.CategoryRequest;
import com.miniproject.library.dto.category.CategoryResponse;
import com.miniproject.library.entity.Category;
import com.miniproject.library.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Category")
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest request){
        CategoryResponse categoryResponse = categoryService.addCategory(request);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/edit-{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id, @Valid
                                                            @RequestBody CategoryRequest request){
        CategoryResponse categoryResponse = categoryService.updateCategory(request, id);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}
