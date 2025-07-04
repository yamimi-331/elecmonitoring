<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">회원 정보 수정</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<form>
				<div class="i">
					<label for="user_id">아이디</label><br>
					<input type="text" name="user_id" id="user_id" autocomplete="off" value="${currentUserInfo.user_id}" readonly><br>
				</div>

				<div class="i">
					<label for="user_pw">새 비밀번호</label><br>
					<input type="password" name="user_pw" id="user_pw" autocomplete="new-password"><br>
				</div>
			  
				<div class="i">
					<label for="user_pw_ck">새 비밀번호 확인</label><br>
					<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password"><br>
				</div>
				
				<div class="i">
					<label for="user_nm">이름</label><br>
					<input type="text" name="user_nm" id="user_nm" autocomplete="off"><br>
				</div>
				
				<div class="i">
					<label for="user_addr">주소</label><br>
					<input type="text" name="user_addr" id="user_addr" autocomplete="off"><br>
				</div>
				
				<div class="i">
					<label for="user_mail">메일</label><br>
					<input type="text" name="user_mail" id="user_mail" autocomplete="off"><br>
				</div>
			</form>
		</main>
	</div>
</body>
</html>