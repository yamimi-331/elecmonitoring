<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AS 일정 목록</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
/* 테이블 스타일 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

table th, table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
}

table thead {
  background-color: #f2f2f2;
}

table tr:nth-child(even) {
  background-color: #f9f9f9;
}

table tr:hover {
  background-color: #f1f1f1;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  z-index: 9999;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0,0,0,0.5);
  display: none;
}

.modal-content {
  background-color: #fff;
  margin: 10% auto;
  padding: 30px;
  border: 1px solid #888;
  width: 500px;
  border-radius: 8px;
  position: relative;
}

.modal-content .close {
  position: absolute;
  right: 15px;
  top: 10px;
  font-size: 24px;
  font-weight: bold;
  cursor: pointer;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
}

.modal-content .modal-info {
  margin-bottom: 10px;
}

.modal-content label {
  display: block;
  margin-top: 10px;
}

.modal-content select {
  width: 100%;
  padding: 8px;
  margin-bottom: 20px;
}

.modal-content button {
  padding: 8px 16px;
  background-color: #1e90ff;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.modal-content button:hover {
  background-color: #005ecb;
}
</style>
</head>
<body>
  <div class="wrapper">
    <h2 class="header-title">전기재해 모니터링 시스템</h2>
    <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
    <main class="main">
      <h2>AS 일정 목록</h2>
      <table>
        <thead>
          <tr>
            <th>코드번호</th>
            <th>신고 제목</th>
            <th>담당자명</th>
            <th>진행 상태</th>
            <th>일정 확인</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="as" items="${asList}">
            <tr>
              <td>${as.as_cd}</td>
              <td>${as.as_title}</td>
              <td>${as.staff_nm}</td>
              <td>${as.as_status}</td>
              <td>
                <button type="button" onclick="openModal(${as.as_cd})">일정 확인</button>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty asList}">
            <tr>
              <td colspan="5">조회된 일정이 없습니다.</td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </main>
  </div>

  <!-- 모달 -->
  <div id="statusModal" class="modal">
    <div class="modal-content">
      <span class="close" onclick="closeModal()">&times;</span>
      <h3>AS 상세 정보</h3>

      <div class="modal-info"><strong>코드번호:</strong> <span id="modalAsCd"></span></div>
      <div class="modal-info"><strong>신고 제목:</strong> <span id="modalAsTitle"></span></div>
      <div class="modal-info"><strong>담당자명:</strong> <span id="modalStaffNm"></span></div>
      <div class="modal-info"><strong>상세 정보:</strong> <span id="modalAsContent"></span></div>
      <div class="modal-info"><strong>예약 일시:</strong> <span id="modalAsDate"></span></div>
      <div class="modal-info"><strong>위치:</strong> <span id="modalAsAddr"></span></div>
      <div class="modal-info"><strong>시설물:</strong> <span id="modalAsFacility"></span></div>
      <div class="modal-info"><strong>진행 상태:</strong>
        <select id="statusSelect"></select>
      </div>
      <button onclick="saveStatus()">변경사항 저장</button>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/common/footer.jsp" %>

<script src="../../resources/js/asOder.js"></script>
</body>
</html>
