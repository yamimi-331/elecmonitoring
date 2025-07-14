<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/index.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<div class="container">
				<%-- 로그인 안한 경우 --%>
				<c:choose>
					<c:when test="${empty sessionScope.userType}">
						<div class="button-group">
							<button class="square-btn" onclick="location.href='/as/form'">AS 신고하기</button>
							<button class="square-btn" onclick="location.href='/as/detail'">나의 신고내역 <br>확인하기</button>
						</div>
					</c:when>
			
					<%-- 일반 사용자(userType == 'common') --%>
					<c:when test="${sessionScope.userType == 'common'}">
			    	    <div class="button-group">
							<button class="square-btn" onclick="location.href='/as/form'">AS 신고하기</button>
							<button class="square-btn" onclick="location.href='/as/detail'">나의 신고내역 <br>확인하기</button>
						</div>
					</c:when>
			
					<%-- 직원 (userType == 'staff') --%>
					<c:when test="${sessionScope.userType == 'staff'}">
						<div class="button-group">
							<button class="square-btn" onclick="location.href='/as/order'">AS 진행 현황 관리</button>
							<button class="square-btn" onclick="location.href='/as/calendar'">전체 AS 일정 확인</button>
						</div>
					</c:when>
				  
					<%-- 관리자(userType == 'admin') --%>
					<c:when test="${sessionScope.userType == 'admin'}">
			   			<div class="button-group">
							<button class="square-btn" onclick="location.href='/as/order'">AS 진행 현황 관리</button>
							<button class="square-btn" onclick="location.href='/as/calendar'">전체 AS 일정 확인</button>
						</div>
					</c:when>

					<%-- 기타 예외 --%>
					<c:otherwise>
						<div class="button-group">
							<button class="square-btn" onclick="location.href='/as/form'">AS 신고하기</button>
							<button class="square-btn" onclick="location.href='/as/detail'">나의 신고내역 <br>확인하기</button>
						</div>
					</c:otherwise>
				</c:choose>
				<button class="rect-btn" onclick="location.href='/dashboard'">전기 재해 모니터링 바로가기</button>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>