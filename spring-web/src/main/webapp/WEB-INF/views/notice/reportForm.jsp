<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글</h2>
			<div class="container">
				<form action="/report/register" method="post">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" placeholder="제목" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" id="staff_nm" value="${currentUserInfo.staff_nm}" readonly></td>
						</tr>
						<tr>
							<th><label for="phone">신고자 전화번호</label></th>
							<td><input type="text" name="phone" id="phone" placeholder="신고자 전화번호" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="local">지역</label></th>
							<td>
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
							</td>
						</tr>
						<tr>
							<th><label for="type">유형</label></th>
							<td>
								<select name="type" id="type">
									<option value="-">--재해 유형--</option>
									<option value="전기 화재">전기 화재</option>
									<option value="전기 감전">전기 감전</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea name="content" id="content" placeholder="내용"></textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">등록하기</button>
						<button type="button" onclick="location.href='/report'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>