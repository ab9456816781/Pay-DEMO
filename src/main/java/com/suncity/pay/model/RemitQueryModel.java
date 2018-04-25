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

public class RemitQueryModel {
	/**
	 * 订单号
	 */
	private String orderNum;
	/**
	 * 代付时间
	 */
	private String remitDate;
	/**
	 * 商户号
	 */
	private String merNo;
	/**
	 * 金额
	 */
	private String amount;
	/**
	 * 编码方式
	 */
	private String charset;
	/**
	 * 密钥
	 */
	private String key;
	/**
	 * 版本号
	 */
	private String version;
	
	public RemitQueryModel(HttpServletRequest request) {
		super();
		this.orderNum = request.getParameter("orderNum");
		this.remitDate = request.getParameter("remitDate");
		this.merNo = request.getParameter("merNo");
		this.amount = request.getParameter("amount");
		this.charset = request.getParameter("charset");
		this.key = request.getParameter("key");
		this.version = request.getParameter("version");
	}
	
	public String getData() throws IOException {
		// 订单号、订单发起日期、赏花后、金额(分)
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("orderNum",orderNum);
		metaSignMap.put("remitDate", remitDate);
		metaSignMap.put("merNo", merNo);
		metaSignMap.put("amount",amount);

		// 签名
		String metaSignJsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(metaSignJsonStr + key,charset);
		System.out.println("sign=" + sign);
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
		return URLEncoder.encode(param,charset);
	}
	public String getParamString() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("data=").append(getData());
		sb.append("&merchNo=").append(merNo);
		sb.append("&version=").append(version);
		return sb.toString();
	}
}
