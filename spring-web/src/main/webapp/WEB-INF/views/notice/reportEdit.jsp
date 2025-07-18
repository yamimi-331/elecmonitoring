<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글 수정</h2>
			<div class="container">
				<form method="post" action="/report/modify">
					<input type="hidden" id="report_cd" name="report_cd" value="${report.report_cd}">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" value="${report.title}"></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" name="staff_nm" id="staff_nm" value="${currentUserInfo.staff_nm}" readonly></td>
						</tr>
						<tr>
							<th><label for="phone">신고자 전화번호</label></th>
							<td><input type="text" name="phone" id="phone" value="${report.phone}" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="local">지역</label></th>
							<td>
								<select name="local" id="local">
								    <option value="서울" ${report.local == '서울' ? 'selected' : ''}>서울특별시</option>
								    <option value="부산" ${report.local == '부산' ? 'selected' : ''}>부산광역시</option>
								    <option value="대구" ${report.local == '대구' ? 'selected' : ''}>대구광역시</option>
								    <option value="인천" ${report.local == '인천' ? 'selected' : ''}>인천광역시</option>
								    <option value="광주" ${report.local == '광주' ? 'selected' : ''}>광주광역시</option>
								    <option value="대전" ${report.local == '대전' ? 'selected' : ''}>대전광역시</option>
								    <option value="울산" ${report.local == '울산' ? 'selected' : ''}>울산광역시</option>
								    <option value="세종" ${report.local == '세종' ? 'selected' : ''}>세종특별자치시</option>
								    <option value="경기" ${report.local == '경기' ? 'selected' : ''}>경기도</option>
								    <option value="강원" ${report.local == '강원' ? 'selected' : ''}>강원도</option>
								    <option value="충북" ${report.local == '충북' ? 'selected' : ''}>충청북도</option>
								    <option value="충남" ${report.local == '충남' ? 'selected' : ''}>충청남도</option>
								    <option value="전북" ${report.local == '전북' ? 'selected' : ''}>전라북도</option>
								    <option value="전남" ${report.local == '전남' ? 'selected' : ''}>전라남도</option>
								    <option value="경북" ${report.local == '경북' ? 'selected' : ''}>경상북도</option>
								    <option value="경남" ${report.local == '경남' ? 'selected' : ''}>경상남도</option>
								    <option value="제주" ${report.local == '제주' ? 'selected' : ''}>제주특별자치도</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="type">유형</label></th>
							<td>
								<select name="type" id="type">
									<option value="전기 화재" ${report.type == '전기 화재' ? 'selected' : ''}>전기 화재</option>
									<option value="전기 감전" ${report.type == '전기 감전' ? 'selected' : ''}>전기 감전</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="content" id="content">${report.content}</textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">수정하기</button>
						<button type="button" onclick="location.href='/report'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/report.js"></script>
</body>
</html>