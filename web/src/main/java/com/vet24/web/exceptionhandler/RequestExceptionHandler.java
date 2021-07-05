package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(NotFoundException exception) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleException(BadRequestException exception) {
        ExceptionDto data = new ExceptionDto(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleException(MethodArgumentNotValidException exception) {
        String exceptionMessage = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
        ExceptionDto data = new ExceptionDto(exceptionMessage);

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
