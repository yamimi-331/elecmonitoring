<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AS 일정 목록</title>
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css" rel="stylesheet" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/locales/ko.global.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/asOder.css?after" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
		<main class="main">
			<h2>AS 일정 목록</h2>
			<div class="container">
				<div class="search-section">
					<input type="date" id="startDate" /> ~ <input type="date" id="endDate" />
					<c:if test="${currentUserInfo.staff_role eq 'admin'}">
						<input type="text" id="searchStaff" placeholder="담당자">
					</c:if>
					<button id="btnSearch">조회</button>
				</div>
				<table id="asTable" class="custom-table">
					<thead>
						<tr>
							<th>코드번호</th>
							<th>신고 제목</th>
							<th>담당자명</th>
							<th>진행 상태</th>
							<th>AS 예약 일시</th>
							<th>상세 정보</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="as" items="${asList}">
							<tr>
								<td>${as.as_cd}</td>
								<td>${as.as_title}</td>
								<td>${as.staff_nm}</td>
								<td>${as.as_status}</td>
								<td class="asTime">${formatDate(item.as_date)}</td>
								<td>
									<button type="button" onclick="openModal(${as.as_cd})">상세보기</button>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty asList}">
							<tr>
								<td colspan="6">조회된 일정이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</main>
	</div>

  <!-- 모달 -->
	<div id="statusModal" class="modal">
	  <div class="modal-content">
	    <h3>AS 상세 정보</h3>
	    <div class="modal-info"><strong>코드번호</strong> <span id="modalAsCd"></span></div>
	    <div class="modal-info"><strong>신고 제목</strong> <span id="modalAsTitle"></span></div>
	    <div class="modal-info"><strong>담당자명</strong> <span id="modalStaffNm"></span></div>
	    <div class="modal-info"><strong>신청자명</strong> <span id="modalUserNm"></span></div>
	    <div class="modal-info"><strong>상세 정보</strong> <span id="modalAsContent"></span></div>
	    <div class="modal-info"><strong>예약 일시</strong> <span id="modalAsDate"></span></div>
	    <div class="modal-info"><strong>위치</strong> <span id="modalAsAddr"></span></div>
	    <div class="modal-info"><strong>시설물</strong> <span id="modalAsFacility"></span></div>
	    <div class="modal-info"><strong>진행 상태</strong>
	      <select id="statusSelect"></select>
	    </div>
	
	    <div class="modal-buttons">
	      <button class="save-btn" onclick="saveStatus()">변경사항 저장</button>
	      <button class="close-btn red" onclick="closeModal()">닫기</button>
	    </div>
	  </div>
	</div>


  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
<script>
  window.userType = '${sessionScope.userType}'; 
</script>
<script src="../../resources/js/asOder.js"></script>
</body>
</html>
