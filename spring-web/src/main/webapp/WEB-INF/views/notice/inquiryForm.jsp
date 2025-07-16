<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의 게시글 작성</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>문의 게시글 작성</h2>
			<div class="container">
				<form action="/inquiry/register" method="post">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="inquiry_title" id="title" placeholder="제목" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="user_nm">작성자</label></th>
							<td><input type="text" id="user_nm" value="${currentUserInfo.user_nm}" readonly></td>
						</tr>
						<tr>
							<th>공개 여부</th>
							<td>
								<input type="radio" name="secret_yn" id="secret_n" value="N" checked><label for="secret_n">공개</label>
								<input type="radio" name="secret_yn" id="secret_y" value="Y"><label for="secret_y">비공개</label>
							</td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="inquiry_content" id="content" placeholder="내용"></textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">등록하기</button>
						<button type="button" onclick="location.href='/inquiry'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>