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
			<h2>공지사항</h2>
			<div class="container">
				<div class="search_addr">
					<input type="text" name="search_word" id="search_word" autocomplete="off">
					<button type="button" id="search_word_btn">검색</button>
				</div>
				
				<table class="report-table">
					<colgroup>
						<col style="width:5%">
						<col style="width:50%">
						<col style="width:15%">
						<col style="width:15%">
						<col style="width:15%">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>수정일시</th>
						</tr>
					</thead>
					<tbody id="noticeTableBody">
						<tr>
							<td colspan="5">게시글이 없습니다.</td>
						</tr>
					</tbody>
				</table>
				<!-- 페이지네이션이 렌더링될 div -->
				<div id="pagination" class="pagination-area"></div>
				<div class="report-button">
					<c:if test="${userType eq 'admin'}">
						<button onclick="location.href='/notice/form'" class="report-button">새 글 등록</button>
					</c:if>
				</div>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/notice.js"></script>
</body>
</html>