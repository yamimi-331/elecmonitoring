<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">A/S 신고</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<table border="1">
			    <thead>
			        <tr>
			        	<th>번호</th>
			            <th>신고 내역</th>
			            <th>예약 일시</th>
			            <th>진행 상태</th>
			            <th>상세 정보</th>
			        </tr>
			    </thead>
			    <tbody>
			        <c:forEach var="as" items="${userList}">
			            <tr>
			            	<td>${as.as_cd}</td>
			            	<td>
			            		<form action="/as/edit" method="post" style="display:inline;">
								    <input type="hidden" name="as_cd" value="${as.as_cd}">
								    <button type="submit">
								        ${as.as_title}
								    </button>
								</form>
			            	</td>
			                <td>${as.as_date}</td>
			                <td>${as.as_status}</td>
			                <td>정보.....</td>
			            </tr>
			        </c:forEach>
				    </tbody>
				</table>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>