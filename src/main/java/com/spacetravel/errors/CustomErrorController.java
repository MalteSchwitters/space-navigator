package com.spacetravel.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @ResponseBody()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public final String handleNotFoundExceptionException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public final String handleBadRequestException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public final String handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public final String handleHttpMediaTypeNotAcceptableExceptionException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody()
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public final String handleConflictException(HttpServletRequest req, Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final String handleAnyException(HttpServletRequest req, Exception ex) {
        log.error("Unexpected exception in request " + req.getRequestURI(), ex);
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

