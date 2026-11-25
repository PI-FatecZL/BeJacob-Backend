-- ===================================================================
-- TABELA: APPOINTMENT_GOOGLE_EVENT
-- Agendamentos de serviços do Studio vinculados ao google calendar
-- ===================================================================

CREATE TABLE appointment_google_event (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    appointment_id BIGINT NOT NULL UNIQUE,
    google_event_id VARCHAR(255),

    CONSTRAINT pk_appointment_google_event PRIMARY KEY (id),
    CONSTRAINT fk_age_appointment
      FOREIGN KEY (appointment_id)
          REFERENCES appointment(appointment_id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
);