<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>A/S 상세 정보</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../../resources/js/asEdit.js?after"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
		<main class="main">
			<h2>A/S 상세 정보</h2>
			<div class="container">
				<span>신고자 정보</span>
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<c:choose>
						<c:when test="${sessionScope.userType == 'common'}">
							<tr>
								<th>이름</th>
								<td>${currentUserInfo.user_nm}</td>
							</tr>
							<tr>
								<th>이메일</th>
								<td>${asVO.user_mail}</td>
							</tr>
						</c:when>
						<c:when test="${sessionScope.userType == 'guest'}">
							<tr>
								<th>이름</th>
								<td>${asVO.guest_nm}</td>
							</tr>
							<tr>
								<th>이메일</th>
								<td>${asVO.guest_mail}</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>이름</th>
								<td></td>
							</tr>
							<tr>
								<th>이메일</th>
								<td></td>
							</tr>   
						</c:otherwise>
					</c:choose>
				</table>
				<span>시설물 정보</span>
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th><label for="as_facility">종류</label></th>
						<td>${asVO.as_facility}</td>
					</tr>
					<tr>
						<th><label for="as_addr">주소</label></th>
						<td>${base_addr} ${detail_addr} </td>
					</tr>
				</table>
				<span>상세 정보</span>
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th>문제 종류</th>
						<td>${asVO.as_title}</td>
					</tr>
					<tr>
						<th>상세 정보</th>
						<td>${asVO.as_content}</td>
					</tr>
					<tr>
						<th>예약 일시</th>
						<td>${formattedAsDate}</td>
					</tr>
					<tr>
						<th>진행 현황</th>
						<td>${asVO.as_status}</td>
					</tr>
				</table>
				<div class="button-box">
					<c:if test="${asVO.as_status == '신고 접수'}">
						<button type="button" id="modifyBtn">수정하기</button>
						<button type="submit" form="cancleForm" onclick="return confirmCancel()">예약 취소</button>
					</c:if>
					<button type="button" onclick="location.href='/as/detail'">돌아가기</button>
				</div>
				<form id="cancleForm" action="/as/cancleCommon" method="post">
					<input type="hidden" name="as_cd" value="${asVO.as_cd}" />
				</form>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
	<script>
		document.getElementById('modifyBtn').addEventListener('click', function() {
			location.href = '/as/edit?as_cd=' + ${asVO.as_cd};
		});
	</script>
</body>
</html>