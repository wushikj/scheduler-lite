package com.wushi.scheduler.api.entity;

import com.wushi.scheduler.api.interfaces.ApiResult;

import java.time.LocalDateTime;

/**
 * @author yulianghua
 * @date 2020/9/18 5:02 PM
 * @description
 */
public class JsonResult<T> implements ApiResult {
    private Boolean success;
    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳，单位毫秒
     **/
    private LocalDateTime timestamp;

    public Boolean getSuccess() {
        return true;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }
}
