-- ==================================================================================
-- PROCEDURE: CLEANUP_OLD_NOTIFICATIONS
-- Deletar notificacoes antigas para minimizar muito espaco ocupado
-- ==================================================================================

CREATE OR REPLACE PROCEDURE sp_cleanup_old_notifications(
    days INTEGER DEFAULT 90
)
LANGUAGE plpgsql
AS $$
BEGIN
DELETE FROM notification
WHERE
    is_read = TRUE AND
    created_at < (CURRENT_TIMESTAMP - (days * INTERVAL '1 day'));
END;
$$;