package ru.flamexander.transfer.service.core.backend.errors;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class FieldsValidationErrorDto {
    private static final String DEFAULT_CODE = "FIELDS_VALIDATION_ERROR";
    private static final String DEFAULT_MESSAGE = "Поля запроса не прошли проверку";

    private final String code = DEFAULT_CODE;
    private String message = DEFAULT_MESSAGE;
    private final List<FieldValidationError> fields = new ArrayList<>();
    private final LocalDateTime date = LocalDateTime.now();

    public FieldsValidationErrorDto(List<FieldValidationError> fields) {
        this.fields.addAll(fields);
    }
}
