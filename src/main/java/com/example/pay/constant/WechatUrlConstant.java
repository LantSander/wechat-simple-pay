package com.example.pay.constant;
/*
* 微信小程序相关url
* */
public interface WechatUrlConstant {

    //小程序登陆获取openid
    String loginUrl="https://api.weixin.qq.com/sns/jscode2session";

    //发起预支付
    String unifiedorderUrl="https://api.mch.weixin.qq.com/pay/unifiedorder";


}
