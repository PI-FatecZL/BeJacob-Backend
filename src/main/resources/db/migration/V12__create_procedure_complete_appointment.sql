CREATE OR REPLACE PROCEDURE sp_complete_appointment(
    p_id             BIGINT,
    p_payment_method VARCHAR,
    p_reference_time TIMESTAMP DEFAULT LOCALTIMESTAMP
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_status    VARCHAR(10);
    v_starts_at TIMESTAMP;
BEGIN
    SELECT status, starts_at
      INTO v_status, v_starts_at
      FROM appointment
     WHERE appointment_id = p_id
       FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Agendamento % não encontrado.', p_id
            USING ERRCODE = 'BJ404';
    END IF;

    IF p_payment_method IS NULL OR p_payment_method NOT IN ('PIX', 'DEBIT', 'CREDIT', 'CASH') THEN
        RAISE EXCEPTION 'Informe uma forma de pagamento válida para concluir o agendamento.'
            USING ERRCODE = 'BJ400';
    END IF;

    IF v_status <> 'SCHEDULED' THEN
        RAISE EXCEPTION 'Apenas agendamentos SCHEDULED podem ser concluídos (status atual: %).', v_status
            USING ERRCODE = 'BJ422';
    END IF;

    IF v_starts_at > p_reference_time THEN
        RAISE EXCEPTION 'O atendimento ainda não começou (início em %).', v_starts_at
            USING ERRCODE = 'BJ425';
    END IF;

    UPDATE appointment
       SET status = 'COMPLETED',
           payment_method = p_payment_method
     WHERE appointment_id = p_id;
END;
$$;
