<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
	<label>지역:
		<select id="regionSelect">
			<option value="서울특별시">서울특별시</option>
			<option value="부산광역시">부산광역시</option>
			<option value="대구광역시">대구광역시</option>
			<!-- 등등 -->
		</select>
	</label>

	<label>예측 연도 수:
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

</body>


</html>