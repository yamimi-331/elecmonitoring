<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
 <script src="../../resources/js/dashboard.js"></script>
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

	<label>예측 연도:
		<select id="yearSelect">
			<option value="2023">2023</option>
			<option value="2022">2022</option>
			<option value="2021">2021</option>
			<option value="2020">2020</option>
			<option value="2019">2019</option>
			<option value="2018">2018</option>
			<option value="2017">2017</option>
			<option value="2016">2016</option>
			<option value="2015">2015</option>
			<option value="2014">2014</option>
			<option value="2013">2013</option>
		</select>
	</label>
	
	<table>
		<tr>
			<td>화재 건 수 : </td>
			<td></td>
		</tr>
		<tr>
			<td>화재 피해액 : </td>
			<td></td>
		</tr>
		<tr>
			<td>화재 부상자 수 : </td>
			<td></td>
		</tr>
		<tr>
			<td>화재 사망자 수 : </td>
			<td></td>
		</tr>
		<tr>
			<td>감전 부상자 수 : </td>
			<td></td>
		</tr>
		<tr>
			<td>감전 사망자 수 : </td>
			<td></td>
		</tr>
	</table>

	<div id="resultBox"></div>
	<canvas id="myChart" width="800" height="400"></canvas>
		
	<h2>전기 화재 비율 (연도별)</h2>
	<canvas id="elecRateChart" width="600" height="400"></canvas>
	
	<h2>주요 전기 사고 원인</h2>
	<button id="fireBtn">화재 사고 원인</button>
	<button id="shockBtn">감전 사고 원인</button>
	<canvas id="reasonChart" width="500" height="500"></canvas>
</body>
</html>