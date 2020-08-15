package com.example.pay.service.impl;

import com.example.pay.config.WechatAccount;
import com.example.pay.constant.WechatUrlConstant;
import com.example.pay.enums.ResultEnum;
import com.example.pay.service.WechatService;
import com.example.pay.utils.HttpUtils;
import com.example.pay.utils.JsonUrils;
import com.example.pay.utils.ResultVOUtil;
import com.example.pay.vo.MiniAppLoginResult;
import com.example.pay.vo.MiniAppLoginSuccessVo;
import com.example.pay.vo.ResultVo;
import com.google.api.client.json.Json;
import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {
    @Autowired
    private WechatAccount wechatAccount;

    @Override
    public ResultVo miniAppLogin(String code){
        String loginUrl = WechatUrlConstant.loginUrl;
        Map<String, String> params = new HashMap();
        params.put("appid", wechatAccount.getAppid());
        params.put("secret", wechatAccount.getAppSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String paramJoin = Joiner.on("&").withKeyValueSeparator("=").join(params);
        loginUrl += "?" + paramJoin;
        try {
            String result = HttpUtils.doSimpleGet(loginUrl, null);
            log.info("登陆请求成功result={}", result);
            MiniAppLoginResult loginResult = JsonUrils.JsonStringToBean(result, MiniAppLoginResult.class);
            if (StringUtils.isNotBlank(loginResult.getOpenid())&&StringUtils.isNotBlank(loginResult.getSession_key())){
                //过滤必要字段返回前端
                MiniAppLoginSuccessVo loginSuccess=new MiniAppLoginSuccessVo();
                loginSuccess.setSessionKey(loginResult.getSession_key());
                loginSuccess.setOpenid(loginResult.getOpenid());
                return ResultVOUtil.success(loginSuccess);
            }
            return ResultVOUtil.error(ResultEnum.WECHAT_LOGIN_COD_FAIL.getCode(),ResultEnum.WECHAT_LOGIN_COD_FAIL.getMessage());
        } catch (IOException e) {
            log.error("调用微信登陆接口失败{}", e.getMessage());
            return ResultVOUtil.error(ResultEnum.WECHATLOGINFAIL.getCode(), ResultEnum.WECHATLOGINFAIL.getMessage());
        }
    }
}
