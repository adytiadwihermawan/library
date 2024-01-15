package com.miniproject.library.controller;

import com.miniproject.library.dto.librarian.LibrarianRequest;
import com.miniproject.library.dto.librarian.LibrarianResponse;
import com.miniproject.library.entity.Librarian;
import com.miniproject.library.service.LibrarianService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Librarian")
@RequestMapping("/librarian")
public class LibrarianController {
    private final LibrarianService librarianService;

    @PutMapping("/edit-{id}")
    public ResponseEntity<LibrarianResponse> updateLibrarian(@PathVariable Integer id, @Valid
    @RequestBody LibrarianRequest request){
        LibrarianResponse librarianResponse = librarianService.updateLibrarian(request, id);
        return ResponseEntity.ok(librarianResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Librarian>> getAllLibrarian(){
        List<Librarian> librarians = librarianService.getAllLibrarian();
        return ResponseEntity.ok(librarians);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Librarian> getLibrarianById(@PathVariable Integer id){
        Librarian librarian = librarianService.getLibrarianById(id);
        return ResponseEntity.ok(librarian);
    }

    @DeleteMapping("/delete-{id}")
    public ResponseEntity<Void> deleteLibrarianById(@PathVariable Integer id){
        librarianService.deleteLibrarianById(id);
        return ResponseEntity.noContent().build();
    }
}
