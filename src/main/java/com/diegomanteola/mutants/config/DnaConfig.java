package com.diegomanteola.mutants.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "dna")
public class DnaConfig {

    private int sequenceLength = 4;
    private int requiredMatches = 2;
    private Set<Character> validBases = Set.of('A', 'T', 'C', 'G');

}