//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.pay.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.w3c.dom.Document;

@XStreamAlias("xml")
public class WxPayUnifiedOrderResult extends BaseWxPayResult {
    @XStreamAlias("prepay_id")
    private String prepayId;
    @XStreamAlias("trade_type")
    private String tradeType;
    @XStreamAlias("mweb_url")
    private String mwebUrl;
    @XStreamAlias("code_url")
    private String codeURL;

    protected void loadXML(Document d) {
        this.prepayId = readXMLString(d, "prepay_id");
        this.tradeType = readXMLString(d, "trade_type");
        this.mwebUrl = readXMLString(d, "mweb_url");
        this.codeURL = readXMLString(d, "code_url");
    }

    public String getPrepayId() {
        return this.prepayId;
    }

    public String getTradeType() {
        return this.tradeType;
    }

    public String getMwebUrl() {
        return this.mwebUrl;
    }

    public String getCodeURL() {
        return this.codeURL;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public void setMwebUrl(String mwebUrl) {
        this.mwebUrl = mwebUrl;
    }

    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    public String toString() {
        return "WxPayUnifiedOrderResult(prepayId=" + this.getPrepayId() + ", tradeType=" + this.getTradeType() + ", mwebUrl=" + this.getMwebUrl() + ", codeURL=" + this.getCodeURL() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof WxPayUnifiedOrderResult)) {
            return false;
        } else {
            WxPayUnifiedOrderResult other = (WxPayUnifiedOrderResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label61: {
                    Object this$prepayId = this.getPrepayId();
                    Object other$prepayId = other.getPrepayId();
                    if (this$prepayId == null) {
                        if (other$prepayId == null) {
                            break label61;
                        }
                    } else if (this$prepayId.equals(other$prepayId)) {
                        break label61;
                    }

                    return false;
                }

                label54: {
                    Object this$tradeType = this.getTradeType();
                    Object other$tradeType = other.getTradeType();
                    if (this$tradeType == null) {
                        if (other$tradeType == null) {
                            break label54;
                        }
                    } else if (this$tradeType.equals(other$tradeType)) {
                        break label54;
                    }

                    return false;
                }

                Object this$mwebUrl = this.getMwebUrl();
                Object other$mwebUrl = other.getMwebUrl();
                if (this$mwebUrl == null) {
                    if (other$mwebUrl != null) {
                        return false;
                    }
                } else if (!this$mwebUrl.equals(other$mwebUrl)) {
                    return false;
                }

                Object this$codeURL = this.getCodeURL();
                Object other$codeURL = other.getCodeURL();
                if (this$codeURL == null) {
                    return other$codeURL == null;
                } else return this$codeURL.equals(other$codeURL);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof WxPayUnifiedOrderResult;
    }


    public WxPayUnifiedOrderResult() {
    }
}
