package com.example.pay.vo;

import com.lly835.bestpay.utils.MoneyUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class RefundRequest {

    /*
     * 退款资金来源
     * 1.未结算资金退款：从用户支付的未结算（即钱微信还没结算给商家账号）的订单退款REFUND_SOURCE_UNSETTLED_FUNDS（默认使用方式）
     * 2.可用余额退款：从商户账号可用的余额退款  REFUND_SOURCE_RECHARGE_FUNDS
     * 3.结论：如果一个已经支付的订单，过了很长一段时间，微信已经结账把钱转给商户账号，那么此时用户申请退款，并且使用未结算资金退款方式会报错。此时应该选择使用可用余额退款方式退款。
     * */
    public static final String[] refundAccountState = new String[]{"REFUND_SOURCE_RECHARGE_FUNDS", "REFUND_SOURCE_UNSETTLED_FUNDS"};
    private String refundAccount = "REFUND_SOURCE_UNSETTLED_FUNDS";

    //微信订单号，与商户订单号二选一，如果同时存在优先级：transaction_id> out_trade_no
    private String transactionId;

    //商户订单号，与微信订单号二选一，如果同时存在优先级：transaction_id> out_trade_no
    private String outTradeNo;

    /*
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * */
    private String outRefundNo;

    //订单金额
    @NotNull(message = "订单总金额", groups = prod.class)
    private Double totalFee;

    //退款金额
    @NotNull(message = "退款金额不能为空", groups = prod.class)
    private Double refundFee;

    //退款原因
    private String refundDesc;

    //退款结果通知url
    private String notifyUrl;

    public interface prod { }

    //签名需要的参数
    public Map<String, String> signMap(String appid, String mchId) {
        Map<String, String> map = new HashMap(8);
        map.put("appid", appid);
        map.put("mch_id", mchId);
        map.put("nonce_str", String.valueOf(System.currentTimeMillis()));

        if (StringUtils.isNotBlank(transactionId)) {
            map.put("transaction_id", this.transactionId);
        }
        if (StringUtils.isNotBlank(outTradeNo)) {
            map.put("out_trade_no", this.outTradeNo);
        }

        if (StringUtils.isBlank(this.outRefundNo)) {
            this.outRefundNo = UUID.randomUUID().toString().replace("-", "");
        }

        map.put("out_refund_no", this.outRefundNo);
        map.put("total_fee", String.valueOf(MoneyUtil.Yuan2Fen(totalFee)));
        map.put("refund_fee", String.valueOf(MoneyUtil.Yuan2Fen(refundFee)));
        map.put("refund_account", this.refundAccount);
        if (StringUtils.isNotBlank(this.refundDesc)) {
            map.put("refund_desc", this.refundDesc);
        }
        if (StringUtils.isNotBlank(this.notifyUrl)) {
            map.put("notify_url", this.notifyUrl);
        }
        return map;
    }


}
