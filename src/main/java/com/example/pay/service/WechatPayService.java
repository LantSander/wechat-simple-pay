package com.example.pay.service;

import com.example.pay.vo.RefundRequest;
import com.example.pay.vo.UnifiedOrderRequest;
import com.example.pay.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;

public interface WechatPayService {

    //创建预支付订单
    ResultVo unifiedorder(UnifiedOrderRequest unifiedOrderRequest);

    //退款申请
    ResultVo refund(RefundRequest refundRequest);

    //支付完成回调
    String notifyOrderResult(String xmlData);
}
