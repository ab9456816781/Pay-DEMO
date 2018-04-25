<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <div>
        <h2>客户端支付订单确认</h2>
        <ul class="fix">
            <li>
                <label>订单号：<font color="red">*</font></label> <%= request.getParameter("orderNum") %>
            </li>
            <li><label>金额：<font color="red">*</font></label> <%= request.getParameter("amount") %></li>
        </ul>
        <form id="appForm" name="appForm"
            action="<%= request.getParameter("reqUrl") %>" method="POST">
            <input type="hidden" name="data" value="<%= request.getAttribute("data") %>" />
            <input type="hidden" name="merchNo" value="<%= request.getParameter("merchNo") %>" />
            <input type="hidden" name="version" value="<%= request.getParameter("version") %>" />
            <input type="submit" value="确认付款">
        </form>
    </div>
</body>
</html>