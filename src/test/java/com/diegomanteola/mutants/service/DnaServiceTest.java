package com.diegomanteola.mutants.service;

import com.diegomanteola.mutants.config.DnaConfig;
import com.diegomanteola.mutants.model.DnaEntity;
import com.diegomanteola.mutants.repository.DnaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DnaServiceTest {

    @Mock
    private DnaRepository repo;

    private DnaService sut;                 // SUT = System Under Test

    private static final String[] MUTANT_DNA = {
            "ATGCGA","CAGTGC","TTATGT",
            "AGAAGG","CCCCTA","TCACTG"};

    private static final String[] HUMAN_DNA = {
            "ATGCGA","CAGTGC","TTATTT",
            "AGACGG","GCGTCA","TCACTG"};

    @BeforeEach
    void setUp() {
        DnaConfig cfg = new DnaConfig();    // usa defaults 4 / 2 / [A,T,C,G]
        sut = new DnaService(cfg, repo);
    }

    /* ---------- CASO MUTANTE --------------------------------------- */

    @Test
    @DisplayName("Devuelve true y persiste cuando el ADN es mutante")
    void mutantMatrixReturnsTrue() {
        when(repo.findByHash(any())).thenReturn(Optional.empty());

        assertTrue(sut.isMutant(MUTANT_DNA));

        verify(repo, times(1)).save(argThat(DnaEntity::isMutant));
    }

    /* ---------- CASO HUMANO ---------------------------------------- */

    @Test
    @DisplayName("Devuelve false y persiste cuando el ADN es humano")
    void humanMatrixReturnsFalse() {
        when(repo.findByHash(any())).thenReturn(Optional.empty());

        assertFalse(sut.isMutant(HUMAN_DNA));

        verify(repo, times(1)).save(argThat(e -> !e.isMutant()));
    }

    /* ---------- CARÁCTER INVÁLIDO ---------------------------------- */

    @Test
    @DisplayName("Lanza IllegalArgumentException si el ADN contiene bases inválidas")
    void invalidCharThrows() {
        String[] bad = {"ATGXGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"};
        assertThrows(IllegalArgumentException.class, () -> sut.isMutant(bad));
        verifyNoInteractions(repo);         // no toca la base
    }

    /* ---------- DEDUPLICACIÓN -------------------------------------- */

    @Test
    @DisplayName("No recalcula si el hash ya existe en la base")
    void skipsComputationWhenHashIsCached() {
        when(repo.findByHash(any()))
                .thenReturn(Optional.of(new DnaEntity("hash", true)));

        assertTrue(sut.isMutant(MUTANT_DNA));

        verify(repo, never()).save(any());
    }
}
