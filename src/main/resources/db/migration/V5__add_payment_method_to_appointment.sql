ALTER TABLE appointment
    ADD COLUMN payment_method VARCHAR(10);

ALTER TABLE appointment
    ADD CONSTRAINT chk_appointment_payment_method
        CHECK (payment_method IN ('PIX', 'DEBIT', 'CREDIT', 'CASH'));

ALTER TABLE appointment
    ADD COLUMN price NUMERIC(10,2);


UPDATE appointment a
SET price = s.price
FROM service s
WHERE s.id_service = a.service_id;

ALTER TABLE appointment
    ALTER COLUMN price SET NOT NULL;

ALTER TABLE appointment
    ADD CONSTRAINT chk_appointment_price CHECK (price >= 0);
