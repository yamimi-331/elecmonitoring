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
<style>
   .korea-province {
  fill: #ccc;
  stroke: #333;
  stroke-width: 1;
  cursor: pointer;
  transition: fill 0.3s;
}

.korea-province:hover {
  fill: #999;
}

.korea-province.selected {
  fill: orange;
  stroke: #222;
  stroke-width: 2;
}

</style>
</head>
<body>
	 <div class="wrapper">
   <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	<main class="main">
		<div class="container">
			<div class="content-wrapper">
				<!-- 좌측 테이블 start----------------- -->
				<div class="table-box">
					<div id="map-container">
						<%@ include file="../../resources/img/koreamap.svg" %>
					</div>
					<table class="controll-table">
						<colgroup>
							<col width="50%">
							<col width="50%">
						</colgroup>
						<tr>
							<td>연도</td>
							<td>
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
							</td>
						</tr>
					</table>
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
						<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
						<label for="customRange">예측 연도 수: <span id="rangeValue">3</span></label><br>
						<input id="predictYear" type="range" min="3" max="12" step="3" value="0">
						<canvas class="chartCanvas" id="myChart"></canvas>
					</div>
					<div class="bottom-charts">
						<div class="small-chart">
							<div class="controller">
								<h3>주요 전기 사고 원인</h3>
								<button class="reasonBtn" id="fireBtn">화재 사고 원인</button>
								<button class="reasonBtn" id="shockBtn">감전 사고 원인</button>
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
	<footer>
	</footer>
	
	<script>
  const allowedValues = [3, 5, 7, 9]; // 순서 중요!
  const range = document.getElementById('predictYear');
  const label = document.getElementById('rangeValue');

  range.addEventListener('change', function() {
    const realValue = this.value;
    console.log("realValue " + realValue);
    console.log("this.value " + this.value);
    label.textContent = realValue;
  });
</script>
</body>
</html>