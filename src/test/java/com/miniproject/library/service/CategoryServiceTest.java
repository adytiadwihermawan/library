package com.miniproject.library.service;

import com.miniproject.library.dto.category.CategoryRequest;
import com.miniproject.library.dto.category.CategoryResponse;
import com.miniproject.library.entity.Category;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Fiction");

        CategoryResponse response = categoryService.addCategory(request);

        assertNotNull(response);
        assertEquals("Fiction", response.getName());
    }

    @Test
    void testUpdateCategory() {
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest();
        request.setName("Science Fiction");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        CategoryResponse response = categoryService.updateCategory(request, categoryId);

        assertNotNull(response);
        assertEquals("Science Fiction", response.getName());
    }

    @Test
    void testUpdateCategoryNotFound() {
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest();
        request.setName("Slice of life");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(request, categoryId));

        assertEquals("Id Category Not Found", exception.getMessage());
    }

    @Test
    void testGetAllCategory() {
        // Asumsi jika memiliki beberapa data di repository
        Integer categoryId = 1;
        Category sampleCategory = new Category();
        sampleCategory.setId(categoryId);
        sampleCategory.setName("Fiction");
        List<Category> categories = new ArrayList<>();
        categories.add(sampleCategory);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> categoryList = categoryService.getAllCategory();

        assertFalse(categoryList.isEmpty());
        assertEquals(1, categoryList.size());
        assertEquals("Fiction", categoryList.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        Integer categoryId = 1;
        Category sampleCategory = new Category();
        sampleCategory.setId(categoryId);
        sampleCategory.setName("Fiction");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));

        Category category = categoryService.getCategoryById(categoryId);

        assertNotNull(category);
        assertEquals("Fiction", category.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));

        assertEquals("Id Category Not Found", exception.getMessage());
    }
}
