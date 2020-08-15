package com.example.pay.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UnifiedOrderRequest {
    //订单编号
    @NotBlank(message = "订单编号不能为空", groups = prep.class)
    private String outTradeNo;
    //费用,元
    @NotNull(message = "费用不能为空", groups = prep.class)
    @Min(message = "费用不能小于0", value = 0, groups = prep.class)
    private Double totalFee;
    //商品简单描述
    @NotBlank(message = "商品描述不能为空", groups = prep.class)
    private String body;
    //商品详细描述
    private String detail;
    //调用微信支付API的机器IP
    private String spbillCreateIp = "8.8.8.8";
    //trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
    private String openid;
    private String sign;
    //数据，在查询API和支付通知中原样返回，可作为自定义参数使用
    private String attach;
    private String tradeType = "JSAPI";
    private String appid;
    private String mchId;
    private String mchSecret;
    private String notifyUrl;

    //用于分组，校验不同类型的请求规范
    public interface prep { }

}
