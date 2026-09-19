-- =====================================================
-- TABELA: SERVICE
-- Serviços oferecidos pelo Studio
-- =====================================================

CREATE TABLE service (
    id_service INTEGER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    active BOOLEAN DEFAULT TRUE,

    CONSTRAINT pk_service PRIMARY KEY (id_service),
    CONSTRAINT chk_service_duration CHECK (duration_minutes > 0),
    CONSTRAINT chk_service_price CHECK (price >= 0)
);