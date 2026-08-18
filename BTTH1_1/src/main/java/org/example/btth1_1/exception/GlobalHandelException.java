package org.example.btth1_1.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalHandelException {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return receptionistMessage(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return receptionistMessage(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException exception) {
        return "Dạ, " + exception.getMessage()
                + ". Anh/chị vui lòng kiểm tra và cung cấp lại giúp em ạ.";
    }

    private String receptionistMessage(List<String> errors) {
        return "Dạ, để em hỗ trợ anh/chị đặt phòng, anh/chị vui lòng cung cấp thêm: "
                + String.join(", ", errors)
                + ". Cảm ơn anh/chị ạ!";
    }
}
