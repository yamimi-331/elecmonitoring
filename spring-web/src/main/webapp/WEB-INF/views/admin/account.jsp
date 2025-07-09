<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">관리자 페이지</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>
		<main class="main">
			<!-- 직원 계정 관리 -->
			<h3>직원 계정 관리</h3>
			<div>
				<input type="search" placeholder="ID 입력" autocomplete="off" />
				<button onclick="searchStaff()">조회</button>
				<button onclick="createStaff()">계정 생성</button>
			</div>
			<table>
				<thead>
					<tr>
						<th style="width: 15%">직원번호</th>
						<th style="width: 20%">아이디</th>
						<th style="width: 20%">이름</th>
						<th style="width: 15%">직급</th>
						<th style="width: 20%">담당 주소</th>
						<th style="width: 10%">상세 정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="6">조회된 데이터가 없습니다.</td>
					</tr>
				</tbody>
			</table>

			<!-- 계정 복구 -->
			<h3>계정 복구</h3>
			<div class="search-area">
				<select id="usertype" name="usertype">
					<option value="staff">직원</option>
					<option value="common">일반 사용자</option>
				</select> 
				<input type="search" placeholder="ID 입력" autocomplete="off" />
				<button onclick="searchDeletedAccount()">조회</button>
			</div>
			<table>
				<thead>
					<tr>
						<th style="width: 25%">계정 번호</th>
						<th style="width: 25%">아이디</th>
						<th style="width: 25%">이름</th>
						<th style="width: 25%">상세 정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="4">조회된 데이터가 없습니다.</td>
					</tr>
				</tbody>
			</table>

		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>