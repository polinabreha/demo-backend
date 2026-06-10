package com.pluralsight.demo.internship.service;


import com.pluralsight.demo.internship.model.Candidate;
import com.pluralsight.demo.internship.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {
    @Mock
    private CandidateRepository candidateRepository;
    @InjectMocks
    private CandidateService candidateService;

    @Test
    void createInternship_whenAutoVisibleFalse_shouldPublish() throws  Exception{
        ReflectionTestUtils.setField(candidateService, "visibleByDefault", true);

        Candidate candidate = new Candidate("Polina", "polina@example.com", "Math");
        candidate.setVisible(true);

        when(candidateRepository.save(any(Candidate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Candidate result = candidateService.createCandidate(candidate);

        // ASSERT
        assertNotNull(result.getRegisteredAt());

        verify(candidateRepository).save(argThat(savedCandidate ->
                savedCandidate.getRegisteredAt() != null
        ));
    }



    @Test
    void getCandidateById_whenNotFound_shouldThrowException() {
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            candidateService.getCandidateById(99L);
        });
    }
}
