package ru.flamexander.transfer.service.core.backend.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> catchResourceNotFoundException(ResourceNotFoundException e) {
        ErrorDto errorDto = new ErrorDto("RESOURCE_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppLogicException.class)
    public ResponseEntity<ErrorDto> catchAppLogicException(AppLogicException e) {
        ErrorDto errorDto = new ErrorDto(e.getCode(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FieldsValidationException.class)
    public ResponseEntity<FieldsValidationErrorDto> catchFieldsValidationException(FieldsValidationException e) {
        String errors = e.getFields().stream().map(x -> x.getFieldName() + ": " + x.getMessage() + "   ").collect(Collectors.joining());
        FieldsValidationErrorDto fieldsValidationErrorDto = new FieldsValidationErrorDto(e.getFields());
     //   FieldsValidationErrorDto fieldsValidationErrorDto = new FieldsValidationErrorDto(errors);
        return new ResponseEntity<>(fieldsValidationErrorDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}