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
import com.suncity.pay.model.AppPayModel;
import com.suncity.pay.service.PayService;
import com.suncity.pay.utils.ToolKit;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Controller
public class PayController {
	
	/**日志记录对象 */
	private static final Logger logger = Logger.getLogger(PayController.class);

	/**配置文件*/
	private static final String PROPERTIES = "config.properties";
	
	@Autowired
	private PayService payService;

	/**
	 * 发起支付
	 */
	@RequestMapping("/appPay")
	public String InitiatePay(Map<String, Object> map) {
		try {
			// 读取配置文件 , 将map参数返回页面
			payService.readfile(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取配置文件异常",e);
		}
		return "AppPay";
	}

	/**
	 * 确认支付
	 * @return
	 */
	@RequestMapping("/appConfirm")
	public String ConfirmPay(HttpServletRequest request) {
		//接收request 参数
		AppPayModel model = new AppPayModel(request);
		try {
			//进行
			String data = model.getData();
			String parameter = "data=" + data + "&merchNo="+ request.getParameter("merchNo") + "&version=" + request.getParameter("version");
			//发起支付请求
			String requestJson = ToolKit.request(request.getParameter("reqUrl"),parameter);
			JSONObject jsonResult = JSONObject.fromObject(requestJson);
			String stateCode = jsonResult.getString("stateCode");
			String msg = jsonResult.getString("msg");
			if (!stateCode.equals(PayEnums.NORMAL_STATUS.getCode())) {
				throw new PayException(PayEnums.ABNORMAL_STATUS,msg);
			}
			//取得签名
			String resultSign = jsonResult.getString("sign");
			jsonResult.remove("sign");
			//拼接 md5 密钥  进行MD5加密
			String splice = jsonResult.toString() + request.getParameter("key");
			String MD5String = ToolKit.MD5(splice, request.getParameter("charset"));
			//与签名进行匹配
			if (!MD5String.equals(resultSign)) {
				throw new PayException(PayEnums.SIGN_MISMATCH_ERROR);
			}
			//无误，支付地址
			String paymentUrl =  jsonResult.getString("qrcodeUrl");
			return "redirect:" + paymentUrl;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 订单查询
	 */
	@RequestMapping("/query")
	public String query(Map<String, Object> map) {
		try {
			payService.readfile(map);
		} catch (Exception e) {
			logger.error("订单查询 " + e);
		}
		return "Query";
	}

	/**
	 * 提交查询
	 */
	@RequestMapping("/submitQuery")
	public String submitQuery(HttpServletRequest request) {
		try {
			payService.submitQuery(request);
		} catch (IOException e) {
			logger.error("接口调用出现异常！", e);
		}
		return "Query";
	}

	/**
	 * 支付回显
	 */
	@RequestMapping("/callback") 
	public String callback(HttpServletRequest request) {
		return "Callback"; 
	}
	

	/**
	 * 支付回调
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/result")
	public void result(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 读取私钥
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES);
		// 加载属性列表
		Properties prop = new Properties();
		prop.load(inputStream);
		String privateKey = prop.getProperty("PRIVATE_KEY");
		String key = prop.getProperty("key");
		String charset = prop.getProperty("charset");

		String data = request.getParameter("data");
		byte[] result = ToolKit.decryptByPrivateKey(new BASE64Decoder().decodeBuffer(data), privateKey);
		String resultData = new String(result, charset);// 解密数据

		JSONObject jsonObj = JSONObject.fromObject(resultData);
		Map<String, String> metaSignMap = jsonToMap(jsonObj);
		String jsonStr = ToolKit.mapToJson(metaSignMap);
		String sign = ToolKit.MD5(jsonStr.toString() + key, charset);
		if (!sign.equals(jsonObj.getString("sign"))) {
			System.out.println("签名校验失败");
			throw new PayException(PayEnums.SIGN_MISMATCH_ERROR);
		}
		System.out.println("签名校验成功");
		// 回调成功返回0
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
		metaSignMap.put("netway", jsonObj.getString("netway"));
		metaSignMap.put("orderNum", jsonObj.getString("orderNum"));
		metaSignMap.put("amount", jsonObj.getString("amount"));
		metaSignMap.put("goodsName", jsonObj.getString("goodsName"));
		metaSignMap.put("payResult", jsonObj.getString("payResult"));// 支付状态
		metaSignMap.put("payDate", jsonObj.getString("payDate"));// yyyyMMddHHmmss
		return metaSignMap;
	}
}