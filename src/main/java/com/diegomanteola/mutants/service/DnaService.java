package com.diegomanteola.mutants.service;

import org.springframework.stereotype.Service;
import com.diegomanteola.mutants.config.DnaConfig;
import java.util.Set;

@Service
public class DnaService {

    private final int L;          // sequence length
    private final int needed;     // required matches
    private final Set<Character> valid;

    public DnaService(DnaConfig cfg) {
        this.L      = cfg.getSequenceLength();
        this.needed = cfg.getRequiredMatches();
        this.valid  = cfg.getValidBases();
    }

    public boolean isMutant(String[] dna) {
        int n = dna.length;
        if (n < L) return false;

        char[][] g = new char[n][n];
        for (int i = 0; i < n; i++) {
            if (dna[i].length() != n) return false;
            for (int j = 0; j < n; j++) {
                char c = Character.toUpperCase(dna[i].charAt(j));
                if (!valid.contains(c)) {
                    throw new IllegalArgumentException(
                            "Base no válida: " + c + " en fila " + i + ", columna " + j);
                }
                g[i][j] = c;
            }
        }

        int found = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                char b = g[i][j];
                if (j <= n - L && check(g, b, i, j, 0, 1))  // →
                    if (++found >= needed) return true;

                if (i <= n - L && check(g, b, i, j, 1, 0))  // ↓
                    if (++found >= needed) return true;

                if (i <= n - L && j <= n - L && check(g, b, i, j, 1, 1))  // ↘
                    if (++found >= needed) return true;

                if (i <= n - L && j >= L - 1 && check(g, b, i, j, 1, -1)) // ↙
                    if (++found >= needed) return true;
            }
        }
        return false;
    }

    /** comprueba L-1 posiciones adicionales a partir de (i,j) con paso (di,dj) */
    private boolean check(char[][] g, char b, int i, int j, int di, int dj) {
        for (int k = 1; k < L; k++) {
            if (g[i + di * k][j + dj * k] != b) return false;
        }
        return true;
    }
}