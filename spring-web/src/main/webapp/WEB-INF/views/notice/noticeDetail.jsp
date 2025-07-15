<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<title>공지사항 상세</title>
<style>
.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
}

input[name="title"] {
	width: 500px;
	padding: 10px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 4px;
	height: 50px;
	font-size: 16px;
}

input[name="user_nm"]{
	width: 300px;
	padding: 10px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 4px;
	height: 50px;
	font-size: 16px;
}

.form-group textarea{
	width: 700px;
	padding: 10px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 4px;
	height: 500px;
	font-size: 16px;
	resize: vertical;
}

.form-group div {
	padding: 10px 15px;
	border: 1px solid #ccc;
	border-radius: 4px;
	background-color: #e5ebd5;
	font-size: 16px;
	width: fit-content;
	max-width: 700px;
	box-sizing: border-box;
	white-space: pre-wrap;
}

.button-group{
	text-align: center;
	margin-top: 30px;
}

.button-group button {
	padding: 10px 20px;
	margin: 0 5px;
	border: none;
	background-color: #007bff;
	color: white;
	border-radius: 5px;
	cursor: pointer;
}

.button-group button:hover{
	background-color: #0056b3;
}
.container{
	display: flex;
	justify-content: center;
}
</style>

</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
		<h2>게시글 상세</h2>
		<div class="container">
			<c:choose>
				<c:when test="${mode eq 'insert'}">
					<c:set var="formAction" value="/notice/insert" />
				</c:when>
				<c:when test="${mode eq 'update'}">
					<c:set var="formAction" value="/notice/update" />
				</c:when>
				<c:otherwise>
					<c:set var="formAction" value="#" />
				</c:otherwise>
			</c:choose>

			<form action="${formAction}" method="post">
				<c:if test="${mode ne 'insert'}">
					<input type="hidden" name="notice_cd" value="${notice.notice_cd}">
				</c:if>

				<input type="hidden" name="use_yn" value="Y"> <input
					type="hidden" name="staff_role" value="common">

				<div class="form-group">
					<label for="title">제목</label>
					<c:choose>
						<c:when test="${mode eq 'view'}">
							<div>${notice.title}</div>
						</c:when>

						<c:otherwise>
							<input type="text" name="title" value="${notice.title}" required>
						</c:otherwise>
					</c:choose>
				</div>

				<%-- 작성자 --%>
				<div class="form-group">
					<label for="user_nm">작성자</label>
					<c:choose>
						<c:when test="${mode eq 'view'}">
							<div>${notice.user_nm}</div>
						</c:when>
						<c:otherwise>
							<input type="text" name="user_nm" value="${notice.user_nm}"
								required>
						</c:otherwise>
					</c:choose>
				</div>

				<%-- 조회 모드일 경우: 수정/삭제/목록 버튼 표시 --%>
				<div class="form-group">
					<label for="content">내용</label>
					<c:choose>
						<c:when test="${mode eq 'view'}">
							<%-- 줄바꿈을 유지한 채 텍스트 출력 --%>
							<div style="white-space: pre-wrap;">${notice.content}</div>
						</c:when>
						<c:otherwise>
							<textarea name="content" rows="8" required>${notice.content}</textarea>
						</c:otherwise>
					</c:choose>
				</div>

				<%-- 버튼 영역 --%>
				<div class="button-group">
					<c:choose>

						<%-- 조회 모드일 경우: 수정/삭제/목록 버튼 표시 --%>
						<c:when test="${mode eq 'view'}">
							<button type="button"
								onclick="location.href='/notice/detail?notice_cd=${notice.notice_cd}&mode=update'">수정</button>

							<%-- 삭제: 삭제 확인 후 삭제 요청 --%>
							<button type="button"
								onclick="if(confirm('삭제하시겠습니까?')) location.href='/notice/delete?notice_cd=${notice.notice_cd}'">삭제</button>

							<!-- 목록으로 이동 -->
							<button type="button" onclick="location.href='/notice/list'">목록</button>
						</c:when>
						<%-- 등록 또는 수정 모드일 경우: 저장/취소 버튼 표시 --%>
						<c:otherwise>
							<button type="submit">저장</button>
							<button type="button" onclick="location.href='/notice/list'">취소</button>
						</c:otherwise>
					</c:choose>
				</div>
			</form>
			</div>
		</main>

	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>