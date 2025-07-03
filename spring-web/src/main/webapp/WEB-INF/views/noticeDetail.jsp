<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<style>
.wrapper {
	max-width: 700px;
	margin: 0 auto;
	padding: 20px;
}
</style>
<title>공지사항 상세</title>
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">공지사항 상세 페이지</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>
		<main>
			<form action="/notice/${mode}" method="post">
				<input type="hidden" name="notice_cd" value="${notice.notice_cd}">

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

				<!-- 작성자 -->
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

				<!-- 내용 입력 or 출력 -->
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

				<!-- 버튼 영역 -->
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

		</main>

	</div>
</body>
</html>