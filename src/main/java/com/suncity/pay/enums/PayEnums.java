package com.suncity.pay.enums;

/**
 * 
 * @author Abbott
 *
 */
public enum PayEnums {
	NORMAL_STATUS("00","正常状态"),
	
	ABNORMAL_STATUS("01","状态不正常"),
	
	SIGN_MISMATCH_ERROR("02","签名不匹配"),
	;
	private String code;
	private String msg;
	
	
	private PayEnums(String code, String msg) {
		this.code = code;
		this.msg = msg;
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
	};
	
	
}
