CREATE TABLE dna (
    id          BIGSERIAL PRIMARY KEY,
    hash        VARCHAR(64) UNIQUE NOT NULL,
    is_mutant   BOOLEAN       NOT NULL,
    created_at  TIMESTAMP     DEFAULT NOW()
);

CREATE INDEX idx_dna_mutant_true ON dna (is_mutant) WHERE is_mutant;
