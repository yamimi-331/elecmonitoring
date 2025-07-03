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
   <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	<main>
		<div class="controller">
			<label class="controller-label">지역: </label>
			<select class="controller-select" id="regionSelect">
				<option value="서울특별시">서울특별시</option>
				<option value="부산광역시">부산광역시</option>
				<option value="대구광역시">대구광역시</option>
				<!-- 등등 -->
			</select>
			
			<label class="controller-label">예측 연도 수: </label>
			<select class="controller-select" id="predictYear">
				<option value="3">3개월</option>
				<option value="6">6개월</option>
				<option value="9">9개월</option>
				<option value="12">12개월</option>
			</select>
			
			<label class="controller-label">연도:</label>
			<select class="controller-select" id="yearSelect">
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
		</div>
		<div class="container">
			<div class="content-wrapper">
	
				<div class="left-charts">
					<div class="top-chart">
						<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
						<canvas class="chartCanvas" id="myChart"></canvas>
					</div>
					<div class="bottom-charts">
						<div class="small-chart">
							<h3>전기 화재 비율 (연도별)</h3>
							<canvas class="chartCanvas" id="elecRateChart"></canvas>
						</div>
						<div class="small-chart">
							<div class="controller2">
								<h3>주요 전기 사고 원인</h3>
								<button class="reasonBtn" id="fireBtn">화재 사고 원인</button>
								<button class="reasonBtn" id="shockBtn">감전 사고 원인</button>
							</div>
							<canvas class="chartCanvas" id="reasonChart"></canvas>
						</div>
					</div>
				</div>
	
				<div class="table-box">
					<table>
						<tbody>
							<tr><td>화재 건 수 :</td><td></td></tr>
							<tr><td>화재 피해액 :</td><td></td></tr>
							<tr><td>화재 부상자 수 :</td><td></td></tr>
							<tr><td>화재 사망자 수 :</td><td></td></tr>
							<tr><td>감전 부상자 수 :</td><td></td></tr>
							<tr><td>감전 사망자 수 :</td><td></td></tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</main>
	<footer>
	</footer>
</body>
</html>