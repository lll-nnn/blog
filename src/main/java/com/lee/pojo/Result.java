package com.lee.pojo;

public class Result<T> {
    private Integer code;
    private String message;
    private Boolean success;
    private T data;

    public Result() {
        this.code = 200;
        this.message = "success";
        this.success = Boolean.TRUE;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
