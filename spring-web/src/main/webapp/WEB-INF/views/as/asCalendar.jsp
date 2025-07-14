<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AS 전체 일정</title>
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css" rel="stylesheet" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/locales/ko.global.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/asOder.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">AS 전체 일정</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<h2>AS 전체 일정</h2>
			<div class="container">
				<div class="calendar-container">
					<c:if test="${currentUserInfo.staff_role eq 'admin'}">
						<div class="search-section">
							<input type="text" id="searchCalendarStaff" placeholder="담당자">
							<button id="btnCalendarSearch">조회</button>
						</div>
					</c:if>
					<div></div>
					<div class="calendar-legend">
						<span style="background:#439b43" class="legend-dot"></span> 신고 접수
						<span style="background:#f0ad4e" class="legend-dot"></span> A/S 작업 중
						<span style="background:#ff6b6b" class="legend-dot"></span> 작업 취소
						<span style="background:#cccccc" class="legend-dot"></span> 작업 완료
					</div>
				</div>
				<div id="calendar"></div>
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

<script src="../../resources/js/asOder.js"></script>
</body>
</html>
