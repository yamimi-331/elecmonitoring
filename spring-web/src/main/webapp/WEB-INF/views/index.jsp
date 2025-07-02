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

  table {
    margin: 30px auto;
    border-collapse: collapse;
    width: 50%;
    background: white;
  }

  table, th, td {
    border: 1px solid #ddd;
  }

  td {
    padding: 10px;
    text-align: left;
  }

  canvas {
    display: block;
    margin: 30px auto;
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px #ddd;
  }
  .controller{
  	display: flex;
  	justify-content: center;
  }
</style>
</head>

<body>
	<h2>메인 페이지</h2>
	<ul>
		<li><a href="/">메인 페이지</a></li>
		<li><a href="/asRegister">AS 신고 접수 페이지</a></li>
		<li><a href="/asRequestList">AS 신고 접수 내역 조회 페이지</a></li>
	</ul>
	
	
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
	
	<h3>연도별 전기화재 피해 현황 및 예측 차트</h3>
	<canvas id="myChart" width="800" height="400"></canvas>
		
	<h2>전기 화재 비율 (연도별)</h2>
	<canvas id="elecRateChart" width="600" height="400"></canvas>
	
	<h2>주요 전기 사고 원인</h2>
	<div class="controller">
	<button id="fireBtn">화재 사고 원인</button>
	<button id="shockBtn">감전 사고 원인</button>
	</div>
	<canvas id="reasonChart" width="500" height="500"></canvas>
</body>
</html>