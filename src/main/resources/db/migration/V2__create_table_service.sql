
--tabela serviços
CREATE TABLE service (
                         id_service INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         name VARCHAR(150) NOT NULL,
                         description TEXT,
                         duration_minutes INTEGER NOT NULL,
                         price NUMERIC(10,2) NOT NULL,
                         active BOOLEAN DEFAULT TRUE,

                         CONSTRaAINT chk_service_duration
                             CHECK (duration_minutes > 0),

                         CONSTRAINT chk_service_price
                             CHECK (price >= 0)
);