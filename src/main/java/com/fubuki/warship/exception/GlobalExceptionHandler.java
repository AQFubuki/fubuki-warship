package com.fubuki.warship.exception;

import com.fubuki.warship.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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

    //拦截@Valid注解参数校验错误的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestResponse handleBindingResult(BindingResult result) {
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(WarshipExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse
                .error(WarshipExceptionEnum.REQUEST_PARAM_ERROR.getCode()
                        , list.toString());
    }
}
