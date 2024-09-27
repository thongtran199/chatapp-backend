package com.springboot.chatapp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.MULTIPLE_CHOICES)
public class MultipleRowsFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public MultipleRowsFoundException(String resourceName) {
        super(String.format("Multiple %s found", resourceName));
        this.resourceName = resourceName;
    }

}
