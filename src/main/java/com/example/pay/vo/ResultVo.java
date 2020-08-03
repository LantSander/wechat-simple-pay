package com.example.pay.vo;

import lombok.Data;

@Data
public class ResultVo {

    private Integer code;
    private Object data;
    private String msg;

    public ResultVo(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
    public ResultVo(){}
}
