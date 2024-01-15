package com.miniproject.library.service;

import com.miniproject.library.dto.librarian.LibrarianRequest;
import com.miniproject.library.dto.librarian.LibrarianResponse;
import com.miniproject.library.entity.Librarian;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LibrarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibrarianService {
    private final LibrarianRepository librarianRepository;
    private static final String ID_LIBRARIAN_NOT_FOUND = "Id Librarian Not Found";

    public LibrarianResponse updateLibrarian(LibrarianRequest request, Integer id){
        Optional<Librarian> optionalLibrarian = librarianRepository.findById(id);
        if (optionalLibrarian.isPresent()){
            Librarian librarian = optionalLibrarian.get();
            librarian.setId(id);
            librarian.setNip(request.getNip());
            librarian.setName(request.getName());
            librarian.setEmail(request.getEmail());
            librarian.setPhone(request.getPhone());
            librarian.setAddress(request.getAddress());
            librarian.setGender(request.getGender());
            librarianRepository.save(librarian);

            return LibrarianResponse.builder()
                    .id(librarian.getId())
                    .nip(librarian.getNip())
                    .name(librarian.getName())
                    .email(librarian.getEmail())
                    .phone(librarian.getPhone())
                    .address(librarian.getAddress())
                    .gender(librarian.getGender())
                    .build();
        }
        throw new ResourceNotFoundException(ID_LIBRARIAN_NOT_FOUND);
    }

    public List<Librarian> getAllLibrarian(){
        return librarianRepository.findAll();
    }

    public Librarian getLibrarianById(Integer id){
        return librarianRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_LIBRARIAN_NOT_FOUND));
    }

    public void deleteLibrarianById(Integer id){
        librarianRepository.deleteById(id);
    }
}
