package com.vet24.web.exceptionhandler;

import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.exception.RepeatedCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(RepeatedCommentException.class)
    public ResponseEntity<ExceptionDto> handleException(RepeatedCommentException exception) {
        ExceptionDto data = new ExceptionDto();
        data.setMessage(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
