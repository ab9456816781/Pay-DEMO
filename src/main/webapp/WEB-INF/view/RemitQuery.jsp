<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代付查詢</title>
</head>
<body>
<%
    Object msg = request.getAttribute("msg");
%>
<c:if test='${ msg != null}'>
<h1 style="color:red;">${msg }</h1>
</c:if>
	<form method="post" action="./submitRemitQuery">
		<div>
			<table class="style1">
				<tr>
					<td class="style2">签名密钥:</td>
					<td class="style3"><input name="key" type="text"
						value="${key}" style="width: 403px;" /></td>
				</tr>
				<tr>
					<td class="style2">订单号:</td>
					<td class="style3"><input name="orderNum" type="text"
						value="${orderNum}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">代付时间:</td>
					<td class="style3"><input name="remitDate" type="date"
						value="${remitDate}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">代付金额:</td>
					<td class="style3"><input name="amount" type="text"
						value="${amount}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">商户号:</td>
					<td class="style3"><input name="merNo" type="text"
						value="${merchNo}" style="width: 214px;" /></td>
				</tr>
				<tr>
					<td class="style2">编码方式:</td>
					<td class="style3"><input name="charset" type="text"
						value="${charset}" style="width: 400px;" /></td>
				</tr>
				<tr>
					<td class="style2">版本号：</td>
					<td class="style3"><input name="version" type="text"
						value="${version}" /></td>
				</tr>
				<tr>
					<td class="style2">查询地址:</td>
					<td class="style3"><input name="remitQueryUrl" type="text"
						value="${remitQueryUrl}" style="width: 400px;" /></td>
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