<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전기 재해 신고 목록</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/report.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>전기 재해 신고 목록</h2>
			<div class="container">
				<div class="search_addr">
					<select name="local" id="local">
					    <option value="전체" selected>전체</option>
					    <option value="서울">서울특별시</option>
					    <option value="부산">부산광역시</option>
					    <option value="대구">대구광역시</option>
					    <option value="인천">인천광역시</option>
					    <option value="광주">광주광역시</option>
					    <option value="대전">대전광역시</option>
					    <option value="울산">울산광역시</option>
					    <option value="세종">세종특별자치시</option>
					    <option value="경기">경기도</option>
					    <option value="강원">강원도</option>
					    <option value="충북">충청북도</option>
					    <option value="충남">충청남도</option>
					    <option value="전북">전라북도</option>
					    <option value="전남">전라남도</option>
					    <option value="경북">경상북도</option>
					    <option value="경남">경상남도</option>
					    <option value="제주">제주특별자치도</option>
					</select>
					<button type="button" id="search_addr_btn">지역 조회</button>
				</div>
				<div class="report-button">
					<c:if test="${userType eq 'staff'}">
						<button onclick="location.href='/report/form'" class="report-button">새 글 등록</button>
					</c:if>
				</div>
				<table class="report-table">
					<colgroup>
						<col style="width:5%">
						<col style="width:15%">
						<col style="width:40%">
						<col style="width:10%">
						<col style="width:15%">
						<col style="width:15%">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>지역</th>
							<th>신고 제목</th>
							<th>담당자</th>
							<th>신고일</th>
							<th>수정일시</th>
						</tr>
					</thead>
					<tbody id="reportTableBody">
						<tr>
							<td colspan="6">신고 내역이 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
<script src="../../resources/js/report.js"></script>
</body>
</html>