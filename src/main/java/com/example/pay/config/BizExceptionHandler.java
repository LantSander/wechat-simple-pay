package com.example.pay.config;


import com.example.pay.vo.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常统一处理
 */
@RestControllerAdvice
public class BizExceptionHandler {

    /**
     * 用于处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        //返回主要错误信息
        resultVo.setMsg(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        return resultVo;
    }
}
