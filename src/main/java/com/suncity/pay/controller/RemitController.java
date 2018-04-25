package com.suncity.pay.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suncity.pay.Exception.PayException;
import com.suncity.pay.enums.PayEnums;
import com.suncity.pay.model.RemitModel;
import com.suncity.pay.service.PayService;
import com.suncity.pay.utils.ToolKit;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Controller
public class RemitController {

	/**配置文件*/
	private static final String PROPERTIES = "config.properties";
	
	@Autowired
	private PayService payService;

	/**
	 * 发起代付
	 */
	@RequestMapping("/remit")
	public String remit(Map<String, Object> map) {
		try {
			// 读取配置文件，通过map返回页面
			payService.readfile(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Remit";
	}

	/**
	 * 代付提交
	 */
	@RequestMapping("/remitConfirm")
	public String remitConfirm(HttpServletRequest request) throws IOException {
		//参数封装
		RemitModel remitModel = new RemitModel(request);
		String data = remitModel.getData();
		// 发起代付请求
		String parameter = "data=" + data + "&merchNo=" + request.getParameter("merchNo") + "&version=" + request.getParameter("version");
		String resultJsonStr = ToolKit.request(request.getParameter("reqRemitUrl"), parameter );
		// 检查状态
		JSONObject resultJsonObj = JSONObject.fromObject(resultJsonStr);
		String stateCode = resultJsonObj.getString("stateCode");
		if (stateCode.equals(PayEnums.NORMAL_STATUS.getCode())) {
			String resultSign = resultJsonObj.getString("sign");
			resultJsonObj.remove("sign"); 
			//MD5加密 并与签名进行 校验
			String targetString = ToolKit.MD5(resultJsonObj.toString() + request.getParameter("key"), request.getParameter("charset"));
			if (!targetString.equals(resultSign)) {
				System.out.println("签名校验失败");
				request.setAttribute("msg", PayEnums.SIGN_MISMATCH_ERROR.getMsg());
			}
		}
		//获取具体 信息 并返回页面
		request.setAttribute("msg", resultJsonObj.getString("msg"));
		return "Remit";
	}
	/**
	 * 发起查询
	 */
	@RequestMapping("/remitQuery")
	public String query(Map<String, Object> map) {
		try {
			// 读取配置文件，通过map返回页面
			payService.readfile(map);
		} catch (Exception e) {
			System.out.println("读取配置文件异常");
		}
		return "RemitQuery";
	}
	
	/**
	 * 提交代付查询
	 * @throws IOException 
	 */
	@RequestMapping("/submitRemitQuery")
	public String submitRemitQuery(HttpServletRequest request)  {
		try {
			payService.submitRemitQuery(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "RemitQuery";
	}
	/**
	 * 代付回调
	 * @throws IOException 
	 */
	@RequestMapping("/remitResult")
	public void remitResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 读取私钥
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES);
		// 加载属性列表
		Properties prop = new Properties();
		prop.load(inputStream);
		String PRIVATE_KEY = prop.getProperty("PRIVATE_KEY");
		String key = prop.getProperty("key");
		String charset = prop.getProperty("charset");
		
		String data = request.getParameter("data");

		byte[] result = ToolKit.decryptByPrivateKey(new BASE64Decoder().decodeBuffer(data),PRIVATE_KEY);
		String resultData = new String(result, charset);
		System.out.println("解密数据：" + resultData);

		JSONObject jsonObj = JSONObject.fromObject(resultData);
		Map<String, String> metaSignMap = jsonToMap(jsonObj);
		String jsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(jsonStr.toString() + key, charset);
		if (!sign.equals(jsonObj.getString("sign"))) {
			throw new PayException(PayEnums.SIGN_MISMATCH_ERROR);
		}
		System.out.println("签名校验成功");
		response.getOutputStream().write("0".getBytes());
	}
	
	/**
	 * 将值放入map
	 * Abbott
	 * @param jsonObj
	 * @return
	 */
	public Map<String, String> jsonToMap(JSONObject jsonObj){
		Map<String, String> metaSignMap = new TreeMap<String, String>();
		metaSignMap.put("merNo", jsonObj.getString("merNo"));
		metaSignMap.put("orderNum", jsonObj.getString("orderNum"));
		metaSignMap.put("amount", jsonObj.getString("amount"));
		metaSignMap.put("remitResult", jsonObj.getString("remitResult"));
		metaSignMap.put("remitDate", jsonObj.getString("remitDate"));// yyyyMMddHHmmss
		return metaSignMap;
	}
}
