<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h2>메인 페이지</h2>
	<ul>
		<li><a href="/">메인 페이지</a></li>
		<li><a href="/asRegister">AS 신고 접수 페이지</a></li>
		<li><a href="/asRequestList">AS 신고 접수 내역 조회 페이지</a></li>
	</ul>
	
	
  <label>지역:
    <select id="regionSelect">
      <option value="서울특별시">서울특별시</option>
      <option value="부산광역시">부산광역시</option>
      <option value="대구광역시">대구광역시</option>
      <!-- 등등 -->
    </select>
  </label>

  <label>예측 연도 수:
    <input type="number" id="yearsInput" value="3" min="1" max="10">
  </label>

  <button id="predictBtn">예측하기</button>

  <div id="resultBox"></div>

  <script>
   
  </script>
</body>
</html>