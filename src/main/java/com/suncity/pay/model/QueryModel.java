package com.suncity.pay.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.suncity.pay.utils.MD5Utils;
import com.suncity.pay.utils.ToolKit;

import sun.misc.BASE64Encoder;

/**
 * 订单查询参数model
 * 
 * @author Terry
 */
public class QueryModel {
	
	/**
	 * md5签名密钥
	 */
	private String key;
	/**
	 * 订单号
	 */
	private String orderNum;
	/**
	 * 支付时间
	 */
	private String payDate;
	/**
	 * 商户号
	 */
	private String merNo;
	/**
	 * 网关
	 */
	private String netway;
	/**
	 * 订单金额
	 */
	private String amount;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 编码方式
	 */
	private String charset;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 查询地址
	 */
	private String queryUrl;

	/**
	 * 構造器，讀取request對象中對應的參數，注入model
	 * 
	 * @param request
	 *            请求消息体对象
	 */
	public QueryModel(HttpServletRequest request) {
		super();
		this.amount = request.getParameter("amount");
		this.goodsName = request.getParameter("goodsName");
		this.merNo = request.getParameter("merNo");
		this.netway = request.getParameter("netway");
		this.orderNum = request.getParameter("orderNum");
		this.payDate = request.getParameter("payDate");
		this.key = request.getParameter("key");
		this.version = request.getParameter("version");
		this.queryUrl = request.getParameter("queryUrl");
		this.charset = request.getParameter("charset");
	}

	/**
	 * 根據當前對象屬性生成簽名
	 * 
	 * @return 返回參數生成簽名
	 * @author Terry
	 */
	public String getSign() {
		return MD5Utils.enCode(toString());
	}

	public String getData() throws IOException {
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("orderNum", orderNum);
		metaSignMap.put("payDate", payDate);
		metaSignMap.put("merNo", merNo);
		metaSignMap.put("netway", netway);// WX:微信支付,ZFB:支付宝支付
		metaSignMap.put("amount", amount);// 单位:分
		metaSignMap.put("goodsName", goodsName);// 商品名称：20位

		String metaSignJsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(metaSignJsonStr + key, charset);// 32位
		System.out.println("sign=" + sign); // 英文字母大写
		metaSignMap.put("sign", sign);

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("config.properties");
		// 加载属性列表
		Properties prop = new Properties();
		prop.load(inputStream);
		String payKey = prop.getProperty("PAY_PUBLIC_KEY");
		byte[] dataStr = ToolKit.encryptByPublicKey(ToolKit.mapToJson(metaSignMap).getBytes(charset), payKey);
		String param = new BASE64Encoder().encode(dataStr);
		return URLEncoder.encode(param, charset);
	}

	/**
	 * 获取接口请求参数串
	 * 
	 * @return 返回请求查询接口的参数串
	 * @author Terry
	 * @throws IOException
	 */
	public String getParamString() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("data=").append(getData());
		sb.append("&merchNo=").append(merNo);
		sb.append("&version=").append(version);
		return sb.toString();
	}
}