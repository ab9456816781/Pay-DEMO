package com.suncity.pay.Exception;

import com.suncity.pay.enums.PayEnums;
/**
 * 定义exception类
 * @author Abbott
 *
 */
public class PayException extends RuntimeException{

	private String code;

	private String msg;
	
	public PayException(String code) {
		super();
		this.code = code;
	}

	public PayException(PayEnums payEnums) {
		super(payEnums.getMsg());
		this.code = payEnums.getCode();
	}

	public PayException(PayEnums payEnums,String msg) {
		super(msg);
		this.code = payEnums.getCode();
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
