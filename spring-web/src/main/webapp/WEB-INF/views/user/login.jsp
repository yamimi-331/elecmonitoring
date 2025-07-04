<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 3개 탭 -->
	<ul>
		<li><a href="#user">일반 로그인</a></li>
		<li><a href="#staff">직원 로그인</a></li>
		<li><a href="#guest">비회원 로그인</a></li>
	</ul>

	<div id="user">
		<form action="/login/user" method="post">
			<!-- 일반 로그인 폼 -->
		</form>
	</div>

	<div id="staff">
		<form action="/login/staff" method="post">
			<!-- 직원 로그인 폼 -->
		</form>
	</div>

	<div id="guest">
		<form action="/login/guest" method="post">
			<!-- 비회원 예약조회 폼 -->
		</form>
	</div>
</body>
</html>