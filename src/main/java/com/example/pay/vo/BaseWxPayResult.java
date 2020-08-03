//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.pay.vo;

import com.example.pay.exceptions.WxPayException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class BaseWxPayResult implements Serializable {
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
    private String xmlString;
    private transient Document xmlDoc;

    public static String fenToYuan(Integer fen) {
        return BigDecimal.valueOf(Double.valueOf((double) fen) / 100.0D).setScale(2, 4).toPlainString();
    }

    public static <T extends BaseWxPayResult> T fromXML(String xmlString, Class<T> clz) {
        try {
            BaseWxPayResult t = clz.newInstance();
            t.setXmlString(xmlString);
            Document doc = t.getXmlDoc();
            t.loadBasicXML(doc);
            t.loadXML(doc);
            return (T) t;
        } catch (Exception var4) {
            throw new RuntimeException("parse xml error", var4);
        }
    }

    protected abstract void loadXML(Document var1);

    private void loadBasicXML(Document d) {
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
    }

    public static Integer readXMLInteger(Node d, String tagName) {
        String content = readXMLString(d, tagName);
        return content != null && content.trim().length() != 0 ? Integer.parseInt(content) : null;
    }

    public static String readXMLString(Node d, String tagName) {
        if (!d.hasChildNodes()) {
            return null;
        } else {
            NodeList childNodes = d.getChildNodes();
            int i = 0;

            for (int j = childNodes.getLength(); i < j; ++i) {
                Node node = childNodes.item(i);
                if (tagName.equals(node.getNodeName())) {
                    if (!node.hasChildNodes()) {
                        return null;
                    }

                    return node.getFirstChild().getNodeValue();
                }
            }

            return null;
        }
    }


    public void checkResult() throws WxPayException {
        List<String> successStrings = Lists.newArrayList("SUCCESS", "");
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

            String errorMs = Joiner.on("，").skipNulls().join(this.returnCode == null ? null : String.format("返回代码：[%s]", this.returnCode), this.returnMsg == null ? null : String.format("返回信息：[%s]", this.returnMsg), this.resultCode == null ? null : String.format("结果代码：[%s]", this.resultCode), this.errCode == null ? null : String.format("错误代码：[%s]", this.errCode), this.errCodeDes == null ? null : String.format("错误详情：[%s]", this.errCodeDes), this.xmlString == null ? null : "微信返回的原始报文：\n" + this.xmlString);
            System.out.println("\n结果业务代码异常，返回结果：{},\n{}"+errorMsg.toString());
            throw new WxPayException(errorMs);
        }
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

    public static Integer readXMLInteger(Document d, String tagName) {
        String content = readXMLString(d, tagName);
        return content != null && content.trim().length() != 0 ? Integer.parseInt(content) : null;
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

    protected String getXmlValue(String... path) {
        Document doc = this.getXmlDoc();
        String expression = String.format("/%s//text()", Joiner.on("/").join(path));

        try {
            return (String) XPathFactory.newInstance().newXPath().compile(expression).evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException var5) {
            throw new RuntimeException("未找到相应路径的文本：" + expression);
        }
    }

    protected Integer getXmlValueAsInt(String... path) {
        String result = this.getXmlValue(path);
        return StringUtils.isBlank(result) ? null : Integer.valueOf(result);
    }


    public BaseWxPayResult() {
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public String getReturnMsg() {
        return this.returnMsg;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrCodeDes() {
        return this.errCodeDes;
    }

    public String getAppid() {
        return this.appid;
    }

    public String getMchId() {
        return this.mchId;
    }

    public String getSubAppId() {
        return this.subAppId;
    }

    public String getSubMchId() {
        return this.subMchId;
    }

    public String getNonceStr() {
        return this.nonceStr;
    }

    public String getSign() {
        return this.sign;
    }

    public String getXmlString() {
        return this.xmlString;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setXmlString(String xmlString) {
        this.xmlString = xmlString;
    }

    public void setXmlDoc(Document xmlDoc) {
        this.xmlDoc = xmlDoc;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseWxPayResult)) {
            return false;
        } else {
            BaseWxPayResult other = (BaseWxPayResult) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label155:
                {
                    Object this$returnCode = this.getReturnCode();
                    Object other$returnCode = other.getReturnCode();
                    if (this$returnCode == null) {
                        if (other$returnCode == null) {
                            break label155;
                        }
                    } else if (this$returnCode.equals(other$returnCode)) {
                        break label155;
                    }

                    return false;
                }

                Object this$returnMsg = this.getReturnMsg();
                Object other$returnMsg = other.getReturnMsg();
                if (this$returnMsg == null) {
                    if (other$returnMsg != null) {
                        return false;
                    }
                } else if (!this$returnMsg.equals(other$returnMsg)) {
                    return false;
                }

                Object this$resultCode = this.getResultCode();
                Object other$resultCode = other.getResultCode();
                if (this$resultCode == null) {
                    if (other$resultCode != null) {
                        return false;
                    }
                } else if (!this$resultCode.equals(other$resultCode)) {
                    return false;
                }

                label134:
                {
                    Object this$errCode = this.getErrCode();
                    Object other$errCode = other.getErrCode();
                    if (this$errCode == null) {
                        if (other$errCode == null) {
                            break label134;
                        }
                    } else if (this$errCode.equals(other$errCode)) {
                        break label134;
                    }

                    return false;
                }

                label127:
                {
                    Object this$errCodeDes = this.getErrCodeDes();
                    Object other$errCodeDes = other.getErrCodeDes();
                    if (this$errCodeDes == null) {
                        if (other$errCodeDes == null) {
                            break label127;
                        }
                    } else if (this$errCodeDes.equals(other$errCodeDes)) {
                        break label127;
                    }

                    return false;
                }

                label120:
                {
                    Object this$appid = this.getAppid();
                    Object other$appid = other.getAppid();
                    if (this$appid == null) {
                        if (other$appid == null) {
                            break label120;
                        }
                    } else if (this$appid.equals(other$appid)) {
                        break label120;
                    }

                    return false;
                }

                Object this$mchId = this.getMchId();
                Object other$mchId = other.getMchId();
                if (this$mchId == null) {
                    if (other$mchId != null) {
                        return false;
                    }
                } else if (!this$mchId.equals(other$mchId)) {
                    return false;
                }

                label106:
                {
                    Object this$subAppId = this.getSubAppId();
                    Object other$subAppId = other.getSubAppId();
                    if (this$subAppId == null) {
                        if (other$subAppId == null) {
                            break label106;
                        }
                    } else if (this$subAppId.equals(other$subAppId)) {
                        break label106;
                    }

                    return false;
                }

                Object this$subMchId = this.getSubMchId();
                Object other$subMchId = other.getSubMchId();
                if (this$subMchId == null) {
                    if (other$subMchId != null) {
                        return false;
                    }
                } else if (!this$subMchId.equals(other$subMchId)) {
                    return false;
                }

                label92:
                {
                    Object this$nonceStr = this.getNonceStr();
                    Object other$nonceStr = other.getNonceStr();
                    if (this$nonceStr == null) {
                        if (other$nonceStr == null) {
                            break label92;
                        }
                    } else if (this$nonceStr.equals(other$nonceStr)) {
                        break label92;
                    }

                    return false;
                }

                Object this$sign = this.getSign();
                Object other$sign = other.getSign();
                if (this$sign == null) {
                    if (other$sign != null) {
                        return false;
                    }
                } else if (!this$sign.equals(other$sign)) {
                    return false;
                }

                Object this$xmlString = this.getXmlString();
                Object other$xmlString = other.getXmlString();
                if (this$xmlString == null) {
                    return other$xmlString == null;
                } else return this$xmlString.equals(other$xmlString);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseWxPayResult;
    }

}
