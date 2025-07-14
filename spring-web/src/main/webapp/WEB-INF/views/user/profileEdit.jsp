<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../../resources/js/profileEdit.js?after"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/signup.css?after" />
<script type="text/javascript">
//주소 API
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            const fullAddress = data.roadAddress || data.jibunAddress;
            document.getElementById("addr").value = fullAddress;
        }
    }).open();
}
</script>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
		<main class="main">
			<c:choose>
				<%-- 사용자 타입이 일반인경우 -------------------------------------------------------- --%>
				<c:when test="${userType eq 'common'}">
				<div class="profile-container">
					<form action="/profileEdit/common" method="post" class="signup-form">
						<input type="hidden" name="user_cd" value="${profileInfo.user_cd }">
						<c:if test="${profileInfo.user_social eq 'Basic'}">
							<label for="id">아이디</label>  
							<input type="text" name="user_id" id="id" value="${profileInfo.user_id}" readonly> 
						
							<label for="pw">기존 비밀번호 확인</label>  
							<input type="password" name="prepw" id="prepw" autocomplete="new-password"> 
							<button type="button">비밀번호 확인</button>
	
							<div id="pwVerifyMsg" class="msg">
								<c:if test="${not empty errorMsg}">
							        ${errorMsg}
							    </c:if>
							</div>
	
							<label for="pw">새 비밀번호</label>  
							<input type="password" name="user_pw" id="pw" autocomplete="new-password"> 
							<small id="newPwMsg" class="msg"></small>
	
							<label for="user_pw_ck">새 비밀번호 확인</label>  
							<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password">  
							<small id="pwCheckMsg" class="msg"></small>
						</c:if>
						<label for="nm">이름</label>  
						<input type="text" name="user_nm" id="nm" value="${profileInfo.user_nm}"> 
						
						<label for="addr">주소</label>  
						<input type="text" name="user_addr" id="addr" value="${profileInfo.user_addr}" readonly>
						<button type="button" onclick="searchAddress()">주소 검색</button>

						<label for="mail">메일</label>  
						<input type="text" name="user_mail" id="mail" value="${profileInfo.user_mail}">
						
						<input type="hidden" name="user_social" id="user_social" value="${profileInfo.user_social}">
						
						<button type="submit">제출하기</button>
						<button id="goback" type="button" onclick="location.href='/'">돌아가기</button>
						<a href="/profileEdit/delete" onclick="return confirmDelete();">회원 탈퇴</a>
					</form>
				</div>
				</c:when>
				<%-- 사용자 타입이 일반인경우 -------------------------------------------------------- --%>
				<%-- 사용자 타입이 직원인경우 -------------------------------------------------------- --%>
				<c:when test="${userType eq 'staff'}">
				<div class="profile-container">
					<form action="/profileEdit/staff" method="post" class="signup-form">
						<label for="id">아이디</label>  
						<input type="text" name="staff_id" id="id" value="${profileInfo.staff_id}" readonly> 

						<label for="prepw">기존 비밀번호 확인</label>  
						<input type="password" name="prepw" id="prepw" autocomplete="new-password"> 
						<button type="button">비밀번호 확인</button>

						<div id="pwVerifyMsg"  class="msg">
							<c:if test="${not empty errorMsg}">
						        ${errorMsg}
						    </c:if>
						</div>

						<label for="pw">새 비밀번호</label>
						<input type="password" name="staff_pw" id="pw" autocomplete="new-password">
						<small id="newPwMsg" class="msg"></small>

						<label for="user_pw_ck">새 비밀번호 확인</label>
						<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password">
						<small id="pwCheckMsg" class="msg"></small>

						<label for="nm">이름</label>
						<input type="text" name="staff_nm" id="nm" value="${profileInfo.staff_nm}">

						<label for="addr">담당 주소</label>
						<input type="text" name="staff_addr" id="addr" value="${profileInfo.staff_addr}" readonly>

						<button type="submit">제출하기</button>
						<button id="goback" type="button" onclick="location.href='/'">돌아가기</button>
						<a href="/profileEdit/delete" onclick="return confirmDelete();">회원 탈퇴</a>
					</form>
				</div>
				</c:when>
				<%-- 사용자 타입이 직원인경우 -------------------------------------------------------- --%>
				<%-- 사용자 타입이 관리자인경우 -------------------------------------------------------- --%>
				<c:when test="${userType eq 'admin'}">
				<div class="profile-container">
					<form action="/profileEdit/staff" method="post" class="signup-form">
						<label for="id">아이디</label>
						<input type="text" name="staff_id" id="id" value="${profileInfo.staff_id}" readonly>

						<label for="prepw">기존 비밀번호 확인</label>
						<input type="password" name="prepw" id="prepw" autocomplete="new-password">
						<button type="button">비밀번호 확인</button>

						<div id="pwVerifyMsg" class="msg">
							<c:if test="${not empty errorMsg}">
						        ${errorMsg}
						    </c:if>
						</div>

						<label for="pw">새 비밀번호</label>
						<input type="password" name="staff_pw" id="pw" autocomplete="new-password">
						<small id="newPwMsg" class="msg"></small>

						<label for="user_pw_ck">새 비밀번호 확인</label>
						<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password">
						<small id="pwCheckMsg" class="msg"></small>

						<label for="staff_nm">이름</label>
						<input type="text" name="staff_nm" id="nm" value="${profileInfo.staff_nm}">

						<input type="hidden" name="staff_addr" id="addr" value="${profileInfo.staff_addr}">

						<button type="submit">제출하기</button>
						<button id="goback" type="button" onclick="location.href='/'">돌아가기</button>
						<a href="/profileEdit/delete" onclick="return confirmDelete();">회원 탈퇴</a>
					</form>
				</div>
				</c:when>
				<%-- 그외 --%>
				<c:otherwise>
					<p>회원가입 후 이용해주세요</p>
				</c:otherwise>
			</c:choose>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>