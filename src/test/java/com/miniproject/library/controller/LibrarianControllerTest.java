package com.miniproject.library.controller;

import com.miniproject.library.dto.librarian.LibrarianRequest;
import com.miniproject.library.dto.librarian.LibrarianResponse;
import com.miniproject.library.entity.Librarian;
import com.miniproject.library.service.LibrarianService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibrarianControllerTest {
    @InjectMocks
    LibrarianController librarianController;
    @Mock
    LibrarianService librarianService;
    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private List<Librarian> librarianList(){
        Librarian sampleLibrarian = new Librarian();
        sampleLibrarian.setId(1);
        sampleLibrarian.setNip(12345L);
        sampleLibrarian.setName("Arsyal");
        sampleLibrarian.setEmail("arsyal@gmail.com");
        sampleLibrarian.setPhone("1256756555");
        sampleLibrarian.setAddress("Bekasi Selatan");
        sampleLibrarian.setGender("Male");
        List<Librarian> librarians = new ArrayList<>();
        librarians.add(sampleLibrarian);
        return  librarians;
    }

    @Test
    void updateLibrarian() {
        LibrarianRequest request = new LibrarianRequest();
        request.setNip(1234542L);
        request.setName("Fanny");
        request.setEmail("fanny@gmail.com");
        request.setPhone("125675651235");
        request.setAddress("Tambun");
        request.setGender("Male");

        Librarian librarian = librarianList().get(0);
        librarian.setNip(request.getNip());
        librarian.setName(request.getName());
        librarian.setEmail(request.getEmail());
        librarian.setPhone(request.getPhone());
        librarian.setAddress(request.getAddress());
        librarian.setGender(request.getGender());

        LibrarianResponse response = mapper.map(librarian,LibrarianResponse.class);
        when(librarianService.updateLibrarian(request,1)).thenReturn(response);

        ResponseEntity<LibrarianResponse> responseEntity = librarianController.updateLibrarian(1, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getAllLibrarian() {
        when(librarianService.getAllLibrarian()).thenReturn(librarianList());
        ResponseEntity<List<Librarian>> responseEntity = librarianController.getAllLibrarian();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(librarianList(), responseEntity.getBody());
    }

    @Test
    void getLibrarianById() {
        when(librarianService.getLibrarianById(1)).thenReturn(librarianList().get(0));
        ResponseEntity<Librarian> responseEntity = librarianController.getLibrarianById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(librarianList().get(0), responseEntity.getBody());
    }

    @Test
    void deleteLibrarianById() {
        doNothing().when(librarianService).deleteLibrarianById(1);
        ResponseEntity<Void>responseEntity = librarianController.deleteLibrarianById(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(librarianService,times(1)).deleteLibrarianById(1);
    }
}