package com.example.pay.vo;

import com.example.pay.config.WechatAccount;
import com.example.pay.exceptions.WxPayException;
import com.example.pay.utils.WechatPaySignUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.management.OperationsException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WeChatPayUnifiedorderResultVo {
    private String nonceStr;
    private String sign;
    private String prepayId;
    private String timeStamp;
    private String signType="MD5";
}
