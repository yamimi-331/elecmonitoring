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
<script src="../../resources/js/dashboard.js?after"></script>
<link rel="stylesheet" href="../../resources/css/index.css?after" />
<link rel="stylesheet" href="../../resources/css/common.css?after" />
</head>
<body>
	 <div class="wrapper">
	<h2 class="header-title">전기재해 모니터링 시스템</h2>
   <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	<main class="main">
		<div class="container">
			<div class="content-wrapper">
				<!-- 좌측 테이블 start----------------- -->
				<div class="table-box">
					<div class="controll-title">
						<label for="yearSelect">지역 </label>
						<select class="controller-select" id="regionSelect">
							<option value="seoul">서울특별시</option>
							<option value="busan">부산광역시</option>
							<option value="incheon">인천광역시</option>
							<option value="daegu">대구광역시</option>
							<option value="daejeon">대전광역시</option>
							<option value="gwangju">광주광역시</option>
							<option value="sejong">세종특별자치시</option>
							<option value="ulsan">울산광역시</option>
							<option value="gyeonggi">경기도</option>
							<option value="gangwon">강원도</option>
							<option value="chungbuk">충청북도</option>
							<option value="chungnam">충청남도</option>
							<option value="jeonbuk">전라북도</option>
							<option value="jeonnam">전라남도</option>
							<option value="gyeongbuk">경상북도</option>
							<option value="gyeongnam">경상남도</option>
							<option value="jeju">제주특별자치도</option>
							<option value="nation">전국</option>
							<option value="">선택 해제</option>
						</select>
					</div>
					<div id="map-container">
						<%@ include file="../../resources/img/koreamap.svg" %>
					</div>
					<div class="controll-title">
						<label for="yearSelect">연도 </label>
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
					<table class="summary-table">
						<caption id="summaryCaption">서울특별시의 전기 재해 현황</caption>
						<colgroup>
							<col width="45%">
							<col width="55%">
						</colgroup>
						<tr><td>화재 건 수</td><td></td></tr>
						<tr><td>화재 피해액</td><td></td></tr>
						<tr><td>화재 부상자 수</td><td></td></tr>
						<tr><td>화재 사망자 수</td><td></td></tr>
						<tr><td>감전 부상자 수</td><td></td></tr>
						<tr><td>감전 사망자 수</td><td></td></tr>
					</table>
				</div>
				<!-- 좌측 테이블 end----------------- -->
				<!-- 우측 차트 start----------------- -->
				<div class="right-section">
					<div class="top-chart">
						<div class="chart-header">
							<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
							<div class="range-group">
						      	<select class="controller-predict" id="predictYear">
									<option value="3">예측 기간 3년</option>
									<option value="5">예측 기간 5년</option>
									<option value="7">예측 기간 7년</option>
									<option value="10">예측 기간 10년</option>
								</select>
						    </div>
						</div>
						<canvas class="chartCanvas" id="myChart"></canvas>
					</div>
					<div class="bottom-charts">
						<div class="small-chart">
							<div class="controller">
								<h3>주요 전기 사고 원인</h3>
								<button class="reasonBtn" id="fireBtn">화재</button>
								<button class="reasonBtn" id="shockBtn">감전</button>
							</div>
							<canvas class="chartCanvas" id="reasonChart"></canvas>
						</div>
						<div class="small-chart">
							<h3>전기 화재 비율 (연도별)</h3>
							<canvas class="chartCanvas" id="elecRateChart"></canvas>
						</div>
					</div>
				</div>
				<!-- 우측 차트 end----------------- -->
			</div>
		</div>
	</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
	
</body>
</html>