package com.diegomanteola.mutants.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { // se agrega la data de conexión para que funcione de forma local los tests con docker y postgresql
                "spring.datasource.url=jdbc:postgresql://localhost:5432/dnabase",
                "spring.datasource.username=dnauser",
                "spring.datasource.password=dnapass",
                "spring.flyway.enabled=false"
        }
)
class StatsControllerIT {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void statsReturns200() {
        ResponseEntity<Map> resp = rest.getForEntity("/stats", Map.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().containsKey("count_mutant_dna"));
        assertTrue(resp.getBody().containsKey("count_human_dna"));
        assertTrue(resp.getBody().containsKey("ratio"));
    }
}
