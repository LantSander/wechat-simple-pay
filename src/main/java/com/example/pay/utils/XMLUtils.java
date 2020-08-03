package com.example.pay.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.Map;

public class XMLUtils {

    /*
    * map转为xml
    * */
    public static String mapToXml(Map<String,String> param){
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("xml");
            Iterator var4 = param.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var4.next();
                if (entry.getValue() != null) {
                    Element elm = root.addElement((String)entry.getKey());
                    elm.addText((String)entry.getValue());
                }
            }

            return document.asXML();
        } catch (Exception var7) {
            throw new RuntimeException("generate xml error", var7);
        }

    }

}
