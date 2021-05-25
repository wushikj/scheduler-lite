package com.wushi.scheduler.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wushi.scheduler.api.interfaces.ApiResult;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author yulianghua
 * @date 2020/9/21 11:24 AM
 * @description
 */
public class ErrorResult  implements ApiResult {
    private Boolean success;

    /**
     * 错误信息
     **/
    private ErrorMessageContent error;

    /**
     * 响应状态码
     **/
    @JsonIgnore()
    private HttpStatus httpStatus;

    /**
     * 时间戳，单位毫秒
     **/
    private LocalDateTime timestamp;

    public Boolean getSuccess() {
        return false;
    }

    public ErrorMessageContent getError() {
        return error;
    }

    public void setError(ErrorMessageContent error) {
        this.error = error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

    public ErrorResult(ErrorMessageContent errorMessageContent) {
        this.error = errorMessageContent;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ErrorResult(ErrorMessageContent errorMessageContent, HttpStatus httpStatus) {
        this.error = errorMessageContent;
        this.httpStatus = httpStatus;
    }
}
