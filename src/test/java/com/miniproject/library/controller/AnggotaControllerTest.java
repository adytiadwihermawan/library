package com.miniproject.library.controller;

import com.miniproject.library.dto.anggota.AnggotaRequest;
import com.miniproject.library.dto.anggota.AnggotaResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.service.AnggotaService;
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

class AnggotaControllerTest {
    @InjectMocks
    AnggotaController anggotaController;
    @Mock
    AnggotaService anggotaService;
    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private List<Anggota>anggotaList(){
        Anggota sampleAnggota = new Anggota();
        sampleAnggota.setId(1);
        sampleAnggota.setNik(132842L);
        sampleAnggota.setName("Adytia Dwi Hermawan");
        sampleAnggota.setEmail("Adytia@gmail.com");
        sampleAnggota.setPhone("08514565");
        sampleAnggota.setAddress("Kalimantan");
        sampleAnggota.setGender("Male");

        Anggota anggota = new Anggota();
        anggota.setId(2);
        anggota.setNik(12345L);
        anggota.setName("bandar b");
        anggota.setEmail("bandar@gmail.com");
        anggota.setPhone("085145659");
        anggota.setAddress("Depok");
        anggota.setGender("futa");

        List<Anggota>anggotaList = new ArrayList<>();
        anggotaList.add(sampleAnggota);
        anggotaList.add(anggota);
        return anggotaList;
    }
    @Test
    void updateAnggota() {
        AnggotaRequest request = new AnggotaRequest();
        request.setNik(1234567890L);
        request.setName("qowi");
        request.setEmail("qowi@gmail.com");
        request.setPhone("987-654-3210");
        request.setAddress("456 Updated St");
        request.setGender("male");

        Anggota existingAnggota = anggotaList().get(1);
        Anggota anggota = mapper.map(request,Anggota.class);
        anggota.setId(1);
        AnggotaResponse response = mapper.map(anggota,AnggotaResponse.class);
        when(anggotaService.updateAnggota(request,existingAnggota.getId())).thenReturn(response);
        ResponseEntity<AnggotaResponse> responseEntity= anggotaController.updateAnggota(existingAnggota.getId(), request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getAllAnggota() {
        when(anggotaService.getAllAnggota()).thenReturn(anggotaList());
        ResponseEntity<List<Anggota>> responseEntity = anggotaController.getAllAnggota();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(anggotaList(), responseEntity.getBody());
    }

    @Test
    void getAnggotaById() {
        when(anggotaService.getAnggotaById(2)).thenReturn(anggotaList().get(1));
        ResponseEntity<Anggota> responseEntity= anggotaController.getAnggotaById(2);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(anggotaList().get(1), responseEntity.getBody());
    }

    @Test
    void deleteAnggotaById() {
        doNothing().when(anggotaService).deleteAnggotaById(1);
        ResponseEntity<Void>responseEntity = anggotaController.deleteAnggotaById(1);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(anggotaService,times(1)).deleteAnggotaById(1);
    }
}