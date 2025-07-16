<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의 게시판</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/report.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>문의 게시판</h2>
			<div class="container">
				<div class="report-button">
					<c:if test="${userType eq 'common'}">
						<button onclick="location.href='/inquiry/form'" class="report-button">새 글 등록</button>
					</c:if>
					
				</div>
				<table class="report-table">
					<colgroup>
						<col style="width:5%">
						<col style="width:55%">
						<col style="width:15%">
						<col style="width:15%">
						<col style="width:10%">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>처리 상태</th>
						</tr>
					</thead>
					<tbody id="inquiryTableBody">
						<tr>
							<td colspan="5">게시글이이 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/inquiry.js"></script>
<script>
	<c:choose>
		<c:when test="${userType eq 'common'}">
			window.currentUserCd = ${currentUserInfo.user_cd};
		</c:when>
		<c:when test="${userType eq 'staff'}">
			window.currentUserCd = null; // staff에게 user_cd 없음
		</c:when>
		<c:otherwise>
			window.currentUserCd = null; // guest 등 기타
		</c:otherwise>
	</c:choose>
	window.userRole = "${userType}";
</script>
</body>
</html>