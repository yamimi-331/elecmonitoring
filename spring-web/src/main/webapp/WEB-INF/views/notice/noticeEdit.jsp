<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글 수정</h2>
			<div class="container">
				<form method="post" action="/notice/modify" id="notice-form">
					<input type="hidden" id="notice_cd" name="notice_cd" value="${notice.notice_cd}">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" value="${notice.title}"></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" name="staff_nm" id="staff_nm" value="${currentUserInfo.staff_nm}" readonly></td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="content" id="content">${notice.content}</textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">수정하기</button>
						<button type="button" onclick="location.href='/notice'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/notice.js"></script>
</body>
</html>