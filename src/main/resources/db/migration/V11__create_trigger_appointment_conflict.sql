CREATE OR REPLACE FUNCTION fn_appointment_before_write()
RETURNS TRIGGER AS $$
DECLARE
    v_duration      INTEGER;
    v_price         NUMERIC(10,2);
    v_schedule_changed BOOLEAN;
    v_reactivated   BOOLEAN;
BEGIN
    v_schedule_changed := TG_OP = 'INSERT'
        OR NEW.starts_at  IS DISTINCT FROM OLD.starts_at
        OR NEW.service_id IS DISTINCT FROM OLD.service_id
        OR NEW.ends_at    IS DISTINCT FROM OLD.ends_at;

    v_reactivated := TG_OP = 'UPDATE'
        AND OLD.status = 'CANCELED'
        AND NEW.status <> 'CANCELED';
    IF v_schedule_changed THEN
        SELECT s.duration_minutes, s.price
          INTO v_duration, v_price
          FROM service s
         WHERE s.id_service = NEW.service_id;

        IF NOT FOUND THEN
            RAISE EXCEPTION 'Serviço % não encontrado.', NEW.service_id
                USING ERRCODE = 'BJ404';
        END IF;

        NEW.ends_at := NEW.starts_at + make_interval(mins => v_duration);

        IF TG_OP = 'INSERT' AND NEW.price IS NULL THEN
            NEW.price := v_price;
        END IF;
    END IF;


    IF NEW.status <> 'CANCELED' AND (v_schedule_changed OR v_reactivated) THEN

       
        PERFORM pg_advisory_xact_lock(
            hashtext('appointment_schedule'),
            (NEW.starts_at::date - DATE '2000-01-01')
        );

        IF EXISTS (
            SELECT 1
              FROM appointment a
             WHERE a.appointment_id <> COALESCE(NEW.appointment_id, -1)
               AND a.status <> 'CANCELED'
               AND a.starts_at < NEW.ends_at
               AND a.ends_at   > NEW.starts_at
        ) THEN
            RAISE EXCEPTION 'Conflito de horário: já existe um agendamento entre % e %.',
                  NEW.starts_at, NEW.ends_at
                USING ERRCODE = 'BJ409';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_appointment_before_write
    BEFORE INSERT OR UPDATE ON appointment
    FOR EACH ROW
    EXECUTE FUNCTION fn_appointment_before_write();
