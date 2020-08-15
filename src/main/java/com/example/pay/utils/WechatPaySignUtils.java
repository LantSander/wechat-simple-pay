package com.example.pay.utils;

import com.example.pay.vo.UnifiedOrderRequest;
import com.google.common.base.Joiner;
import com.lly835.bestpay.utils.MoneyUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class WechatPaySignUtils {

    public static String createSign(Map<String, String> params, String mchSecret) {
        Map<String, String> needSignMap = new HashMap<>();
        Iterator var4 = params.keySet().iterator();
        while (var4.hasNext()) {
            String key = (String) var4.next();
            String value = (String) params.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                needSignMap.put(key, value);
            }
        }
        SortedMap<String, String> sortedParam = new TreeMap<>(needSignMap);
        String joinParm = Joiner.on("&").withKeyValueSeparator("=").join(sortedParam);
        //末尾连接商户key
        joinParm += "&key=" + mchSecret;
        return Hex.encodeHexString(DigestUtils.md5(joinParm)).toUpperCase();
    }


    public static boolean checkSign(Map<String, String> params, String signKey) {
        String sign = createSign(params, signKey);
        return sign.equals(params.get("sign"));
    }


    public static Map<String, String> buildPrepareOrderMap(UnifiedOrderRequest unifiedOrderRequest) {
        Map<String, String> map = new HashMap();
        map.put("appid", unifiedOrderRequest.getAppid());
        map.put("mch_id", unifiedOrderRequest.getMchId());
        map.put("nonce_str", String.valueOf(System.currentTimeMillis()));
        map.put("notify_url", unifiedOrderRequest.getNotifyUrl());
        map.put("out_trade_no", unifiedOrderRequest.getOutTradeNo());
        map.put("total_fee", String.valueOf(MoneyUtil.Yuan2Fen(unifiedOrderRequest.getTotalFee())));
        map.put("body", unifiedOrderRequest.getBody());
        map.put("openid", unifiedOrderRequest.getOpenid());
        if (StringUtils.isNotBlank(unifiedOrderRequest.getAttach())) {
            map.put("attach", unifiedOrderRequest.getAttach());
        }
        if (StringUtils.isNotBlank(unifiedOrderRequest.getDetail())) {
            map.put("detail", unifiedOrderRequest.getDetail());
        }
        if (StringUtils.isNotBlank(unifiedOrderRequest.getTradeType())) {
            map.put("trade_type", unifiedOrderRequest.getTradeType());
        } else {
            map.put("trade_type", "JSAPI");
        }
        if (StringUtils.isNotBlank(unifiedOrderRequest.getSpbillCreateIp())) {
            map.put("spbill_create_ip", unifiedOrderRequest.getSpbillCreateIp());
        } else {
            map.put("spbill_create_ip", "8.8.8.8");
        }
        if (StringUtils.isNotBlank(unifiedOrderRequest.getAttach())) {
            map.put("attach", unifiedOrderRequest.getAttach());
        }
        return map;
    }


}
