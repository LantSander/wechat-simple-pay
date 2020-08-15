package com.example.pay.vo;

import com.example.pay.config.WechatAccount;
import com.example.pay.exceptions.WxPayException;
import com.example.pay.utils.WechatPaySignUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lly835.bestpay.utils.RandomUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ReusableMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class WeChatPayUnifiedorderResult {

    @XStreamAlias("return_code")
    protected String returnCode;
    @XStreamAlias("return_msg")
    protected String returnMsg;
    @XStreamAlias("result_code")
    private String resultCode;
    @XStreamAlias("err_code")
    private String errCode;
    @XStreamAlias("err_code_des")
    private String errCodeDes;
    @XStreamAlias("appid")
    private String appid;
    @XStreamAlias("mch_id")
    private String mchId;
    @XStreamAlias("sub_appid")
    private String subAppId;
    @XStreamAlias("sub_mch_id")
    private String subMchId;
    @XStreamAlias("nonce_str")
    private String nonceStr;
    @XStreamAlias("sign")
    private String sign;

    @XStreamAlias("prepay_id")
    private String prepayId;
    @XStreamAlias("trade_type")
    private String tradeType;
    @XStreamAlias("mweb_url")
    private String mwebUrl;
    @XStreamAlias("code_url")
    private String codeURL;


    private String xmlString;
    private transient Document xmlDoc;


    //将预支付请求结果转为WeChatPayUnifiedorderResult对象
    public static WeChatPayUnifiedorderResult fromXML(String xmlString) {
        try {
            WeChatPayUnifiedorderResult t = new WeChatPayUnifiedorderResult();
            t.setXmlString(xmlString);
            Document doc = t.getXmlDoc();
            t.loadXML(doc);
            return t;
        } catch (Exception var4) {
            throw new RuntimeException("parse xml error", var4);
        }
    }


    private Document getXmlDoc() {
        if (this.xmlDoc != null) {
            return this.xmlDoc;
        } else {
            this.xmlDoc = this.openXML(this.xmlString);
            return this.xmlDoc;
        }
    }


    protected Document openXML(String content) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setExpandEntityReferences(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            return factory.newDocumentBuilder().parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception var3) {
            throw new RuntimeException("非法的xml文本内容：\n" + this.xmlString, var3);
        }
    }


    private void loadXML(Document d) {
        this.returnCode = readXMLString(d, "return_code");
        this.returnMsg = readXMLString(d, "return_msg");
        this.resultCode = readXMLString(d, "result_code");
        this.errCode = readXMLString(d, "err_code");
        this.errCodeDes = readXMLString(d, "err_code_des");
        this.appid = readXMLString(d, "appid");
        this.mchId = readXMLString(d, "mch_id");
        this.subAppId = readXMLString(d, "sub_appid");
        this.subMchId = readXMLString(d, "sub_mch_id");
        this.nonceStr = readXMLString(d, "nonce_str");
        this.sign = readXMLString(d, "sign");

        this.prepayId = readXMLString(d, "prepay_id");
        this.tradeType = readXMLString(d, "trade_type");
        this.mwebUrl = readXMLString(d, "mweb_url");
        this.codeURL = readXMLString(d, "code_url");

    }

    public Map<String, String> sign2Map() {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String nonceStr = RandomUtil.getRandomStr();
        String packAge = "prepay_id=" + this.getPrepayId();
        String signType = "MD5";

        Map<String, String> map = new HashMap();
        map.put("appId", this.getAppid());
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packAge);
        map.put("signType", signType);
        return map;
    }



    public static String readXMLString(Document d, String tagName) {
        NodeList elements = d.getElementsByTagName(tagName);
        if (elements != null && elements.getLength() != 0) {
            Node node = elements.item(0).getFirstChild();
            return node == null ? null : node.getNodeValue();
        } else {
            return null;
        }
    }


    public void checkResult(WechatAccount wechatAccount) throws WxPayException {
        Map<String, String> map = this.toMap();
        if (this.getSign() != null && !WechatPaySignUtils.checkSign(map, wechatAccount.getMchSecret())) {
            log.error("校验结果签名失败，参数：{}", map);
            throw new WxPayException("参数格式校验错误！");
        } else {
            List<String> successStrings = Lists.newArrayList(new String[]{"SUCCESS", ""});
            if (!successStrings.contains(StringUtils.trimToEmpty(this.getReturnCode()).toUpperCase()) || !successStrings.contains(StringUtils.trimToEmpty(this.getResultCode()).toUpperCase())) {
                StringBuilder errorMsg = new StringBuilder();
                if (this.getReturnCode() != null) {
                    errorMsg.append("返回代码：").append(this.getReturnCode());
                }

                if (this.getReturnMsg() != null) {
                    errorMsg.append("，返回信息：").append(this.getReturnMsg());
                }

                if (this.getResultCode() != null) {
                    errorMsg.append("，结果代码：").append(this.getResultCode());
                }

                if (this.getErrCode() != null) {
                    errorMsg.append("，错误代码：").append(this.getErrCode());
                }

                if (this.getErrCodeDes() != null) {
                    errorMsg.append("，错误详情：").append(this.getErrCodeDes());
                }

                log.error("\n结果业务代码异常，返回结果：{},\n{}", map, errorMsg.toString());
                throw new WxPayException(errorMsg.toString());
            }
        }
    }


    public Map<String, String> toMap() {
        if (StringUtils.isBlank(this.xmlString)) {
            throw new RuntimeException("xml数据有问题，请核实！");
        } else {
            Map<String, String> result = Maps.newHashMap();
            Document doc = this.getXmlDoc();

            try {
                NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile("/xml/*").evaluate(doc, XPathConstants.NODESET);
                int len = list.getLength();

                for (int i = 0; i < len; ++i) {
                    result.put(list.item(i).getNodeName(), list.item(i).getTextContent());
                }

                return result;
            } catch (XPathExpressionException var6) {
                throw new RuntimeException("非法的xml文本内容：" + this.xmlString);
            }
        }
    }

}
