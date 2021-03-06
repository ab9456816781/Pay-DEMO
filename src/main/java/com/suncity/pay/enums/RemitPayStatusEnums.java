package com.suncity.pay.enums;
/**
 * 
 * @author  Abbott
 *
 */
public enum RemitPayStatusEnums {
	SUCCESS("00","成功"),
	PROCESSING("01","处理中"),
	ERROR("02","失败"),
	SIGN_ERROR("03","签名错误"),
	OTHER_ERROR("04","其他错误"),
	UNKNOWN("05","未知"),
	INITIALIZATION("06","初始化"),
	INTERNET_ERROR("50", "网络异常"),
	UNPAID("99","未支付"),
	REFUNDED("1000","已退款"),
	;
	private String code;
	private String msg;

	private RemitPayStatusEnums(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	 // 普通方法  
    public static String getMsg(String index) {  
        for (RemitPayStatusEnums c : RemitPayStatusEnums.values()) {  
            if (index.equals(c.getCode())) {  
                return c.msg;  
            }  
        }  
        return null;  
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
