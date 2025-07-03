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

<style>
  body {
    font-family: Arial, sans-serif;
    margin: 30px;
    background: #f9f9f9;
  }

  h2, h3 {
    text-align: center;
    color: #333;
  }

  ul {
    list-style: none;
    padding: 0;
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-bottom: 30px;
  }

  ul li a {
    text-decoration: none;
    color: #007BFF;
    font-weight: bold;
  }

  ul li a:hover {
    text-decoration: underline;
  }

  label {
    margin-right: 20px;
    display: inline-block;
  }

  select, input[type="number"] {
    padding: 5px;
    margin-left: 5px;
  }

  button {
    padding: 7px 15px;
    margin-left: 10px;
    background: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  button:hover {
    background: #0056b3;
  }

  canvas {
    display: block;
    margin: 10px auto;
    background: white;
    border-radius: 8px;
    box-shadow: 0 0 10px #ddd;
  }

	.container{
		width: 100%;
	}
	.controller{
		margin: 15px auto;
		display: flex;
		justify-content: center;
	}
	.controller2{
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.content-wrapper {
		width: 95%;
		margin: 0 auto;
		display: flex;
		gap: 20px;
		justify-content: center;
		align-items: flex-start;
	}
	.left-charts {
		display: flex;
		flex-direction: column;
		flex: 3;
	}
	.top-chart {
		margin-bottom: 20px;
	}
	#myChart {
		width: 100%;
		height: auto;
		max-height: 420px;
	}
	#elecRateChart{
	}
	#reasonChart{
	}
	.bottom-charts {
		display: flex;
		align-content: stretch;
		gap: 20px;
	}
	.small-chart {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
	}
	.small-chart canvas {
		width: 100%;
		flex-grow: 1;
		max-height: 350px;
	}
	.table-box {
		background: #f9f9f9;
		box-sizing: border-box;
		margin-top: 50px;
		flex-shrink: 0;
		width: 300px;
		min-width: 300px;
	}
	.table-box table {
		width: 100%l
		border-collapse: collapse;
		margin: 20px auto;
		border-collapse: collapse;
		background: white;
	}
	.table-box td {
		padding: 8px;
		border: 1px solid #ddd;
	}
	footer{
		height: 50px;
	}
</style>
</head>

<body>
   <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	
	<div class="controller">
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
	
		<button id="predictBtn">예측하기</button>
	</div>
	<div class="container">
		<div class="content-wrapper">

			<div class="left-charts">
				<div class="top-chart">
					<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
					<canvas id="myChart"></canvas>
				</div>
				<div class="bottom-charts">
					<div class="small-chart">
						<h3>전기 화재 비율 (연도별)</h3>
						<canvas id="elecRateChart"></canvas>
					</div>
					<div class="small-chart">
						<div class="controller2">
							<h3>주요 전기 사고 원인</h3>
							<button id="fireBtn">화재 사고 원인</button>
							<button id="shockBtn">감전 사고 원인</button>
						</div>
						<canvas id="reasonChart"></canvas>
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
	<footer>
	</footer>
</body>
</html>