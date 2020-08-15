package com.example.pay.service.impl;

import com.example.pay.config.WechatAccount;
import com.example.pay.constant.WechatUrlConstant;
import com.example.pay.enums.ResultEnum;
import com.example.pay.exceptions.WxPayException;
import com.example.pay.service.WechatPayService;
import com.example.pay.utils.HttpUtils;
import com.example.pay.utils.ResultVOUtil;
import com.example.pay.utils.WechatPaySignUtils;
import com.example.pay.utils.WechatPayXMLUtils;
import com.example.pay.vo.*;
import com.lly835.bestpay.service.impl.WxPaySignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Autowired
    private WechatAccount wechatAccount;


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
            Map<String,String> sign2Map=payUnifiedOrderResult.sign2Map();
            String sign = WechatPaySignUtils.createSign(sign2Map, wechatAccount.getMchSecret());
            sign2Map.put("sign",sign);
            return ResultVOUtil.success(sign2Map);
        } catch (IOException | WxPayException e) {
            e.printStackTrace();
            log.error("调用微信支付预下单失败{}", e.getMessage());
            return ResultVOUtil.error(ResultEnum.UNIFIEDORDER_PAY_FALI.getCode(), ResultEnum.UNIFIEDORDER_PAY_FALI.getMessage());
        }

    }

    @Override
    public String notifyOrderResult(String xmlData) {
        log.info("接收到微信支付结果通知{}",xmlData);

        final WechatNotifyOrderResult unifiedOrderResult = WechatPayXMLUtils.converToJavaBean(xmlData, WechatNotifyOrderResult.class);

        //判断订单状态，如果已支付完成的，直接返回结果成功
           //--待写入订单状态校验逻辑

        //验证签名
       if(!WechatPaySignUtils.checkSign(unifiedOrderResult.buildSignMap(),wechatAccount.getMchSecret())){
           log.error("【微信支付异步通知】签名验证失败, response={}", unifiedOrderResult.toString());
           throw new RuntimeException("【微信支付异步通知】签名验证失败");
       } else if (!unifiedOrderResult.getReturnCode().equals("SUCCESS")) {
           log.info("微信支付不成功returnMsg={}",unifiedOrderResult.getReturnMsg());
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
