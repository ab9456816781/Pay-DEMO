<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单查询</title>
</head>
<body>
<%
    Object msg = request.getAttribute("msg");
%>
<c:if test="${ msg != null }">
<h1 style="color:red;"><%= msg %></h1>
</c:if>
	<form method="post" action="./submitQuery" id="form1">
		<div>
			<table class="style1">
				<tr>
					<td class="style2">签名密钥:</td>
					<td class="style3"><input name="key" type="text"
						value="${key}"
						style="width: 403px;" /></td>
				</tr>
				<tr>
					<td class="style2">订单号:</td>
					<td class="style3"><input name="orderNum" type="text"
						value="${orderNum}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">支付时间:</td>
					<td class="style3"><input name="payDate" type="date"
						value="${payDate}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">商户号:</td>
					<td class="style3"><input name="merNo" type="text"
						value="${merchNo}" style="width: 214px;" /></td>
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
                       <!-- 	<option value="ZFB">支付宝</option>
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
					<td class="style2">订单金额:</td>
					<td class="style3"><input name="amount" type="text" value="${amount}" style="width: 400px;" /></td>
				</tr>
                <tr>
					<td class="style2">商品名称:</td>
					<td class="style3"><input name="goodsName" type="text" value="${goodsName}" style="width: 400px;" /></td>
				</tr>
				<tr>
					<td class="style2">编码方式:</td>
					<td class="style3"><input name="charset" type="text" value="${charset}" style="width: 400px;" /></td>
				</tr>
				<tr>
					<td class="style2">版本号：</td>
					<td class="style3"><input name="version" type="text"
						value="${version}" /></td>
				</tr>
                <tr>
					<td class="style2">查询地址:</td>
					<td class="style3"><input name="queryUrl" type="text" value="${queryUrl}" style="width: 400px;" /></td>
				</tr>
				<tr>
					<td class="style2">&nbsp;</td>
					<td class="style3"><input type="submit" name="btnSub"
						value="提交查询" id="btnSub" /></td>
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