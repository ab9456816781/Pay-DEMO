<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发起代付</title>
</head>
<body>
<%
    Object msg = request.getAttribute("msg");
%>
<c:if test='${ msg != null}'>
<h1 style="color:red;">${msg }</h1>
</c:if>
	<form action="./remitConfirm" method="post">
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
                	<td class="style2">代付请求地址:</td>
                    <td class="style3"><input type="text" name="reqRemitUrl"
                        value="${reqRemitUrl}" size="80"/></td>
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
                <%-- <tr>
                    <td class="style2">随机数:</td>
                    <td class="style3"><input type="text" name="random"
                        value="${random}"  size="80"/></td>
                </tr> --%>
               	<%-- <tr>
                    <td class="style2">商品名称:</td>
                    <td class="style3"><input type="text" name="goodsName"
                        value="${goodsName}"  size="80"/></td>
                </tr> --%>
                <tr>
                    <td class="style2">网关类型:</td>
                    <td class="style3"><select name="bankCode" >
                       	<option value="ICBC" selected="selected">中国工商银行</option>
                       	<option value="BOC">中国银行</option>
                       	<option value="ABC">中国农业银行</option>
                       	<option value="CCB">中国建设银行</option>
                       	<option value="BCM">交通银行</option>
                       	<option value="CMB">中国招商银行</option>
                       	<option value="CEB">中国光大银行</option>
                       	<option value="CMBC">中国民生银行</option>
                       	<option value="HXB">华夏银行</option>
                       	<option value="CIB">兴业银行</option>
                       	<option value="CNCB">中信银行</option>
                       	<option value="SPDB">上海浦东发展银行</option>
                       	<option value="PSBC">中国邮政储蓄银行</option>
                    </select></td>
                </tr>
                <tr>
                    <td class="style2">户名:</td>
                    <td class="style3"><input type="text" name="bankAccountName"
                        value="${bankAccountName}"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">卡号:</td>
                    <td class="style3"><input type="text" name="bankAccountNo"
                        value="${bankAccountNo}"  size="80"/></td>
                </tr>
                <tr>
                    <td class="style2">回调地址:</td>
                    <td class="style3"><input type="text" name="remitCallBackUrl"
                        value="${remitCallBackUrl}"  size="80"/></td>
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