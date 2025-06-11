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
    private final int seqLength, reqMatches;
    private final Set<Character> validBases;

    public DnaService(DnaConfig cfg, DnaRepository repo) {
        this.repo = repo;
        this.seqLength = cfg.getSequenceLength();
        this.reqMatches = cfg.getRequiredMatches();
        this.validBases = cfg.getValidBases();
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
                    repo.save(new DnaEntity(hash, result));
                    return result;
                });
    }

    private boolean compute(String[] dna) {
        int n = dna.length;
        int found = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char b = get(dna, i, j);

                if (j <= n - seqLength && check(dna, b, i, j, 0, 1) && ++found >= reqMatches) return true;
                if (i <= n - seqLength && check(dna, b, i, j, 1, 0) && ++found >= reqMatches) return true;
                if (i <= n - seqLength && j <= n - seqLength && check(dna, b, i, j, 1, 1) && ++found >= reqMatches) return true;
                if (i <= n - seqLength && j >= seqLength - 1 && check(dna, b, i, j, 1, -1) && ++found >= reqMatches) return true;
            }
        }
        return false;
    }

    private char get(String[] dna, int row, int col) {
        return Character.toUpperCase(dna[row].charAt(col));
    }

    private boolean check(String[] dna, char b, int i, int j, int di, int dj) {
        for (int k = 1; k < seqLength; k++) {
            if (get(dna, i + di*k, j + dj*k) != b) return false;
        }
        return true;
    }

    private void validateMatrix(String[] dna) {
        int n = dna.length;
        if (n < seqLength) throw new IllegalArgumentException("Matriz muy chica");

        for (int i = 0; i < n; i++) {
            if (dna[i].length() != n)
                throw new IllegalArgumentException("No es NxN");

            for (int j = 0; j < n; j++) {
                char c = Character.toUpperCase(dna[i].charAt(j));
                if (!validBases.contains(c)) {
                    throw new IllegalArgumentException(
                            "Base inválida: '" + c + "' en fila " + (i + 1) + ", columna " + (j + 1));
                }
            }
        }
    }
}