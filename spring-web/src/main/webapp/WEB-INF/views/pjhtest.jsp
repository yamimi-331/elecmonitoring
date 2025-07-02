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
<script>
// 테이블의 값들을 넣을 <td>들을 미리 셀렉트
const tableCells = document.querySelectorAll('table tr td:nth-child(2)');

function updateSummary() {
	const region = document.getElementById('regionSelect').value;
	const year = document.getElementById('yearSelect').value;

	fetch("http://127.0.0.1:8000/summary?region=" + encodeURIComponent(region) + "&year=" + year)
		.then(response => response.json())
		.then(data => {
			if (data.status === 'success') {
				const r = data.result;
				// 순서대로 테이블 td에 값 채우기
				tableCells[0].textContent = r.fire_count.toLocaleString();
				tableCells[1].textContent = r.fire_amount.toLocaleString();
				tableCells[2].textContent = r.fire_injury.toLocaleString();
				tableCells[3].textContent = r.fire_death.toLocaleString();
				tableCells[4].textContent = r.shock_injury.toLocaleString();
				tableCells[5].textContent = r.shock_death.toLocaleString();
			} else {
				alert('데이터를 불러오는 데 실패했습니다.');
			}
		})
		.catch(err => {
			console.error(err);
			alert('서버 오류 발생');
		});
}

// 이벤트 연결
document.getElementById('regionSelect').addEventListener('change', updateSummary);
document.getElementById('yearSelect').addEventListener('change', updateSummary);

// 페이지 로드 시 초기값 호출
window.addEventListener('load', updateSummary);
</script>
</html>