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
			<form action="/profile" method="post">
				<div class="i">
					<label for="user_id">아이디</label><br>
					<input type="text" name="id" id="id" value="${profileInfo.id}" readonly><br>
				</div>
				
				<div class="i">
					<label for="pw">기존 비밀번호 확인</label><br>
					<input type="password" name="pw" id="pw" autocomplete="new-password"><br>
					<button type="button">비밀번호 확인</button>
				</div>
				<div id="pwVerifyMsg" style="color:red; margin: 3px 0;">
				    <c:if test="${not empty errorMsg}">
				        ${errorMsg}
				    </c:if>
				</div>

				<div class="i">
					<label for="new_pw">새 비밀번호</label><br>
					<input type="password" name="new_pw" id="new_pw" autocomplete="new-password"><br>
					<small id="newPwMsg"></small>
				</div>
			  
				<div class="i">
					<label for="user_pw_ck">새 비밀번호 확인</label><br>
					<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password"><br>
					<small id="pwCheckMsg"></small>
				</div>
				
				<div class="i">
					<label for="nm">이름</label><br>
					<input type="text" name="nm" id="nm" value="${profileInfo.nm}"><br>
				</div>
				
				<div class="i">
					<c:choose>
				        <c:when test="${userType eq 'staff'}">
				            <label for="addr">담당 주소</label><br>
				            <input type="text" name="addr" id="addr" value="${profileInfo.addr}" readonly>
				        </c:when>
				        <c:otherwise>
				            <label for="addr">주소</label><br>
				            <input type="text" name="addr" id="addr" value="${profileInfo.addr}">
				        </c:otherwise>
				    </c:choose>
				</div>
				
				<c:if test="${userType eq 'common'}">
				    <div class="i">
				        <label for="mail">메일</label><br>
				        <input type="text" name="mail" id="mail" value="${profileInfo.mail}">
				    </div>
				</c:if>
				
				<button type="submit">제출하기</button>
			</form>
		</main>
	</div>
</body>
</html>