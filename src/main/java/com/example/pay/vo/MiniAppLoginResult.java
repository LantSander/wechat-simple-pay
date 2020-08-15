package com.example.pay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MiniAppLoginResult {
    private String session_key;
    private String openid;
    private String errcode;
    private String errmsg;
}
