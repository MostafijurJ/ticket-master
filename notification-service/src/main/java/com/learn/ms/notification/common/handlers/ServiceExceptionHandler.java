package com.learn.ms.notification.common.handlers;


import com.learn.ms.notification.common.utils.ResponseUtils;
import com.learn.ms.notification.core.domain.enums.ApiResponseCode;
import com.learn.ms.notification.core.domain.enums.ResponseMessage;
import com.learn.ms.notification.core.domain.exceptions.CustomRootException;
import com.learn.ms.notification.core.domain.exceptions.DatabaseException;
import com.learn.ms.notification.core.domain.exceptions.DomainException;
import com.learn.ms.notification.core.domain.exceptions.FeignClientException;
import com.learn.ms.notification.core.domain.exceptions.InterServiceCommunicationException;
import com.learn.ms.notification.core.domain.exceptions.InvalidRequestDataException;
import com.learn.ms.notification.core.domain.exceptions.MethodNotAllowedException;
import com.learn.ms.notification.core.domain.exceptions.OperationFailedException;
import com.learn.ms.notification.core.domain.exceptions.OperationHoldException;
import com.learn.ms.notification.core.domain.exceptions.RecordNotFoundException;
import com.learn.ms.notification.core.domain.exceptions.UnauthorizedResourceException;
import com.learn.ms.notification.core.domain.model.ApiResponse;
import com.learn.ms.notification.core.service.LocaleMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler extends BaseExceptionHandler {
    private final LocaleMessageService localeMessageService;

    @ExceptionHandler({
            DomainException.class,
            DatabaseException.class,
            InvalidRequestDataException.class,
            MethodNotAllowedException.class,
            OperationFailedException.class,
            OperationHoldException.class,
            UnauthorizedResourceException.class,
            RecordNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomRootException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), getMessage(ex.getMessage()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler({InterServiceCommunicationException.class})
    public ResponseEntity<ApiResponse<Void>> handleFeignClientException(CustomRootException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiResponse<Void>> commonException(Exception ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.UNHANDLED_EXCEPTION.getResponseCode(), getMessage(ResponseMessage.INTERNAL_SERVICE_EXCEPTION.getResponseMessage()));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ExceptionHandler(FeignClientException.class)
    public final ResponseEntity<ApiResponse<Void>> handleFeignClientException(FeignClientException ex) {
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Void> apiResponse = ResponseUtils.createApiResponse(ex.getMessageCode(), ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        errorLogger.error("HttpMessageNotReadableException: ", ex);
        String message = getMessage(ResponseMessage.INVALID_REQUEST_DATA.getResponseMessage());
        ApiResponse<Object> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), message);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        String message = getMessage(ResponseMessage.INVALID_REQUEST_DATA.getResponseMessage());
        ApiResponse<Object> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), message, errors);

        dropErrorLogForArgumentNotValid("****Custom Jakarta Validation Error**** ", ex.getParameter().getDeclaringClass().getName(),
                Objects.isNull(ex.getParameter().getMethod()) ? "" : ex.getParameter().getMethod().getName(),
                message,
                errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    private void dropErrorLogForArgumentNotValid(final String logHeader, final String className, final String methodName, final String message, final Object data) {
        errorLogger.error(String.format(logHeader +
                "\nClassName: %s | MethodName: %s | Message : %s" +
                "\nError Data: %s", className, methodName, message, data));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(ResponseMessage.INVALID_REQUEST_METHOD_TYPE.getResponseMessage());
        errorLogger.error(ex.getLocalizedMessage(), ex);
        ApiResponse<Object> apiResponse = ResponseUtils.createApiResponse(ApiResponseCode.METHOD_NOT_ALLOWED.getResponseCode(), message);

        return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private String getMessage(String messageKey) {
        String message = StringUtils.EMPTY;
        try {
            message = localeMessageService.getLocalMessage(messageKey);
        } catch (Exception ex) {
            errorLogger.error(ex.getLocalizedMessage(), ex);
        }
        return StringUtils.isNotBlank(message) ? message : messageKey;
    }
}
