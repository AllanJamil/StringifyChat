package se.nackademin.stringify.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ConnectionLimitException;
import se.nackademin.stringify.exception.InvalidKeyException;
import se.nackademin.stringify.exception.ProfileNotFoundException;
import se.nackademin.stringify.exception.response.ErrorResponse;
import se.nackademin.stringify.util.DateUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status);


        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ChatSessionNotFoundException.class, ProfileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex) {
        return getAndLogApiException(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ConnectionLimitException.class)
    public ResponseEntity<ErrorResponse> handleConnectionLimitException(Exception ex) {
        return getAndLogApiException(HttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidKeyException(Exception ex) {
        return getAndLogApiException(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<ErrorResponse> getAndLogApiException(HttpStatus status, Exception ex ) {
        log.warn(ex.getMessage());

        return new ResponseEntity<>(ErrorResponse.builder()
                .exceptionType(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .timestamp(DateUtil.dateToString(new Timestamp(new Date().getTime())))
                .status(status)
                .build(), status);
    }
}
