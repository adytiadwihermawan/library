package com.miniproject.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.miniproject.library.dto.anggota.AnggotaRequest;
import com.miniproject.library.dto.anggota.AnggotaResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.AnggotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

class AnggotaServiceTest {

    private AnggotaRepository anggotaRepository;
    private AnggotaService anggotaService;

    @BeforeEach
    void setUp() {
        anggotaRepository = mock(AnggotaRepository.class);
        anggotaService = new AnggotaService(anggotaRepository);
    }


    @Test
    void testUpdateAnggota() {
        Integer anggotaId = 1;
        AnggotaRequest request = new AnggotaRequest();
        request.setNik(132842L);
        request.setName("Adytia Dwi Hermawan");
        request.setEmail("Adytia@gmail.com");
        request.setPhone("08514565");
        request.setAddress("Kalimantan");
        request.setGender("male");

        Anggota existingAnggota = new Anggota();
        existingAnggota.setId(anggotaId);

        when(anggotaRepository.findById(anggotaId)).thenReturn(Optional.of(existingAnggota));
        when(anggotaRepository.save(any(Anggota.class))).thenReturn(new Anggota());

        AnggotaResponse response = anggotaService.updateAnggota(request, anggotaId);

        assertNotNull(response);
        assertEquals(anggotaId, response.getId());
        assertEquals(132842L, response.getNik());
        assertEquals("Adytia Dwi Hermawan", response.getName());
        assertEquals("Adytia@gmail.com", response.getEmail());
        assertEquals("08514565", response.getPhone());
        assertEquals("Kalimantan", response.getAddress());
        assertEquals("male", response.getGender());
    }

    @Test
    void testUpdateAnggotaNotFound() {
        Integer id = 1;
        AnggotaRequest request = new AnggotaRequest();
        request.setNik(1234567890L);
        request.setName("qowi");
        request.setEmail("qowi@gmail.com");
        request.setPhone("987-654-3210");
        request.setAddress("456 Updated St");
        request.setGender("male");

        when(anggotaRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> anggotaService.updateAnggota(request, id));

        assertEquals("Id Anggota Not Found", exception.getMessage());
    }

    @Test
    void testGetAllAnggota() {
        // Asumsi jika memiliki beberapa data di repository
        Anggota sampleAnggota = new Anggota();
        sampleAnggota.setId(1);
        sampleAnggota.setNik(132842L);
        sampleAnggota.setName("Adytia Dwi Hermawan");
        sampleAnggota.setEmail("Adytia@gmail.com");
        sampleAnggota.setPhone("08514565");
        sampleAnggota.setAddress("Kalimantan");
        sampleAnggota.setGender("Male");

        when(anggotaRepository.findAll()).thenReturn(List.of(sampleAnggota));

        List<Anggota> anggotaList = anggotaService.getAllAnggota();

        assertFalse(anggotaList.isEmpty());

        assertEquals(1, anggotaList.size());

        Anggota firstAnggota = anggotaList.get(0);
        assertNotNull(firstAnggota.getId());
        assertEquals(132842L, firstAnggota.getNik());
        assertEquals("Adytia Dwi Hermawan", firstAnggota.getName());
        assertEquals("Adytia@gmail.com", firstAnggota.getEmail());
        assertEquals("08514565", firstAnggota.getPhone());
        assertEquals("Kalimantan", firstAnggota.getAddress());
        assertEquals("Male", firstAnggota.getGender());
        verify(anggotaRepository, times(1)).findAll();
    }

    @Test
    void testGetAnggotaById() {
        Integer id = 1;

        Anggota sampleAnggota = new Anggota();
        sampleAnggota.setId(1);
        sampleAnggota.setNik(132842L);
        sampleAnggota.setName("Adytia Dwi Hermawan");
        sampleAnggota.setEmail("Adytia@gmail.com");
        sampleAnggota.setPhone("08514565");
        sampleAnggota.setAddress("Kalimantan");
        sampleAnggota.setGender("Male");

        when(anggotaRepository.findById(id)).thenReturn(Optional.of(sampleAnggota));

        Anggota anggota = anggotaService.getAnggotaById(id);

        assertNotNull(anggota);

        assertEquals(id, anggota.getId());
        assertEquals(132842L, anggota.getNik());
        assertEquals("Adytia Dwi Hermawan", anggota.getName());
        assertEquals("Adytia@gmail.com", anggota.getEmail());
        assertEquals("08514565", anggota.getPhone());
        assertEquals("Kalimantan", anggota.getAddress());
        assertEquals("Male", anggota.getGender());

        verify(anggotaRepository, times(1)).findById(id);
    }

        @Test
        void testGetAnggotaByIdNotFound () {
            Integer id = 1;

            when(anggotaRepository.findById(id)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> anggotaService.getAnggotaById(id));
            assertEquals("Id Anggota Not Found", exception.getMessage());
        }

        @Test
        void testDeleteAnggotaById () {
            Integer id = 1;

            assertDoesNotThrow(() -> anggotaService.deleteAnggotaById(id),
                    "Unexpected exception during delete operation");

        }

}

