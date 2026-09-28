package br.com.bejacob.backend.exception;

import br.com.bejacob.backend.exception.appointment.AppointmentConflictException;
import br.com.bejacob.backend.exception.appointment.AppointmentUnauthorizedException;
import br.com.bejacob.backend.exception.appointment.PastDateAppointmentException;
import br.com.bejacob.backend.exception.auth.EmailAlreadyExistsException;
import br.com.bejacob.backend.exception.auth.InvalidCredentialsException;
import br.com.bejacob.backend.exception.auth.InvalidCurrentPasswordException;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ApiErrorMessage> buildResponseHandler(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiErrorMessage(status, message));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<ApiErrorMessage> EmailAlreadyExistsHandler(EmailAlreadyExistsException exception){
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    private ResponseEntity<ApiErrorMessage> ServiceNotFoundHandler(ServiceNotFoundException exception){
        return buildResponseHandler(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ApiErrorMessage> UserNotFoundHandler(UserNotFoundException exception){
        return buildResponseHandler(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    private ResponseEntity<ApiErrorMessage> InvalidCurrentPasswordHandler(InvalidCurrentPasswordException exception){
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorMessage> InvalidCredentialsHandler(InvalidCredentialsException exception) {
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<ApiErrorMessage> AppointmentConflictHandler(AppointmentConflictException exception) {
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(PastDateAppointmentException.class)
    public ResponseEntity<ApiErrorMessage> PastDateAppointmentHandler(PastDateAppointmentException exception) {
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AppointmentUnauthorizedException.class)
    public ResponseEntity<ApiErrorMessage> AppointmentUnauthorizedHandler(AppointmentUnauthorizedException exception) {
        return buildResponseHandler(HttpStatus.FORBIDDEN, exception.getMessage());
    }
}