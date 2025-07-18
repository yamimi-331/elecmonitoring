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
					<!-- 첨부 파일 조회 필드 추가 -->
					<tr>
						<th>첨부 파일</th>
						<td>
							<c:choose>
								<c:when test="${not empty attachedFiles}">
									<ul>
										<c:forEach var="file" items="${attachedFiles}">
											<li>
												<%-- 파일 다운로드/조회를 위한 링크. /files/ URL은 servlet-context.xml에서 매핑됨 --%>
												<a href="/notice/downloadFile/${file.file_cd}" target="_blank">${file.original_name}</a>
												(<c:out value="${file.file_size / 1024 < 1024 ? String.format('%.1f KB', file.file_size / 1024.0) : String.format('%.1f MB', file.file_size / (1024.0 * 1024.0))}" />)
											</li>
										</c:forEach>
									</ul>
								</c:when>
								<c:otherwise>
									<p>첨부된 파일이 없습니다.</p>
								</c:otherwise>
							</c:choose>
						</td>
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