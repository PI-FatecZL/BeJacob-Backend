-- ==================================================================================
-- TRIGGER: AFTER_INSERT_APPOINTMENT
-- Insercao dos agendamentos a tabela que ligação aos eventos google calendar
-- ==================================================================================

CREATE OR REPLACE FUNCTION fn_create_google_event_record()
    RETURNS TRIGGER AS $$
BEGIN
INSERT INTO appointment_google_event (appointment_id)
VALUES (NEW.appointment_id);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_insert_appointment
    AFTER INSERT ON appointment
    FOR EACH ROW
    EXECUTE FUNCTION fn_create_google_event_record();