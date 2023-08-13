package com.fubuki.warship.exception;

import com.fubuki.warship.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：     处理统一异常的handler
 */
@ControllerAdvice //注解，用于拦截异常
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //系统异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception: ", e);
        return ApiRestResponse.error(WarshipExceptionEnum.SYSTEM_ERROR);
    }

    //业务异常
    @ExceptionHandler(WarshipException.class)
    @ResponseBody
    public Object handleWarshipException(WarshipException e) {
        log.error("WarshipException: ", e);
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }
}
