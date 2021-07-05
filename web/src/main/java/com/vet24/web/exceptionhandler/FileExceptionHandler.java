package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(FileNotFoundException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionDto> handleException(StorageException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
