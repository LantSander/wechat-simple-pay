package com.example.pay.exceptions;

public class WxPayException  extends Exception{

    public WxPayException(String errorMsg){
        super(errorMsg);
    }


}
