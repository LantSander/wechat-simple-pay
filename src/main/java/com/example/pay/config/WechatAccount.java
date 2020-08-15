package com.example.pay.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@EnableConfigurationProperties(WechatAccount.class)
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "wxminiapp")
@Component
public class WechatAccount {
    private String appid;
    private String mchSecret;

    //商户id
    private String mchId;

    //支付成功回调地址
    private String notifyUrl;

    private String appSecret;

}
