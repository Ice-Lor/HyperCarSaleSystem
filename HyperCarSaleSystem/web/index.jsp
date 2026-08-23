<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Tự động chuyển hướng từ trang gốc về Controller Trang Chủ
    response.sendRedirect(request.getContextPath() + "/home");
%>
