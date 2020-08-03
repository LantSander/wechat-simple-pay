package com.example.pay.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@EnableConfigurationProperties(WxMiniAppAccount.class)
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "wxminiapp")
@Component
public class WxMiniAppAccount {
    private String appid;
    private String secret;

    //商户id
    private String mchId;

    //支付成功回调地址
    private String notifyUrl;

}
