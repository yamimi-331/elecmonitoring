<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<style>
.btn-insert {
	display: inline-block;
	padding: 8px 16px;
	margin: 15px 150px 0px 0px;
	background-color: #28a745;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	margin-bottom: 10px;
}

.btn-insert:hover {
	background-color: #218838;
}

table {
	width: 85%;
	border-collapse: collapse;
	table-layout: fixed;
	word-wrap: break-word; 
	margin: 0 auto;
}

th, td {
	border: 1px solid #ccc;
	padding: 10px;
	text-align: center;
}

td a{
	display: inline-block;
	max-width: 100%;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	color: #077bff;
	text-decoration: none;
}

td a:hover{
	text-decoration: underline;
}

th:hover {
	background-color: #f9f9f9;
}
</style>
<title>공지사항 목록</title>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
		<main  class="main">
		<div class="container">
			<div style="text-align: right;">
				<button class="btn-insert"
					onclick="location.href='/notice/detail?mode=insert'">공지 사항 등록</button>
			</div>
			<table>
				<thead>
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>작성일</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="notice" items="${noticeList}" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td><a
								href="/notice/detail?notice_cd=${notice.notice_cd}&mode=view">
									${notice.title} </a></td>
							<td>${notice.user_nm}</td>
							<td>${notice.create_dt}</td>
						</tr>
					</c:forEach>

					<c:if test="${empty noticeList}">
						<tr>
							<td colspan="4">등록된 공지사항이 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		
		</main>

	</div>
	
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>