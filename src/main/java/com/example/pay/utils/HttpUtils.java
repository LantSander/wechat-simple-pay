package com.example.pay.utils;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.IOUtils;
import com.google.gson.Gson;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送Http请求工具类，底层使用google的高性能http组件
 */
public class HttpUtils
{
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String POST_DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    /**
     * 发送GET请求，返回HttpResponse对象，可用于获取详细返回参数
     *
     * @param requestUrl 请求地址
     * @param paramMap   请求参数Map，Map的key会作为参数名，value作为参数值，value会做URLEncoder编码
     * @return HttpResponse对象
     * @throws IOException
     */
    public static HttpResponse doGet(String requestUrl, Map<String, String> paramMap) throws IOException
    {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl genericUrl = new GenericUrl(requestUrl + buildParams(paramMap));
        return requestFactory.buildGetRequest(genericUrl).execute();
    }

    /**
     * 发送GET请求，返回响应字符串
     *
     * @param requestUrl 请求地址
     * @param paramMap   请求参数Map，Map的key会作为参数名，value作为参数值，value会做URLEncoder编码
     * @return 响应的字符串
     * @throws IOException
     */
    public static String doSimpleGet(String requestUrl, Map<String, String> paramMap) throws IOException
    {
        return doGet(requestUrl, paramMap).parseAsString();
    }

    /**
     * 发送POST请求，返回HttpResponse对象，可用于获取详细返回参数
     *
     * @param requestUrl 请求地址
     * @param paramMap   请求参数Map，Map的key会作为参数名，value作为参数值，value会做URLEncoder编码
     * @return HttpResponse对象
     * @throws IOException
     */
    public static HttpResponse doPost(String requestUrl, Map<String, String> paramMap) throws IOException
    {
        String queryString = buildParams(paramMap);
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl genericUrl = new GenericUrl(requestUrl);
        HttpContent httpContent = new ByteArrayContent(POST_DEFAULT_CONTENT_TYPE, queryString.getBytes(DEFAULT_CHARSET));
        return requestFactory.buildPostRequest(genericUrl, httpContent).execute();
    }

    public static String  doSimplePostByQueryString(String requestUrl, String queryString)  throws IOException
    {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl genericUrl = new GenericUrl(requestUrl);
        HttpContent httpContent = new ByteArrayContent(POST_DEFAULT_CONTENT_TYPE, queryString.getBytes(DEFAULT_CHARSET));
        return parseAsStringUTF8(requestFactory.buildPostRequest(genericUrl, httpContent).execute());
    }


    //将请求返回的结果，以utf8方式解析，防止中文乱吗问题
    public static String parseAsStringUTF8(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getContent();
        if (content == null) {
            return "";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(content, out);
            return out.toString("UTF-8");
        }
    }

    /**
     * json参数方式POST提交,请求体是JSON格式
     * @param url
     * @param params   请求参数Map，Map会转为JSON格式发送请求
     * @return
     */
    public static String  doSimplePostBodyJson(String requestUrl, Map<String,String> params) throws IOException {
        HttpContent httpContent = new ByteArrayContent("application/json",
                new Gson().toJson(params).getBytes(Charsets.UTF_8));
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        GenericUrl genericUrl = new GenericUrl(requestUrl);
        return requestFactory.buildPostRequest(genericUrl, httpContent).execute().parseAsString();
    }

    /**
     * 发送POST请求，返回响应字符串
     *
     * @param requestUrl 请求地址
     * @param paramMap   请求参数Map，Map的key会作为参数名，value作为参数值，value会做URLEncoder编码
     * @return 响应的字符串
     * @throws IOException
     */
    public static String doSimplePost(String requestUrl, Map<String, String> paramMap) throws IOException
    {
        return doPost(requestUrl, paramMap).parseAsString();
    }

    private static String buildParams(Map<String, String> paramMap)
    {
        String queryString = "";
        if (paramMap != null && !paramMap.isEmpty())
        {
            try
            {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entrySet : paramMap.entrySet())
                {
                    sb.append(entrySet.getKey())
                      .append("=")
                      .append(URLEncoder.encode(entrySet.getValue(), DEFAULT_CHARSET))
                      .append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
                queryString = sb.toString();
            }
            catch (UnsupportedEncodingException e)
            {
                // do nothing
            }
        }
        return queryString;
    }

    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        map.put("symbol","SZ000789");
        map.put("type","all");
        map.put("is_detail","true");
        map.put("count","5");
        map.put("timestamp", String.valueOf(new Date().getTime()));
        String url="https://stock.xueqiu.com/v5/stock/finance/cn/indicator.json";
        String res = null;
        try {
            res = doSimpleGet(url, map);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(res);
    }

//
//    public static void main(String[] args)
//    {
//
//        Document doc = DocumentHelper.createDocument();
//        Element RequestOrder = doc.addElement("RequestOrder");
//        Element clientID = RequestOrder.addElement("clientID");
//        clientID.setText("K21000119");
//        Element logisticProviderID = RequestOrder.addElement("logisticProviderID");
//        logisticProviderID.setText("YTO");
//        Element customerId = RequestOrder.addElement("customerId");
//        customerId.setText("K21000119");
//        Element txLogisticID = RequestOrder.addElement("txLogisticID");
//        txLogisticID.setText("K21000119-1111111111");
//        Element orderType = RequestOrder.addElement("orderType");
//        orderType.setText("1");
//        Element serviceType = RequestOrder.addElement("serviceType");
//        serviceType.setText("1");
//        Element itemName = RequestOrder.addElement("itemName");
//        itemName.setText("UR2018秋冬新品青春女装格纹个性休闲翻领气质短外套YL42S1BE2001 蓝色格子 S");
//        Element number = RequestOrder.addElement("number");
//        number.setText("1");
//        Element sender = RequestOrder.addElement("sender");
//        Element name = sender.addElement("name");
//        name.setText("上海无限度广场店");
//        Element mobile = sender.addElement("mobile");
//        mobile.setText("13900000001");
//        Element prov = sender.addElement("prov");
//        prov.setText("上海市");
//        Element city = sender.addElement("city");
//        city.setText("上海市,黄浦区");
//        Element address = sender.addElement("address");
//        address.setText("淮海中路138号上海无限度广场104&105&202室");
//        Element receiver = RequestOrder.addElement("receiver");
//        name = receiver.addElement("name");
//        name.setText("张三");
//        mobile = receiver.addElement("mobile");
//        mobile.setText("13900000000");
//        prov = receiver.addElement("prov");
//        prov.setText("江苏省");
//        city = receiver.addElement("city");
//        city.setText("无锡市,滨湖区");
//        address = receiver.addElement("address");
//        address.setText("蠡湖大道2008号蠡湖商务园45-46");
//        String requestXML = RequestOrder.asXML();
//        Map<String, String> paramMap = new HashMap<>();
//        String queryString = null;
//        try
//        {
//            String temp = requestXML + "u2Z1F7Fh";
////            String temp2 = "<order></order>123456";
//            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
//            byte[] abyte0 = messagedigest.digest(temp.getBytes("UTF-8"));
////            byte[] abyte2 = messagedigest.digest(temp2.getBytes("UTF-8"));
//            temp = new String(Base64.encodeBase64(abyte0));
////            temp2 = new String(Base64.encodeBase64(abyte2));
//            StringBuilder sb = new StringBuilder();
//            sb.append("clientId=").append(URLEncoder.encode("K21000119", "UTF-8"));
//            sb.append("&logistics_interface=").append(URLEncoder.encode(requestXML, "UTF-8"));
//            sb.append("&data_digest=").append(URLEncoder.encode(temp, "UTF-8"));
//            queryString = sb.toString();
//            paramMap.put("clientId", "K21000119");
//            paramMap.put("type", "offline");
//            paramMap.put("logistics_interface", requestXML);
//            paramMap.put("data_digest", temp);
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            e.printStackTrace();
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            String response = doSimplePost("http://58.32.246.71:8000/CommonOrderModeBPlusServlet.action", paramMap);
//            System.out.println(response);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
}
