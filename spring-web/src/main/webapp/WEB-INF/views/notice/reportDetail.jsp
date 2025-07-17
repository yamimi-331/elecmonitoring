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
			<h2>게시글 상세 보기</h2>
			<div class="container">
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>${report.title}</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${report.staff_nm}</td>
					</tr>
					<tr>
						<th>신고일</th>
						<td>${reportDt}</td>
					</tr>
					<tr>
						<th>지역</th>
						<td>${report.local}</td>
					</tr>
					<tr>
						<th>유형</th>
						<td>${report.type}</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>${report.content}</td>
					</tr>
				</table>
				<div class="button-box">
					<c:choose>
					    <c:when test="${userType == 'staff'}">
					        <c:if test="${not empty currentUserInfo && currentUserInfo.staff_cd eq report.staff_cd}">
								<button type="button" id="modifyBtn">수정하기</button>
								<button type="button" id="deleteBtn">삭제하기</button>
							</c:if>
					    </c:when>
					    <c:when test="${userType == 'admin'}">
					        <button type="button" id="modifyBtn">수정하기</button>
							<button type="button" id="deleteBtn">삭제하기</button>
					    </c:when>
	 				</c:choose>
					<button onclick="location.href='/report'">돌아가기</button>
				</div>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script>
	document.getElementById('modifyBtn').addEventListener('click', function() {
		location.href = '/report/modify?report_cd=' + ${report.report_cd};
	});
	document.getElementById("deleteBtn").addEventListener("click", function () {
		if (confirm("정말 삭제하시겠습니까?")) {
			const reportCd = ${report.report_cd};

			fetch('/report/remove', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				},
				body: 'report_cd=' + encodeURIComponent(reportCd)
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