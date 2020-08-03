package com.example.pay.utils;

import com.example.pay.vo.PrepareOrder;
import com.google.common.base.Joiner;
import com.lly835.bestpay.utils.MoneyUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WxPaySignUtils {


    //创建签名
    public static String createSign(Map<String, String> prepareOrderMap, String mchId) {
        SortedMap<String, String> sortedParam = new TreeMap<>(prepareOrderMap);
        String joinParm = Joiner.on("&").withKeyValueSeparator("=").join(sortedParam);
        //末尾连接商户key
        joinParm += "&key=" + mchId;
        return Hex.encodeHexString(DigestUtils.md5(joinParm)).toUpperCase();
    }


    public static Map<String, String> buildPrepareOrderMap(PrepareOrder prepareOrder) {
        Map<String, String> map = new HashMap();
        map.put("appid", prepareOrder.getAppid());
        map.put("mch_id", prepareOrder.getMchId());
        map.put("nonce_str", String.valueOf(System.currentTimeMillis()));
        map.put("notify_url", prepareOrder.getNotifyUrl());
        map.put("out_trade_no", prepareOrder.getOutTradeNo());
        map.put("total_fee", String.valueOf(MoneyUtil.Yuan2Fen(prepareOrder.getTotalFee())));
        map.put("body", prepareOrder.getBody());
        map.put("openid", prepareOrder.getOpenid());
        if (StringUtils.isNotBlank(prepareOrder.getAttach())) {
            map.put("attach", prepareOrder.getAttach());
        }
        if (StringUtils.isNotBlank(prepareOrder.getDetail())) {
            map.put("detail", prepareOrder.getDetail());
        }
        if (StringUtils.isNotBlank(prepareOrder.getTradeType())) {
            map.put("trade_type", prepareOrder.getTradeType());
        } else {
            map.put("trade_type", "JSAPI");
        }
        if (StringUtils.isNotBlank(prepareOrder.getSpbillCreateIp())) {
            map.put("spbill_create_ip", prepareOrder.getSpbillCreateIp());
        } else {
            map.put("spbill_create_ip", "8.8.8.8");
        }
        if (StringUtils.isNotBlank(prepareOrder.getAttach())) {
            map.put("attach", prepareOrder.getAttach());
        }
        return map;
    }


}
