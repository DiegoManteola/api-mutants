package com.diegomanteola.mutants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MutantsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MutantsApplication.class, args);
    }
}
