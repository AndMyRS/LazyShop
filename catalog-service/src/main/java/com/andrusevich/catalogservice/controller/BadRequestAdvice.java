package com.andrusevich.catalogservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadRequestAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Create product: Error occurred"
        );
        problemDetail.setProperty("errors", exception.getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .toList());

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
