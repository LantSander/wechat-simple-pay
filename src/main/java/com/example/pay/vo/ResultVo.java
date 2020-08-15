package com.example.pay.vo;

import lombok.Data;

@Data
public class ResultVo<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
