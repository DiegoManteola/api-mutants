package com.diegomanteola.mutants.repository;

import com.diegomanteola.mutants.model.DnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DnaRepository extends JpaRepository<DnaEntity, Long> {
    Optional<DnaEntity> findByHash(String hash);
    long countByMutant(boolean mutant);
}
