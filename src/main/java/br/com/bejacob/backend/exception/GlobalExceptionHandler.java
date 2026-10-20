package br.com.bejacob.backend.exception;

import br.com.bejacob.backend.exception.appointment.AppointmentConflictException;
import br.com.bejacob.backend.exception.appointment.AppointmentUnauthorizedException;
import br.com.bejacob.backend.exception.appointment.InvalidAppointmentTimeException;
import br.com.bejacob.backend.exception.appointment.PastDateAppointmentException;
import br.com.bejacob.backend.exception.auth.EmailAlreadyExistsException;
import br.com.bejacob.backend.exception.auth.InactiveUserException;
import br.com.bejacob.backend.exception.auth.InvalidCredentialsException;
import br.com.bejacob.backend.exception.auth.InvalidCurrentPasswordException;
import br.com.bejacob.backend.exception.auth.PhoneAlreadyExistsException;
import br.com.bejacob.backend.exception.service.ServiceAlreadyExistsException;
import br.com.bejacob.backend.exception.service.ServiceHasAppointmentsException;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.exception.user.UserHasAppointmentsException;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ApiErrorMessage> buildResponseHandler(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiErrorMessage(status, message));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<ApiErrorMessage> EmailAlreadyExistsHandler(EmailAlreadyExistsException exception){
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    private ResponseEntity<ApiErrorMessage> PhoneAlreadyExistsException(PhoneAlreadyExistsException exception){
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

    @ExceptionHandler(UserHasAppointmentsException.class)
    private ResponseEntity<ApiErrorMessage> UserHasAppointmentsHandler(UserHasAppointmentsException exception){
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    private ResponseEntity<ApiErrorMessage> InvalidCurrentPasswordHandler(InvalidCurrentPasswordException exception){
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorMessage> InvalidCredentialsHandler(InvalidCredentialsException exception) {
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<ApiErrorMessage> InactiveUserHandler(InactiveUserException exception) {
        return buildResponseHandler(HttpStatus.FORBIDDEN, exception.getMessage());
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

    @ExceptionHandler(InvalidAppointmentTimeException.class)
    public ResponseEntity<ApiErrorMessage> InvalidAppointmentTimeHandler(InvalidAppointmentTimeException exception) {
        return buildResponseHandler(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorMessage> ServiceAlreadyExistsHandler(ServiceAlreadyExistsException exception) {
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ServiceHasAppointmentsException.class)
    public ResponseEntity<ApiErrorMessage> ServiceHasAppointmentsHandler(ServiceHasAppointmentsException exception) {
        return buildResponseHandler(HttpStatus.CONFLICT, exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(" | "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorMessage(HttpStatus.BAD_REQUEST, errorMessage));
    }
}