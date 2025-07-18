<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글 수정</h2>
			<div class="container">
				<form method="post" action="/notice/modify" id="notice-form"  enctype="multipart/form-data">
					<input type="hidden" id="notice_cd" name="notice_cd" value="${notice.notice_cd}">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" value="${notice.title}"></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" name="staff_nm" id="staff_nm" value="${currentUserInfo.staff_nm}" readonly></td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="content" id="content">${notice.content}</textarea></td>
						</tr>
						<!-- 기존 첨부 파일 목록 -->
						<tr>
							<th>기존 첨부 파일</th>
							<td>
								<c:choose>
									<c:when test="${not empty attachedFiles}">
										<ul class="file-list">
											<c:forEach var="file" items="${attachedFiles}">
												<li>
													<a href="/notice/downloadFile/${file.file_cd}" target="_blank">${file.original_name}</a>
													(<c:out value="${file.file_size / 1024 < 1024 ? String.format('%.1f KB', file.file_size / 1024.0) : String.format('%.1f MB', file.file_size / (1024.0 * 1024.0))}" />)
													<%-- 삭제할 파일의 file_cd를 hidden input으로 전달 --%>
													<label class="delete-checkbox">
														<input type="checkbox" name="deletedFileCds" value="${file.file_cd}"> 삭제
													</label>
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
						<!-- 새로운 파일 첨부 필드 -->
						<tr>
							<th><label for="newFiles">새로운 첨부 파일</label></th>
							<td>
								<input type="file" name="newFiles" id="newFiles" multiple>
								<p class="file-info">새로운 파일을 추가하거나 기존 파일을 대체할 수 있습니다.</p>
							</td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">수정하기</button>
						<button type="button" onclick="location.href='/notice'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/notice.js"></script>
</body>
</html>