package com.diegomanteola.mutants.service;

import com.diegomanteola.mutants.config.DnaConfig;
import com.diegomanteola.mutants.model.DnaEntity;
import com.diegomanteola.mutants.repository.DnaRepository;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DnaService {

    private final DnaRepository repo;
    private final int L, needed;
    private final Set<Character> valid;

    public DnaService(DnaConfig cfg, DnaRepository repo) {
        this.repo = repo;
        this.L = cfg.getSequenceLength();
        this.needed = cfg.getRequiredMatches();
        this.valid = cfg.getValidBases();
    }

    public boolean isMutant(String[] dna) {
        validateMatrix(dna);
        String hash = DigestUtils.sha256Hex(String.join("", dna));
        return computeAndPersist(hash, dna);
    }

    @Cacheable(value = "dna", key = "#hash")
    private boolean computeAndPersist(String hash, String[] dna) {
        // ¿ya esta en la base?
        return repo.findByHash(hash)
                .map(DnaEntity::isMutant)
                .orElseGet(() -> {
                    boolean result = compute(dna);
                    repo.save(new DnaEntity(hash, result));   // único por constraint
                    return result;
                });
    }

    private boolean compute(String[] dna) {

        int n = dna.length;
        if (n < L) return false;

        char[][] g = new char[n][n];

        int found = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char b = g[i][j];

                if (j <= n - L && check(g, b, i, j, 0, 1) && ++found >= needed) return true;
                if (i <= n - L && check(g, b, i, j, 1, 0) && ++found >= needed) return true;
                if (i <= n - L && j <= n - L && check(g, b, i, j, 1, 1) && ++found >= needed) return true;
                if (i <= n - L && j >= L - 1 && check(g, b, i, j, 1, -1) && ++found >= needed) return true;
            }
        }
        return false;
    }

    private void validateMatrix(String[] dna) {
        int n = dna.length;
        if (n < L) throw new IllegalArgumentException("Matriz muy chica");

        for (int i = 0; i < n; i++) {
            if (dna[i].length() != n)
                throw new IllegalArgumentException("No es NxN");

            for (int j = 0; j < n; j++) {
                char c = Character.toUpperCase(dna[i].charAt(j));
                if (!valid.contains(c)) {
                    throw new IllegalArgumentException(
                            "Base inválida: '" + c + "' en fila " + (i + 1) + ", columna " + (j + 1));
                }
            }
        }
    }

    private boolean check(char[][] g, char b, int i, int j, int di, int dj) {
        for (int k = 1; k < L; k++) {
            if (g[i + di * k][j + dj * k] != b) return false;
        }
        return true;
    }
}