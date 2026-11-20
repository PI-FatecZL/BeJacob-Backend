package br.com.bejacob.backend.exception;

import java.sql.SQLException;
public final class DatabaseErrorCodes {

    public static final String APPOINTMENT_CONFLICT = "BJ409";
    public static final String NOT_FOUND = "BJ404";
    public static final String PAYMENT_METHOD_REQUIRED = "BJ400";
    public static final String INVALID_STATUS = "BJ422";
    public static final String APPOINTMENT_NOT_STARTED = "BJ425";

    private DatabaseErrorCodes() {
    }
    public static boolean hasSqlState(Throwable throwable, String sqlState) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof SQLException sqlException && sqlState.equals(sqlException.getSQLState())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
