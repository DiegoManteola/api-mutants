package com.diegomanteola.mutants.controller;

import com.diegomanteola.mutants.dto.DnaRequest;
import com.diegomanteola.mutants.service.DnaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    private final DnaService dnaService;

    public MutantController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping
    public ResponseEntity<Void> isMutant(@RequestBody DnaRequest body) {
        boolean mutant = dnaService.isMutant(body.dna());
        return mutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(403).build();
    }
}