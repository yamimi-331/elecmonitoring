<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="../../resources/js/dashboard.js"></script>
<link rel="stylesheet" href="../../resources/css/index.css?after" />
</head>

<body>
	<!-- 공통헤더 -->
	<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>
	<!-- 상단 컨트롤러: (지역 예측 연도수 예측 연도 버튼) -->
	<div class="controller">
		<label>지역: <select id="regionSelect">
				<option value="서울특별시">서울특별시</option>
				<option value="부산광역시">부산광역시</option>
				<option value="대구광역시">대구광역시</option>
				<!-- 등등 -->
		</select>
		</label> <label>예측 연도 수: <input type="number" id="yearsInput"
			value="3" min="1" max="10">
		</label> <label>예측 연도: <select id="yearSelect">
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

		<button id="predictBtn">예측하기</button>
		<button id="fireBtn">화재 사고 원인</button>
		<button id="shockBtn">감전 사고 원인</button>
	</div>
	<!-- 상단 컨트롤러: (지역 예측 연도수 예측 연도 버튼) end ----------------------------- -->

	<main class="main-container">
		<div class="inner-container">
			<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
			<canvas class="chart-canvas"  id="myChart"></canvas>
		</div>
		<div class="inner-container">
			<h3>전기 화재 비율 (연도별)</h3>
			<canvas class="chart-canvas"  id="elecRateChart"></canvas>
		</div>
		<div class="inner-container">
			<h3>주요 전기 사고 원인</h3>
			<canvas class="chart-canvas"  id="reasonChart" ></canvas>
		</div>
	</main>
	
	<div>
		<table class="data-table">
			<tr>
				<td>화재 건 수</td>
				<td></td>
			</tr>
			<tr>
				<td>화재 피해액</td>
				<td></td>
			</tr>
			<tr>
				<td>화재 부상자 수</td>
				<td></td>
			</tr>
			<tr>
				<td>화재 사망자 수</td>
				<td></td>
			</tr>
			<tr>
				<td>감전 부상자 수</td>
				<td></td>
			</tr>
			<tr>
				<td>감전 사망자 수</td>
				<td></td>
			</tr>
		</table>
	</div>
</body>
</html>