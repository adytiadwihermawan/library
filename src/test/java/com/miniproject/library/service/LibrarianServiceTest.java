package com.miniproject.library.service;

import com.miniproject.library.dto.librarian.LibrarianRequest;
import com.miniproject.library.dto.librarian.LibrarianResponse;
import com.miniproject.library.entity.Librarian;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LibrarianRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class LibrarianServiceTest {
    private LibrarianRepository librarianRepository;
    private LibrarianService librarianService;


    @BeforeEach
    void setUp() {
        librarianRepository = mock(LibrarianRepository.class);
        librarianService = new LibrarianService(librarianRepository);
    }


    @Test
    void TestUpdateLibrarian(){
        Integer librarianId = 1;
        LibrarianRequest request = new LibrarianRequest();
        request.setNip(1234542L);
        request.setName("Arsyal");
        request.setEmail("arsyal@gmail.com");
        request.setPhone("1256756555");
        request.setAddress("Bekasi Selatan");
        request.setGender("Male");

        Librarian existingLibrarian = new Librarian();
        existingLibrarian.setId(librarianId);

        when(librarianRepository.findById(anyInt())).thenReturn(Optional.of(existingLibrarian));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(new Librarian());

        LibrarianResponse response = librarianService.updateLibrarian(request, librarianId);

        assertNotNull(response);
        assertEquals(1234542L, response.getNip());
        assertEquals("Arsyal", response.getName());
        assertEquals("arsyal@gmail.com", response.getEmail());
        assertEquals("1256756555", response.getPhone());
        assertEquals("Bekasi Selatan", response.getAddress());
        assertEquals("Male", response.getGender());
    }

    @Test
    void testUpdateLibrarianNotFound() {
        // Arrange
        Integer librarianId = 1;
        LibrarianRequest request = new LibrarianRequest();
        request.setNip(1234542L);
        request.setName("Fanny");
        request.setEmail("fanny@gmail.com");
        request.setPhone("125675651235");
        request.setAddress("Tambun");
        request.setGender("Male");

        when(librarianRepository.findById(librarianId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> librarianService.updateLibrarian(request, librarianId));

        assertEquals("Id Librarian Not Found", exception.getMessage());
    }

    @Test
    void testGetAllLibrarian() {
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

        when(librarianRepository.findAll()).thenReturn(librarians);

        List<Librarian> librarianList = librarianService.getAllLibrarian();


        Librarian firstLibrarian = librarianList.get(0);
        assertNotNull(firstLibrarian.getId());
        assertEquals(12345L, firstLibrarian.getNip());
        assertEquals("Arsyal", firstLibrarian.getName());
        assertEquals("arsyal@gmail.com", firstLibrarian.getEmail());
        assertEquals("1256756555", firstLibrarian.getPhone());
        assertEquals("Bekasi Selatan", firstLibrarian.getAddress());
        assertEquals("Male", firstLibrarian.getGender());
        verify(librarianRepository, times(1)).findAll();
    }

    @Test
    void testGetLibrarianById() {
        Integer id = 1;

        Librarian sampleLibrarian = new Librarian();
        sampleLibrarian.setId(1);
        sampleLibrarian.setNip(12345L);
        sampleLibrarian.setName("Arsyal");
        sampleLibrarian.setEmail("arsyal@gmail.com");
        sampleLibrarian.setPhone("1256756555");
        sampleLibrarian.setAddress("Bekasi Selatan");
        sampleLibrarian.setGender("Male");

        when(librarianRepository.findById(id)).thenReturn(Optional.of(sampleLibrarian));

        Librarian librarian = librarianService.getLibrarianById(id);

        assertNotNull(librarian);

        assertEquals(id, librarian.getId());
        assertEquals(12345L, librarian.getNip());
        assertEquals("Arsyal", librarian.getName());
        assertEquals("arsyal@gmail.com", librarian.getEmail());
        assertEquals("1256756555", librarian.getPhone());
        assertEquals("Bekasi Selatan", librarian.getAddress());
        assertEquals("Male", librarian.getGender());

        verify(librarianRepository, times(1)).findById(id);
    }

    @Test
    void testGetLibrarianByIdNotFound() {
        // Arrange
        Integer librarianId = 1;

        when(librarianRepository.findById(librarianId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> librarianService.getLibrarianById(librarianId));

        assertEquals("Id Librarian Not Found", exception.getMessage());
    }

    @Test
    void testDeleteLibrarianById() {
        Integer librarianId = 1;

        assertDoesNotThrow(() -> librarianService.deleteLibrarianById(librarianId),
                "Unexpected exception during delete operation");
    }
}
