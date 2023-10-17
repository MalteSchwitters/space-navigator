package com.spacetravel.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@ControllerAdvice
public class CustomErrorController implements ErrorController {

    @ResponseBody()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public final String handleIllegalArgumentExceptionException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @GetMapping(value = "/error")
    public ResponseEntity<String> error_get(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        // exception is servlet exception or null
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        Throwable cause = exception != null ? exception.getCause() : null;

        ResponseEntity.BodyBuilder response = ResponseEntity.status(statusCode);
        if (cause != null) {
            return response.body(cause.getMessage());
        }
        return response.build();
    }

    @PutMapping(value = "/error")
    public ResponseEntity<String> error_put(HttpServletRequest request) {
        return error_get(request);
    }

    @PostMapping(value = "/error")
    public ResponseEntity<String> error_post(HttpServletRequest request) {
        return error_get(request);
    }

    @DeleteMapping(value = "/error")
    public ResponseEntity<String> error_delete(HttpServletRequest request) {
        return error_get(request);
    }
}

