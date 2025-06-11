package com.diegomanteola.mutants.controller;

import com.diegomanteola.mutants.repository.DnaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatsController {

    private final DnaRepository repo;

    public StatsController(DnaRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        long mutants = repo.countByMutant(true);
        long humans  = repo.countByMutant(false);
        double ratio = humans == 0 ? 0.0 : (double) mutants / humans;

        return Map.of(
                "count_mutant_dna", mutants,
                "count_human_dna",  humans,
                "ratio",            ratio
        );
    }
}
