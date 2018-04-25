package com.suncity.pay.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.suncity.pay.utils.ToolKit;

import sun.misc.BASE64Encoder;

public class RemitModel {

	/**
	 * 商户号
	 */
	private String merchNo;
	
	/**
	 * 签名密钥
	 */
	private String key;
	/**
	 * 代付请求地址
	 */
	private String reqRemitUrl;
	/**
	 * 订单号
	 */
	private String orderNum;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 订单金额
	 */
	private String amount;
	/**
	 * 编码方式
	 */
	private String charset;
	/**
	 * 网关类型
	 */
	private String bankCode;
	/**
	 * 户名
	 */
	private String bankAccountName;
	/**
	 * 卡号
	 */
	private String bankAccountNo;
	/**
	 * 回调地址
	 */
	private String callBackUrl;
	
	public RemitModel(HttpServletRequest request) {
		super();
		this.merchNo = request.getParameter("merchNo");
		this.key = request.getParameter("key");
		this.reqRemitUrl = request.getParameter("reqRemitUrl");
		this.orderNum = request.getParameter("orderNum");
		this.version = request.getParameter("version");
		this.amount = request.getParameter("amount");
		this.charset = request.getParameter("charset");
		this.bankCode = request.getParameter("bankCode");
		this.bankAccountName = request.getParameter("bankAccountName");
		this.bankAccountNo = request.getParameter("bankAccountNo");
		this.callBackUrl = request.getParameter("remitCallBackUrl");
	}
	
	public String getData() throws IOException {
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("orderNum", orderNum);
		metaSignMap.put("version", version);
		metaSignMap.put("charset", charset);

		// 需要经常修改的参数
		// 银行代码、商户号、银行账户开户名、卡号、金额（分）、支付结果通知地址
		metaSignMap.put("bankCode", bankCode);
		metaSignMap.put("merNo", merchNo);
		metaSignMap.put("bankAccountName", bankAccountName);
		metaSignMap.put("bankAccountNo", bankAccountNo);
		metaSignMap.put("amount", amount);
		metaSignMap.put("callBackUrl", callBackUrl);
		// 生成参数加入参数列表
		String metaSignJsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(metaSignJsonStr + key,charset);
		metaSignMap.put("sign", sign);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config.properties");
        // 加载属性列表
        Properties prop = new Properties();
        prop.load(inputStream);
        String remitPublicKey = prop.getProperty("REMIT_PUBLIC_KEY");
		// 数据公钥加密
		byte[] dataStr = ToolKit.encryptByPublicKey(ToolKit.mapToJson(metaSignMap).getBytes(charset),remitPublicKey);
		String param = new BASE64Encoder().encode(dataStr);
		return URLEncoder.encode(param, charset);
	}
}
