package com.y.javachat.system.exception;

import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handlerUserNotFoundException(ObjectNotFoundException e) {
        return new Result(false, StatusCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicationJoinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleDuplicationJoinException(DuplicationJoinException e){
        return new Result(false, StatusCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT, "제공된 인자가 타당하지 않습니다.", map);
    }

    @ExceptionHandler(ExternalAPIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleExternalAPIException(Exception e){
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception e) {
        return new Result(false, StatusCode.UNAUTHORIZED, "이메일 혹은 비밀번호가 틀렸습니다.", e.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException e) {
        return new Result(false, StatusCode.UNAUTHORIZED, "사용자 계정에 문제가 있습니다.", e.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return new Result(false, StatusCode.UNAUTHORIZED, "제공된 엑세스 토큰이 만료되어 다시 발급되야 하며, 형식이 틀렸습니다 혹은 다른 이유로 타당하지 않습니다. ", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException e) {
        return new Result(false, StatusCode.FORBIDDEN, "허가되지 않습니다.", e.getMessage());
    }

    // fallback(대비책): 예상 밖의 오류를 받아 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception e) {
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "서버 내부 오류", e.getMessage());
    }

}
