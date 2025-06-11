package com.diegomanteola.mutants.model;
import jakarta.persistence.*;

@Entity
@Table(name = "dna", uniqueConstraints = @UniqueConstraint(columnNames = "hash"))
public class DnaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String hash;

    @Column(name = "is_mutant", nullable = false)
    private boolean mutant;

    protected DnaEntity() {}               // JPA only

    public DnaEntity(String hash, boolean mutant) {
        this.hash = hash;
        this.mutant = mutant;
    }

    public boolean isMutant() { return mutant; }
}
