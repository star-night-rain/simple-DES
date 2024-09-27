package org.example.simpledes.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(
                1,
                "success",
                null
        );
    }

    public static Result success(Object data) {
        return new Result(
                1,
                "success",
                data
        );
    }

    public static Result error(Object data) {
        return new Result(
                0,
                "error",
                data
        );
    }
}
