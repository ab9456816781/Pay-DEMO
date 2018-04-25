<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发起支付</title>
</head>
<body>
    <form action="./appConfirm" id="payForm" name="payForm" method="post">
        <div>
            <table class="style1">
                <tr>
                    <td class="style2">商户号:</td>
                    <td class="style3"><input type="text" name="merchNo"
                        value="${merchNo}"  size="80"/></td>
                </tr>
                <tr>
                	<td class="style2">签名密钥:</td>
                    <td class="style3"><input type="text" name="key"
                        value="${key}"  size="80"/></td>
                </tr>
                <tr>
                	<td class="style2">支付请求地址:</td>
                    <td class="style3"><input type="text" name="reqUrl"
                        value="${reqUrl}" size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">订单号码:</td>
                    <td class="style3"><input type="text" name="orderNum"
                        value="${orderNum}"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">版本号：</td>
                    <td class="style3"><input type="text" name="version"
                        value="V3.1.0.0"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">订单金额:</td>
                    <td class="style3"><input type="text" name="amount"
                        value="2001"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">编码方式:</td>
                    <td class="style3"><input type="text" name="charset"
                        value="UTF-8"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">随机数:</td>
                    <td class="style3"><input type="text" name="random"
                        value="${random}"  size="80"/></td>
                </tr>
               	<tr>
                    <td class="style2">商品名称:</td>
                    <td class="style3"><input type="text" name="goodsName"
                        value="${goodsName}"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">网关类型:</td>
                    <td class="style3"><select name="netway" id="netway">
                       	<option value="E_BANK_ICBC" selected="selected">中国工商银行</option>
                       	<option value="E_BANK_BOC">中国银行</option>
                       	<option value="E_BANK_ABC">中国农业银行</option>
                       	<option value="E_BANK_CCB">中国建设银行</option>
                       	<option value="E_BANK_BCM">交通银行</option>
                       	<option value="E_BANK_CMB">中国招商银行</option>
                       	<option value="E_BANK_CEB">中国光大银行</option>
                       	<option value="E_BANK_CMBC">中国民生银行</option>
                       	<option value="E_BANK_HXB">华夏银行</option>
                       	<option value="E_BANK_CIB">兴业银行</option>
                       	<option value="E_BANK_CNCB">中信银行</option>
                       	<option value="E_BANK_SPDB">上海浦东发展银行</option>
                       	<option value="E_BANK_PSBC">中国邮政储蓄银行</option>
                       	<!-- <option value="ZFB">支付宝</option>
	                       	<option value="ZFB_WAP">支付宝WAP</option>
	                       	<option value="WX">微信</option>
	                       	<option value="WX_H5">微信H5</option>
	                       	<option value="WX_WAP">微信WAP</option>
	                       	<option value="QQ">QQ钱包</option>
	                       	<option value="QQ_WAP">QQ钱包WAP</option>
	                       	<option value="JD">京东钱包</option>
	                       	<option value="BAIDU">百度钱包</option>
	                       	<option value="UNION_WALLET">银联钱包</option> -->
                    </select></td>
                </tr>
                <tr>
                    <td class="style2">回调地址:</td>
                    <td class="style3"><input type="text" name="callBackUrl"
                        value="${callBackUrl}"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">回显地址:</td>
                    <td class="style3"><input type="text" name="callBackViewUrl"
                        value="${callBackViewUrl}"  size="80"/></td>
                </tr>
                    <td class="style2">&nbsp;<input type="hidden" name="safetyKey" value="${safetyKey}" /></td>
                    <td class="style3"><input type="submit" value="提交" /></td>
                </tr>
                <tr>
                    <td class="style2">&nbsp;</td>
                    <td class="style3">&nbsp;</td>
                </tr>
                <tr>
                    <td class="style2">&nbsp;</td>
                    <td class="style3">&nbsp;</td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>