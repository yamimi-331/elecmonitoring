<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글</h2>
			<div class="container">
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>제목입니다</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>김직원</td>
					</tr>
					<tr>
						<th>신고일</th>
						<td>22-03-22 10:53</td>
					</tr>
					<tr>
						<th>지역</th>
						<td>울산광역시</td>
					</tr>
					<tr>
						<th>유형</th>
						<td>전기 화재</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>신고 내용입니다.</td>
					</tr>
				</table>
				<div class="button-box">
					<c:if test="${currentUserInfo.staff_role eq 'admin'}">
						<button type="button" id="modifyBtn">수정하기</button>
						<button type="button" id="deleteBtn">삭제하기</button>
					</c:if>
					<button onclick="location.href='/report'">돌아가기</button>
				</div>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>