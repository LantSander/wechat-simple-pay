package com.example.pay.controller;


import com.example.pay.config.WxMiniAppAccount;
import com.example.pay.exceptions.WxPayException;
import com.example.pay.utils.HttpUtils;
import com.example.pay.utils.WxPaySignUtils;
import com.example.pay.utils.XMLUtils;
import com.example.pay.vo.BaseWxPayResult;
import com.example.pay.vo.PrepareOrder;
import com.example.pay.vo.ResultVo;
import com.example.pay.vo.WxPayUnifiedOrderResult;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@RequestMapping("/wx")
public class WechatSimplePayController {
    @Autowired
    private WxMiniAppAccount wxMiniAppAccount;


    /*
     * 通过前端传来的code，获取用户的openid和sessionkey
     * return {"session_key":"8UC45z21TUiEE7u1U1tRNA==","openid":"o4DG_4sXAEeoaA6XCvFV2uLoP__o"}
     * */
    @GetMapping("/login")
    public Object login(String code) throws IOException {
        String loginUrl = "https://api.weixin.qq.com/sns/jscode2session";

        Map<String, String> params = new HashMap(8);
        params.put("appid", wxMiniAppAccount.getAppid());
        params.put("secret", wxMiniAppAccount.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String paramJoin = Joiner.on("&").withKeyValueSeparator("=").join(params);
        loginUrl += "?" + paramJoin;

        String result = HttpUtils.doSimpleGet(loginUrl, null);
        return result;
    }

    //发起预支付 返回预支付id
    @PostMapping("/unifiedorder")
    public ResultVo unifiedorder(@Validated(PrepareOrder.prep.class) @RequestBody PrepareOrder prepareOrder) {
        return prepareOrder(prepareOrder);
    }

    //支付回调
    public void notifyOrderPayResult() {
        System.out.println("支付完成回调......");

    }





    //创建预支付订单
    public ResultVo prepareOrder(PrepareOrder prepareOrder) {
        prepareOrder.setAppid(wxMiniAppAccount.getAppid());
        prepareOrder.setMchId(wxMiniAppAccount.getMchId());
        prepareOrder.setNotifyUrl(wxMiniAppAccount.getNotifyUrl());
        //需要授权获取
        prepareOrder.setOpenid("");

        //创建签名
        Map<String, String> prepareOrderMap = WxPaySignUtils.buildPrepareOrderMap(prepareOrder);
        prepareOrder.setSign(WxPaySignUtils.createSign(prepareOrderMap, prepareOrder.getMchId()));

        //组装请求参数,参数设置的步骤必须一致
        prepareOrderMap.put("mch_id", prepareOrder.getMchId());
        //必须排序后，最后再添加sign
        SortedMap sortedParmMap = new TreeMap<>(prepareOrderMap);
        sortedParmMap.put("sign", prepareOrder.getSign());
        //转为xml
        String requestXml = XMLUtils.mapToXml(sortedParmMap);

        String result = null;
        try {
            result = HttpUtils.doSimplePostByQueryString( "https://api.mch.weixin.qq.com/pay/unifiedorder", requestXml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WxPayUnifiedOrderResult payUnifiedOrderResult = (WxPayUnifiedOrderResult) BaseWxPayResult.fromXML(result, WxPayUnifiedOrderResult.class);
        try {
            payUnifiedOrderResult.checkResult();
        } catch (WxPayException e) {
            System.out.println("--------支付失败！");
            return new ResultVo(2, e.getMessage(), null);
        }
        return new ResultVo(3, payUnifiedOrderResult, null);
    }


}
