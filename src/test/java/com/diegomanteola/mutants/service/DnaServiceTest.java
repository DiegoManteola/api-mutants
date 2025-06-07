package com.diegomanteola.mutants.service;

import com.diegomanteola.mutants.config.DnaConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnaServiceTest {

    private final DnaConfig cfg = new DnaConfig();   // usa defaults
    private final DnaService sut = new DnaService(cfg);

    @Test
    void mutantMatrixReturnsTrue() {
        String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        assertTrue(sut.isMutant(dna));
    }

    @Test
    void humanMatrixReturnsFalse() {
        String[] dna = {"ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"};
        assertFalse(sut.isMutant(dna));
    }

    @Test
    void invalidCharThrows() {
        String[] dna = {"ATGXGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"};
        assertThrows(IllegalArgumentException.class, () -> sut.isMutant(dna));
    }
}