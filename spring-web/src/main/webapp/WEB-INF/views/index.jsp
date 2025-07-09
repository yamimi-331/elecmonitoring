<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
</head>
<body>
	<div class="wrapper">
	<h2 class="header-title">전기재해 모니터링 및 노후시설 A/S 신고 관리 시스템</h2>
    <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	<main class="main">
		<div class="container">
			<button onclick="location.href='/as/form'">AS 신고하기</button>
			
			<button onclick="location.href='/as/detail'">나의 신고내역 확인하기</button>
			
			<button onclick="location.href='/dashboard'">전기 재해 모니터일 바로가기</button>
		</div>
	</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>