<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>공지사항 작성</h2>
			<div class="container">
				<form action="/notice/register" method="post" id="notice-form">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" placeholder="제목" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" id="staff_nm" value="${currentUserInfo.staff_nm}" readonly></td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="content" id="content" placeholder="내용"></textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">등록하기</button>
						<button type="button" onclick="location.href='/notice'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
	<script>
	document.addEventListener("DOMContentLoaded", () => {
		const form = document.getElementById("notice-form");

		form.addEventListener("submit", function (e) {
			// FormData 디버깅 (console 출력)
			const formData = new FormData(form);
			for (let pair of formData.entries()) {
				console.log(`${pair[0]}: ${pair[1]}`);
			}

			// 입력 필드 가져오기
			const title = document.getElementById("title");
			const content = document.getElementById("content");

			// 유효성 검사
			if (title.value.trim() === "") {
				alert("제목을 입력해주세요.");
				title.focus();
				e.preventDefault();
				return;
			}

			if (content.value.trim() === "") {
				alert("내용을 입력해주세요.");
				content.focus();
				e.preventDefault();
				return;
			}
		});
	});
	</script>
</body>
</html>