package com.example.pay.controller;


import com.example.pay.config.WechatAccount;
import com.example.pay.service.WechatPayService;
import com.example.pay.service.WechatService;
import com.example.pay.vo.ResultVo;
import com.example.pay.vo.UnifiedOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wx")
public class WechatSimplePayController {
    @Autowired
    private WechatAccount wechatAccount;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private WechatPayService wechatPayService;

    /*
     * 小程序登陆
     * 通过前端传来的code，获取用户的openid和sessionkey
     * return {"session_key":"8UC45z21TUiEE7u1U1tRNA==","openid":"o4DG_4sXAEeoaA6XCvFV2uLoP__o"}
     * */
    @GetMapping("/login")
    public ResultVo login(String code) {
        return wechatService.miniAppLogin(code);
    }

    //发起预支付 返回预支付id
    @PostMapping("/unifiedorder")
    public ResultVo unifiedorder(@Validated(UnifiedOrderRequest.prep.class) @RequestBody UnifiedOrderRequest unifiedOrderRequest) {
        return wechatPayService.unifiedorder(unifiedOrderRequest);
    }

    //微信支付结果通知
    @PostMapping("/notify/order")
    public String notifyOrderResult(@RequestBody String xmlData) {
        return wechatPayService.notifyOrderResult(xmlData);
    }

}
