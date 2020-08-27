package com.example.pay.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@ToString
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class RefundResult {

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

    @XmlElement(name = "transaction_id")
    private String transactionId;
    @XmlElement(name = "out_trade_no")
    private String outTradeNo;
    @XmlElement(name = "out_refund_no")
    private String outRefundNo;
    @XmlElement(name = "refund_id")
    private String refundId;
    @XmlElement(name = "refund_fee")
    private Integer refundFee;
    @XmlElement(name = "settlement_refund_fee")
    private Integer settlementRefundFee;
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
    @XmlElement(name = "cash_refund_fee")
    private Integer cashRefundFee;
    @XmlElement(name = "coupon_refund_count")
    private Integer couponRefundCount;
    @XmlElement(name = "coupon_refund_fee")
    private Integer couponRefundFee;

}
