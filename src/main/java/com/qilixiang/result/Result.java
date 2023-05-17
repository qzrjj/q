package com.qilixiang.result;

import com.qilixiang.constant.BaseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private int code = BaseCode.SUCCESS;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result result = new Result();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result result = new Result(code, message);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result result = new Result(BaseCode.SERVER_ERROR, message);
        return result;
    }
}
