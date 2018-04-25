package com.suncity.pay.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.suncity.pay.enums.PayEnums;
import com.suncity.pay.enums.PayStatusEnums;
import com.suncity.pay.enums.RemitPayStatusEnums;
import com.suncity.pay.model.QueryModel;
import com.suncity.pay.model.RemitQueryModel;
import com.suncity.pay.utils.ToolKit;

import net.sf.json.JSONObject;

public class PayService {
	/**
	 * 配置文件名称
	 */
	private static final String CONFIG_FILE_NAME = "config.properties";
	
	/**
	 * 读取文件并且 封装参数
	 */
	public void readfile(Map<String, Object> map){
		/**
		 * 读取配置文件， 配置文件中配置 key 公钥 密钥 等信息
		 */
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
		Properties p = new Properties();
		try {
			//加载
			p.load(inputStream);
			// 迭代获取配置属性
			Iterator<String> it = p.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				map.put(key, p.getProperty(key));
			}
			//随机生成订单号
			String orderNum = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			orderNum += genRandomNum(3);
			map.put("orderNum", orderNum);
			// 生成随机数
			map.put("random", genRandomNum(4));
			//关闭流
			inputStream.close();
		} catch (IOException e1) {
		  e1.printStackTrace();
		}
	}

	/**
	 * 获取商户密钥
	 * 
	 * @return 从配置文件读取的商户密钥
	 * @author Terry
	 * @throws IOException
	 *             可能抛出IOException
	 */
	/*
	 * public String getSafetyKey() throws IOException { Map<String, String> map =
	 * new HashMap<>(); readConfigFile(map); return map.get("safetyKey"); }
	 */

	/**
	 * 提交查询接口
	 * 
	 * @param request
	 *            请求消息体
	 * @author Terry
	 * @throws IOException
	 *             可能抛出IOException
	 */
	public void submitQuery(HttpServletRequest request) throws IOException {
		//获取url
		String url = request.getParameter("queryUrl");
		QueryModel model = new QueryModel(request);
		//发送请求
		String msg = sendPost(url, model.getParamString());
		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(msg);
		String stateCode = resultJsonObj.getString("stateCode");
		String codeMsg = resultJsonObj.getString("msg");
		if (!stateCode.equals(PayEnums.NORMAL_STATUS.getCode())) {
			request.setAttribute("msg", codeMsg);
			return;
		}
		//获取sign 签名  ，校验签名
		String resultSign = resultJsonObj.getString("sign");
		resultJsonObj.remove("sign");
		String key = request.getParameter("key");
		String targetString = ToolKit.MD5(resultJsonObj.toString() + key,
				request.getParameter("charset"));
		if (!targetString.equals(resultSign)) {
			request.setAttribute("msg", PayEnums.SIGN_MISMATCH_ERROR.getMsg());
			System.out.println("签名校验失败");
			return;
		}
		//检查状态  返回信息
		String payStateCode = resultJsonObj.getString("payStateCode");
		String message = PayStatusEnums.getMsg(payStateCode);
		request.setAttribute("msg", message);
	}
	
	/**
	 * 提交代付查询
	 * @throws IOException 
	 */
	public void submitRemitQuery(HttpServletRequest request) throws IOException {
		//获取 url
		String url = request.getParameter("remitQueryUrl");
		String key = request.getParameter("key");
		String charset = request.getParameter("charset");
		RemitQueryModel model=new RemitQueryModel(request);
		//发送请求 
		String msg = sendPost(url, model.getParamString());
		
		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(msg);
		String stateCode = resultJsonObj.getString("stateCode");
		String codeMsg = resultJsonObj.getString("msg");
		if (!stateCode.equals(PayEnums.NORMAL_STATUS.getCode())) {
			request.setAttribute("msg",codeMsg);
			return;
		}
		String resultSign = resultJsonObj.getString("sign");
		resultJsonObj.remove("sign");
		String targetString = ToolKit.MD5(resultJsonObj.toString() + key,charset);
		if (!targetString.equals(resultSign)) {
			System.out.println("签名校验失败");
			request.setAttribute("msg", PayEnums.SIGN_MISMATCH_ERROR.getMsg());
			return ;
		}
		//检测支付状态
		String remitResult = resultJsonObj.getString("remitResult");
		String message = RemitPayStatusEnums.getMsg(remitResult);
		request.setAttribute("msg", message);
	}

	/**
	 * 发送HTTP POST请求
	 * 
	 * @param url
	 *            请求url
	 * @param param
	 *            请求参数
	 * @return 接口返回信息
	 * @throws IOException
	 *             可能抛出IOException
	 * @author Terry
	 */
	private String sendPost(String url, String param) throws IOException {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);

		// 设置请求方式
		conn.setRequestMethod("POST");

		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// 建立连接
		conn.connect();

		// 获取URLConnection对象对应的输出流
		out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

		// 发送请求参数
		out.write(param);

		// flush输出流的缓冲
		out.flush();

		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}

		// 关闭流
		out.close();
		in.close();

		// 返回结果
		return result;
	}

	/**
	 * 随机数字字符串，为了不用每次演示都手动更改订单号
	 * 
	 * @param pwd_len
	 *            指定长度
	 */
	private String genRandomNum(int pwd_len) {
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer num = new StringBuffer();
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(9));
			if (i >= 0 && i < str.length) {
				num.append(str[i]);
				count++;
			}
		}
		return num.toString();
	}
}