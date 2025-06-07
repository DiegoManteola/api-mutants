package com.diegomanteola.mutants.controller;

import com.diegomanteola.mutants.dto.DnaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MutantControllerIT {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void postMutantReturns200() {
        var dna = new DnaRequest(new String[]{
                "ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"});
        var resp = rest.postForEntity("/mutant", dna, Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void postHumanReturns403() {
        var dna = new DnaRequest(new String[]{
                "ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"});
        var resp = rest.postForEntity("/mutant", dna, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
}
