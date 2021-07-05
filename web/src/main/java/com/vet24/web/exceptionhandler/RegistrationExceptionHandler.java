package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.exception.RepeatedRegistrationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;

@ControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionDto> handleException(MessagingException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(RepeatedRegistrationException.class)
    public ResponseEntity<ExceptionDto> handleException(RepeatedRegistrationException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_ACCEPTABLE);
    }

}
