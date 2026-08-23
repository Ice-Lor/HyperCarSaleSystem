<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    
    <!-- Bảo mật CSRF Token cho JavaScript AJAX -->
    <meta name="csrf-token" content="${csrfToken}">
    
    <title>${empty pageTitle ? 'HYPERCAR SHOWROOM - Đỉnh Cao Siêu Xe Độc Bản' : pageTitle} | HYPERCAR</title>
    
    <!-- Link file CSS Giao diện Dark & Gold Thượng Lưu -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
