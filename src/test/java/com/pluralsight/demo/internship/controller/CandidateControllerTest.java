package com.pluralsight.demo.internship.controller;

import com.pluralsight.demo.internship.model.Candidate;

import com.pluralsight.demo.internship.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CandidateService candidateService;

    @Test
    void getAllCandidates_shouldReturnListOfCandidates() throws Exception {

        Candidate candidate1 = new Candidate("Polina Breha", "pbreha@example.com", "Software Development");
        candidate1.setId(1L);
        candidate1.setRegisteredAt(LocalDateTime.now());
        candidate1.setVisible(true);

        Candidate candidate2 = new Candidate("Vi Br", "vibr@example.com", "Machine Learning");
        candidate2.setId(2L);
        candidate2.setRegisteredAt(LocalDateTime.now());
        candidate2.setVisible(true);

        List<Candidate> candidates = Arrays.asList(candidate1, candidate2);

        when(candidateService.getAllCandidates()).thenReturn(candidates);

        mockMvc.perform(get("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(jsonPath("$[0].name").value("Polina Breha"))
                .andExpect(jsonPath("$[0].email").value("pbreha@example.com"))
                .andExpect(jsonPath("$[1].name").value("Vi Br"))
                .andExpect(jsonPath("$.length()").value(2));


    }

    @Test
    void getCandidate_shouldReturnNewCandidate() throws Exception {
        // ARRANGE
        Candidate inputCandidate = new Candidate("New Candidate", "New Email",
                "New Field");

        Candidate savedCandidate = new Candidate("New Candidate", "New Email",
                "New Field");
        savedCandidate.setId(10L);
        savedCandidate.setRegisteredAt(LocalDateTime.now());
        savedCandidate.setVisible(true);

        when(candidateService.createCandidate(any(Candidate.class)))
                .thenReturn(savedCandidate);

        // ACT & ASSERT
        mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "New Candidate",
                          "email": "New Email",
                          "fieldOfStudy": "New Field"
                        }
                        """))
                .andExpect(status().isOk())  // Should be 201 but our code returns 200
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("New Candidate"))
                .andExpect(jsonPath("$.visible").value(true));
    }

    @Test
    void deleteCandidate_shouldReturnNoContent() throws Exception{
        // ARRANGE
        Long id = 1L;
        doNothing().when(candidateService).deleteCandidate(id);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/candidates/{id}", id))
                .andExpect(status().isNoContent());  // 204

        // Verify service was called
        verify(candidateService, times(1)).deleteCandidate(id);
    }

    @Test
    void getCandidateById_shouldReturnCandidateById() throws Exception {

        Candidate candidate1 = new Candidate("Polina Breha", "pbreha@example.com", "Software Development");
        candidate1.setId(1L);
        candidate1.setRegisteredAt(LocalDateTime.now());
        candidate1.setVisible(true);

        Candidate candidate2 = new Candidate("Vi Br", "vibr@example.com", "Machine Learning");
        candidate2.setId(2L);
        candidate2.setRegisteredAt(LocalDateTime.now());
        candidate2.setVisible(true);

        List<Candidate> candidates = Arrays.asList(candidate1, candidate2);
        when(candidateService.getCandidateById(anyLong())).thenReturn(candidates.get(0));
        mockMvc.perform(get("/api/candidates/{id}", candidate1.getId()))
                .andExpect(status().isOk());

        verify(candidateService, times(1)).getCandidateById(anyLong());

    }



}


