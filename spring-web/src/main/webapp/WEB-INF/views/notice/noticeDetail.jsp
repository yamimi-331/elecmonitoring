<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세 보기</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>공지사항 상세 보기</h2>
			<div class="container">
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>${notice.title}</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${notice.staff_nm}</td>
					</tr>
					<tr>
						<th>작성일</th>
						<td>${noticeDt}</td>
					</tr>
					<tr>
						<th>수정일시</th>
						<td>${updateDt}</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>${notice.content}</td>
					</tr>
				</table>
				<div class="button-box">
			        <c:if test="${userType == 'admin'}">
						<button type="button" id="modifyBtn">수정하기</button>
						<button type="button" id="deleteBtn">삭제하기</button>
					</c:if>
					<button onclick="location.href='/notice'">돌아가기</button>
				</div>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script>
	document.getElementById('modifyBtn').addEventListener('click', function() {
		location.href = '/notice/modify?notice_cd=' + ${notice.notice_cd};
	});
	document.getElementById("deleteBtn").addEventListener("click", function () {
		if (confirm("정말 삭제하시겠습니까?")) {
			const noticeCd = ${notice.notice_cd};

			fetch('/notice/remove', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				body: 'notice_cd=' + encodeURIComponent(noticeCd)
			})
			.then(response => {
				if (response.redirected) {
					window.location.href = response.url;
				}
			});
		}
	});
</script>
</body>
</html>