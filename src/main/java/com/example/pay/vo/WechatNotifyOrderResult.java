package com.example.pay.vo;

import lombok.Data;
import lombok.ToString;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatNotifyOrderResult {
    @XmlElement(name = "promotion_detail")
    private String promotionDetail;
    @XmlElement(name = "device_info")
    private String deviceInfo;
    @XmlElement(name = "openid")
    private String openid;
    @XmlElement(name = "is_subscribe")
    private String isSubscribe;
    @XmlElement(name = "sub_openid")
    private String subOpenid;
    @XmlElement(name = "sub_is_subscribe")
    private String subIsSubscribe;
    @XmlElement(name = "trade_type")
    private String tradeType;
    @XmlElement(name = "bank_type")
    private String bankType;
    @XmlElement(name = "total_fee")
    private Integer totalFee;
    @XmlElement(name = "settlement_total_fee")
    private Integer settlementTotalFee;
    @XmlElement(name = "fee_type")
    private String feeType;
    @XmlElement(name = "cash_fee")
    private Integer cashFee;
    @XmlElement(name = "cash_fee_type")
    private String cashFeeType;
    @XmlElement(name = "coupon_fee")
    private Integer couponFee;
    @XmlElement(name = "coupon_count")
    private Integer couponCount;
    @XmlElement(name = "transaction_id")
    private String transactionId;
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    @XmlElement(name = "attach")
    private String attach;
    @XmlElement(name = "time_end")
    private String timeEnd;
    @XmlElement(name = "version")
    private String version;
    @XmlElement(name = "rate_value")
    private String rateValue;
    @XmlElement(name = "sign_type")
    private String signType;
    @XmlElement(name = "return_code")
    protected String returnCode;
    @XmlElement(name = "return_msg")
    protected String returnMsg;
    @XmlElement(name = "result_code")
    private String resultCode;
    @XmlElement(name = "err_code")
    private String errCode;
    @XmlElement(name = "err_code_des")
    private String errCodeDes;
    @XmlElement(name = "appid")
    private String appid;
    @XmlElement(name = "mch_id")
    private String mchId;
    @XmlElement(name = "sub_appid")
    private String subAppId;
    @XmlElement(name = "sub_mch_id")
    private String subMchId;
    @XmlElement(name = "nonce_str")
    private String nonceStr;
    @XmlElement(name = "sign")
    private String sign;


    public Map<String, String> buildSignMap() {
        Map<String, String> map = new HashMap();
        map.put("return_code", this.getReturnCode());
        map.put("return_msg", this.getReturnMsg());
        map.put("appid", this.getAppid());
        map.put("mch_id", this.getMchId());
        map.put("device_info", this.getDeviceInfo());
        map.put("nonce_str", this.getNonceStr());
        map.put("sign", this.getSign());
        map.put("result_code", this.getResultCode());
        map.put("err_code", this.getErrCode());
        map.put("err_code_des", this.getErrCodeDes());
        map.put("openid", this.getOpenid());
        map.put("is_subscribe", this.getIsSubscribe());
        map.put("trade_type", this.getTradeType());
        map.put("bank_type", this.getBankType());
        map.put("total_fee", String.valueOf(this.getTotalFee()));
        map.put("fee_type", this.getFeeType());
        map.put("cash_fee", String.valueOf(this.getCashFee()));
        map.put("cash_fee_type", this.getCashFeeType());
        map.put("coupon_fee", String.valueOf(this.getCouponFee()));
        map.put("coupon_count", String.valueOf(this.getCouponCount()));
        map.put("transaction_id", this.getTransactionId());
        map.put("out_trade_no", this.getOutTradeNo());
        map.put("attach", this.getAttach());
        map.put("time_end", this.getTimeEnd());
        return map;
    }


//
//    public static void main(String[] args) throws Exception {
//        String str = "<xml><appid><![CDATA[wxfb71b73289534a80]]></appid><attach><![CDATA[187]]></attach><bank_type><![CDATA[CMBC_CREDIT]]></bank_type><cash_fee><![CDATA[10]]></cash_fee><device_info><![CDATA[WEB]]></device_info><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1501438571]]></mch_id><nonce_str><![CDATA[4iyUaVvC7cCxMcL0UdE6yEtu5SA6Xp45]]></nonce_str><openid><![CDATA[odGdO5USvnGjtJIAktSulk5oKX-Q]]></openid><out_trade_no><![CDATA[W201907091244049624599]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[AADC5246AB379418CAD3073237F452D1]]></sign><time_end><![CDATA[20190709124458]]></time_end><total_fee>10</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000351201907091147515673]]></transaction_id></xml>";
//        NotifyOrderResult orderInfo = WechatPayXMLUtils.converToJavaBean(str, NotifyOrderResult.class);
//        System.out.println(orderInfo.toString());
//    }
}
