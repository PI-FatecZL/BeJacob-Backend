CREATE TABLE notification (
          notification_id BIGINT GENERATED ALWAYS AS IDENTITY,
          appointment_id BIGINT NOT NULL,
          message TEXT NOT NULL,
          is_read BOOLEAN NOT NULL DEFAULT FALSE,
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

          CONSTRAINT pk_notification PRIMARY KEY (notification_id),

          CONSTRAINT fk_notification_appointment
              FOREIGN KEY (appointment_id)
                  REFERENCES appointment(appointment_id)
                  ON DELETE CASCADE
                  ON UPDATE CASCADE
);
