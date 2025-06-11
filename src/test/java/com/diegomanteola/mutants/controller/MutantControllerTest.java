package com.diegomanteola.mutants.controller;

import com.diegomanteola.mutants.service.DnaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DnaService dnaService;

    @Test
    @DisplayName("Retornar 200 cuando sea mutante")
    void returns200Mutant() throws Exception {
        given(dnaService.isMutant(any())).willReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retornar 403 cuando sea humano")
    void returns403NotMutant() throws Exception {
        given(dnaService.isMutant(any())).willReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Retornar 400 cuando no se mande contenido")
    void returns400InvalidInput() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
