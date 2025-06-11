package com.diegomanteola.mutants.controller;

import com.diegomanteola.mutants.repository.DnaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsControllerTest {

    @Test
    @DisplayName("Calcula ratio correcto con humanos = 0")
    void statsRatioZeroWhenNoHumans() {
        DnaRepository repo = mock(DnaRepository.class);
        when(repo.countByMutant(true)).thenReturn(10L);
        when(repo.countByMutant(false)).thenReturn(0L);

        StatsController ctrl = new StatsController(repo);

        Map<String, Object> json = ctrl.stats();

        assertEquals(10L,  json.get("count_mutant_dna"));
        assertEquals(0L,   json.get("count_human_dna"));
        assertEquals(0.0, (double) json.get("ratio"));
    }
}
