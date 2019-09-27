package com.lanxin.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by aptx4869 on 2019/8/19.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(value = ArithmeticException.class)
    @ResponseBody
    public Result defaultErrorHandler(){

        return Result.faild();
    }

}
