<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 목록</title>
</head>
<body>
	<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>

	<h2>공지사항</h2>
	<a class="btn-insert" href="/notice/detail?mode=insert">+ 새 공지 등록</a>

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
			<c:forEach var="notice" items="${noticeList}">
				<tr>
					<td>${notice.notice_cd}</td>
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
</body>
</html>