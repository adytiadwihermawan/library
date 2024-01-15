package com.miniproject.library.service;

import com.miniproject.library.dto.anggota.AnggotaRequest;
import com.miniproject.library.dto.anggota.AnggotaResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.AnggotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnggotaService {
    private final AnggotaRepository anggotaRepository;
    private static final String ID_ANGGOTA_NOT_FOUND = "Id Anggota Not Found";

    public AnggotaResponse updateAnggota(AnggotaRequest request, Integer id){
        Optional<Anggota> optionalAnggota = anggotaRepository.findById(id);
        if (optionalAnggota.isPresent()){
            Anggota anggota = optionalAnggota.get();
            anggota.setId(id);
            anggota.setNik(request.getNik());
            anggota.setName(request.getName());
            anggota.setEmail(request.getEmail());
            anggota.setPhone(request.getPhone());
            anggota.setAddress(request.getAddress());
            anggota.setGender(request.getGender());
            anggotaRepository.save(anggota);

            return AnggotaResponse.builder()
                    .id(anggota.getId())
                    .nik(anggota.getNik())
                    .name(anggota.getName())
                    .email(anggota.getEmail())
                    .phone(anggota.getPhone())
                    .address(anggota.getAddress())
                    .gender(anggota.getGender())
                    .build();
        }
        throw new ResourceNotFoundException(ID_ANGGOTA_NOT_FOUND);
    }

    public List<Anggota> getAllAnggota(){
        return anggotaRepository.findAll();
    }

    public Anggota getAnggotaById(Integer id){
         return anggotaRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException(ID_ANGGOTA_NOT_FOUND));
    }

    public void deleteAnggotaById(Integer id){
        anggotaRepository.deleteById(id);
    }
}
