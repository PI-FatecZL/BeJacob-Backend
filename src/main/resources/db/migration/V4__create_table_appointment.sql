-- =====================================================
-- TABELA: APPOINTMENT
-- Agendamentos de serviços do Studio
-- =====================================================

CREATE TABLE appointment (
         appointment_id BIGINT GENERATED ALWAYS AS IDENTITY,
         service_id BIGINT NOT NULL,
         user_id BIGINT NOT NULL,
         starts_at TIMESTAMP NOT NULL,
         ends_at TIMESTAMP NOT NULL,
         notes TEXT,
         status VARCHAR(10) DEFAULT 'SCHEDULED',
         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

         CONSTRAINT pk_appointment PRIMARY KEY (appointment_id),

         CONSTRAINT fk_appointment_user
             FOREIGN KEY (user_id)
                 REFERENCES users(user_id)
                 ON DELETE RESTRICT
                 ON UPDATE CASCADE,

         CONSTRAINT fk_appointment_service
             FOREIGN KEY (service_id)
                 REFERENCES service(id_service)
                 ON DELETE RESTRICT
                 ON UPDATE CASCADE,

         CONSTRAINT chk_appointment_duration
             CHECK (starts_at < ends_at)
);