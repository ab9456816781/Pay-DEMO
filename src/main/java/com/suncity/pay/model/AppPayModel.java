package com.suncity.pay.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.suncity.pay.utils.ToolKit;

import sun.misc.BASE64Encoder;

/**
 * 发起支付参数model
 * @author Terry
 */
public class AppPayModel
{
	/**
	 * 商户号
	 */
    private String merchNo;
    /**
     * md5签名密钥
     */
    private String key;
    /**
     * 请求地址
     */
    private String reqUrl;
    /**
     * 订单号
     */
    private String orderNum;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 编码方式
     */
    private String charset;
    
    /**
     * 随机数
     */
    private String random;
    /**
     * 网关
     */
    private String netway;
    /**
     * 金额
     */
    private String amount;
	
    /**
     * 商品名称
     */
    private String goodsName;
    
    /**
     * 回调地址
     */
    private String callBackUrl;
    /**
     * 回显地址
     */
    private String callBackViewUrl;

    /**
     * 構造器，讀取request對象中對應的參數，注入model
     * @param request 请求消息体对象
     */
    public AppPayModel(HttpServletRequest request)
    {
        super();
        this.merchNo = request.getParameter("merchNo");
        this.key = request.getParameter("key");
        this.reqUrl = request.getParameter("reqUrl");
        this.orderNum = request.getParameter("orderNum");
        this.version = request.getParameter("version");
        this.charset = request.getParameter("charset");
        this.random = request.getParameter("random");
        this.netway = request.getParameter("netway");
        this.amount = request.getParameter("amount");
        this.goodsName = request.getParameter("goodsName");
        this.callBackUrl = request.getParameter("callBackUrl");
        this.callBackViewUrl = request.getParameter("callBackViewUrl");
    }
    
    public String getData() throws IOException {
    	Map<String, String> metaSignMap = new TreeMap<String, String>();
    	metaSignMap.put("orderNum", orderNum);
		metaSignMap.put("version", version);
		metaSignMap.put("charset", charset);
		metaSignMap.put("random", random);

		// 商户号、网关代码、金额（分）、商品名称、回调地址、回显地址
		metaSignMap.put("merNo", merchNo);
		metaSignMap.put("netway", netway);
		metaSignMap.put("amount", amount);
		metaSignMap.put("goodsName",goodsName);
		metaSignMap.put("callBackUrl",callBackUrl);
		metaSignMap.put("callBackViewUrl",callBackViewUrl);
		
		// 参数列表转json字符串加签名密钥，使用MD5加密UTF-8编码生成签名，并将签名加入参数列表
		String metaSignJsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(metaSignJsonStr + key,charset);
		System.out.println("sign=" + sign);
		metaSignMap.put("sign", sign);
		// 公钥加密、BASE64位加密、URL编码加密并拼接商户号和版本号
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config.properties");
        // 加载属性列表
        Properties prop = new Properties();
        prop.load(inputStream);
        //读取公钥， 进行公钥加密
        String payKey = prop.getProperty("PAY_PUBLIC_KEY");
        //加密后返回 byte[] 显示不友好，将其转码
		byte[] dataStr = ToolKit.encryptByPublicKey(ToolKit.mapToJson(metaSignMap).getBytes(charset),payKey);
		//进行base64转码
		String param = new BASE64Encoder().encode(dataStr);
		//进行URLEncode转码
		return URLEncoder.encode(param, charset);
    }

}