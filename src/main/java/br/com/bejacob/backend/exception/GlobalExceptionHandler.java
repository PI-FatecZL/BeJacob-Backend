package br.com.bejacob.backend.exception;

import br.com.bejacob.backend.exception.auth.EmailAlreadyExistsException;
import br.com.bejacob.backend.exception.auth.InvalidCredentialsException;
import br.com.bejacob.backend.exception.auth.InvalidCurrentPasswordException;
import br.com.bejacob.backend.exception.service.ServiceNotFoundException;
import br.com.bejacob.backend.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    private ResponseEntity<String> EmailAlreadyExistsHandler(EmailAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    private ResponseEntity<String> ServiceNotFoundHandler(ServiceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<String> UserNotFoundHandler(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    private ResponseEntity<String> InvalidCurrentPasswordHandler(InvalidCurrentPasswordException  exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> InvalidCredentialsHandler(InvalidCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}