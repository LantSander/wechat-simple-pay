package com.example.pay.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),
    WECHATLOGINFAIL(1,"微信登陆失败"),
    WECHAT_LOGIN_COD_FAIL(3,"微信登陆失败，code已经失效"),
    UNIFIEDORDER_PAY_FALI(4,"发起预支付失败");



    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
