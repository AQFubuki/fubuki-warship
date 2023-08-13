package com.fubuki.warship.exception;

/**
 * 描述：     统一异常
 */
public class WarshipException extends Exception {

    private final Integer code;
    private final String message;

    public WarshipException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public WarshipException(WarshipExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
