package com.example.pay.service;

import com.example.pay.vo.ResultVo;

public interface WechatService {

    //小程序登陆
     ResultVo miniAppLogin(String code);


}
