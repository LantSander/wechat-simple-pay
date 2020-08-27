package com.example.pay.service.impl;

import com.example.pay.config.WechatAccount;
import com.example.pay.constant.WechatUrlConstant;
import com.example.pay.enums.ResultEnum;
import com.example.pay.exceptions.WxPayException;
import com.example.pay.service.WechatPayService;
import com.example.pay.utils.*;
import com.example.pay.vo.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Autowired
    private WechatAccount wechatAccount;

    //退款申请循环次数初始值
    private int loop = 0;

    @Override
    public ResultVo unifiedorder(UnifiedOrderRequest unifiedOrderRequest) {
        unifiedOrderRequest.setAppid(wechatAccount.getAppid());
        unifiedOrderRequest.setMchId(wechatAccount.getMchId());
        unifiedOrderRequest.setMchSecret(wechatAccount.getMchSecret());
        unifiedOrderRequest.setNotifyUrl(wechatAccount.getNotifyUrl());
        //需要授权获取
        // unifiedOrderRequest.setOpenid("");

        //创建签名
        Map<String, String> prepareOrderMap = WechatPaySignUtils.buildPrepareOrderMap(unifiedOrderRequest);
        unifiedOrderRequest.setSign(WechatPaySignUtils.createSign(prepareOrderMap, unifiedOrderRequest.getMchSecret()));

        //组装请求参数,参数设置的步骤必须一致
        prepareOrderMap.put("mch_id", unifiedOrderRequest.getMchId());
        //必须排序后，最后再添加sign
        SortedMap sortedParmMap = new TreeMap<>(prepareOrderMap);
        sortedParmMap.put("sign", unifiedOrderRequest.getSign());
        //转为xml
        String requestXml = WechatPayXMLUtils.mapToXml(sortedParmMap);
        log.info("发起预支付请求参数={}", requestXml);

        try {
            String result = HttpUtils.doSimplePostByQueryString(WechatUrlConstant.unifiedorderUrl, requestXml);
            log.info("调用预下单支付返回原始结果={}", result);
            WeChatPayUnifiedorderResult payUnifiedOrderResult = WeChatPayUnifiedorderResult.fromXML(result);
            log.info("开始对微信预支付返回的内容校验----");
            payUnifiedOrderResult.checkResult(wechatAccount);

            //二次签名
            Map<String, String> sign2Map = payUnifiedOrderResult.sign2Map();
            String sign = WechatPaySignUtils.createSign(sign2Map, wechatAccount.getMchSecret());
            sign2Map.put("sign", sign);
            return ResultVOUtil.success(sign2Map);
        } catch (IOException | WxPayException e) {
            e.printStackTrace();
            log.error("调用微信支付预下单失败{}", e.getMessage());
            return ResultVOUtil.error(ResultEnum.UNIFIEDORDER_PAY_FALI.getCode(), ResultEnum.UNIFIEDORDER_PAY_FALI.getMessage());
        }

    }

    @Override
    public ResultVo refund(RefundRequest refundRequest) {
        loop++;
        if (loop > 2) {
            return ResultVOUtil.error(-2, "请求失败");
        }
        if (StringUtils.isBlank(refundRequest.getOutTradeNo()) && StringUtils.isBlank(refundRequest.getTransactionId())) {
            return ResultVOUtil.error(-1, "微信订单号或者商户订单不能为空");
        }
        if (!refundRequest.getRefundAccount().equals("REFUND_SOURCE_RECHARGE_FUNDS")
                && !refundRequest.getRefundAccount().equals("REFUND_SOURCE_UNSETTLED_FUNDS")) {
            return ResultVOUtil.error(-2, "RefundAccount输入有误");
        }

        refundRequest.setNotifyUrl(WechatUrlConstant.refundNotifyUrl);
        Map<String, String> signParamMap = refundRequest.signMap(wechatAccount.getAppid(), wechatAccount.getMchId());
        String sign = WechatPaySignUtils.createSign(signParamMap, wechatAccount.getMchSecret());
        signParamMap = new TreeMap<>(signParamMap);
        signParamMap.put("sign", sign);

        log.info("发起退款申请参数{}", signParamMap);

        String requestParamMxl = WechatPayXMLUtils.mapToXml(signParamMap);
        log.info("发起退款申请requestParamMxl={}", requestParamMxl);
        RefundResult refundResult = null;
        try {

            String result = HttpUtils.doPostBySllContext(HttpUtils.initSSLContext(wechatAccount.getCertPath(),
                    wechatAccount.getMchId()), WechatUrlConstant.refundUrl, requestParamMxl);
            log.info("发起退款请求成功result={}", result);

            refundResult = WechatPayXMLUtils.converToJavaBean(result, RefundResult.class);
            if (StringUtils.isNotBlank(refundResult.getSign())) {
                //对返回的结果校验签名，防止数据安全
                String signPre = WechatPaySignUtils.createSign(WechatPayXMLUtils.xmlToMap(result), wechatAccount.getMchSecret());
                if (!signPre.equals(refundResult.getSign())) {
                    log.error("验证签名失败");
                    return ResultVOUtil.error(-1, "验证签名失败");
                }
            }

            //校验返回的结果
            List<String> successStrings = Lists.newArrayList(new String[]{"SUCCESS", ""});
            if (!successStrings.contains(StringUtils.trimToEmpty(refundResult.getReturnCode()).toUpperCase()) || !successStrings.contains(StringUtils.trimToEmpty(refundResult.getResultCode()).toUpperCase())) {
                /*
                 * 返回代码：SUCCESS，返回信息：OK，结果代码：FAIL，错误代码：NOTENOUGH，错误详情：基本账户余额不足，请充值后重新发起
                 * 使用未结算资金退款失败，后使用可用余额退款再退款一次
                 * */
                if (StringUtils.isNotBlank(refundResult.getErrCode())&&refundResult.getErrCode().equals("NOTENOUGH")) {
                    refundRequest.setRefundAccount("REFUND_SOURCE_UNSETTLED_FUNDS");
                    refund(refundRequest);
                }

                StringBuilder errorMsg = new StringBuilder();
                if (refundResult.getReturnCode() != null) {
                    errorMsg.append("返回代码：").append(refundResult.getReturnCode());
                }
                if (refundResult.getReturnMsg() != null) {
                    errorMsg.append("，返回信息：").append(refundResult.getReturnMsg());
                }
                if (refundResult.getResultCode() != null) {
                    errorMsg.append("，结果代码：").append(refundResult.getResultCode());
                }
                if (refundResult.getErrCode() != null) {
                    errorMsg.append("，错误代码：").append(refundResult.getErrCode());
                }
                if (refundResult.getErrCodeDes() != null) {
                    errorMsg.append("，错误详情：").append(refundResult.getErrCodeDes());
                }
                log.error("结果业务代码异常，返回结果：{}", errorMsg.toString());
                return ResultVOUtil.error(-1, errorMsg.toString());
            }

            log.info("refundResult{}", refundRequest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("发起退款申请失败{}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发起微信退款请求失败");
        }
        //停止退款循环
        loop = 0;
        return ResultVOUtil.success(refundResult);
    }

    @Override
    public String notifyOrderResult(String xmlData) {
        log.info("接收到微信支付结果通知{}", xmlData);

        final WechatNotifyOrderResult unifiedOrderResult = WechatPayXMLUtils.converToJavaBean(xmlData, WechatNotifyOrderResult.class);

        //判断订单状态，如果已支付完成的，直接返回结果成功
        //--待写入订单状态校验逻辑

        //验证签名
        if (!WechatPaySignUtils.checkSign(unifiedOrderResult.buildSignMap(), wechatAccount.getMchSecret())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", unifiedOrderResult.toString());
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        } else if (!unifiedOrderResult.getReturnCode().equals("SUCCESS")) {
            log.info("微信支付不成功returnMsg={}", unifiedOrderResult.getReturnMsg());
            throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + unifiedOrderResult.getReturnMsg());
        }

        //校验返回的订单金额是否与商户侧的订单金额一致
        //---待写入订单金额校验逻辑

        //修改订单状态


        //处理后同步返回给微信
        String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        return resXml;
    }
}
