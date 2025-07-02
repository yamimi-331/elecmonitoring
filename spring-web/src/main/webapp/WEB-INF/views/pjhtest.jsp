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
	<h2>전기 화재 비율 (연도별)</h2>
	<canvas id="elecRateChart" width="600" height="400"></canvas>
	
	<h2>주요 전기 사고 원인</h2>
	<button onclick="loadReasonChart('fire')">화재 사고 원인</button>
	<button onclick="loadReasonChart('shock')">감전 사고 원인</button>
	<canvas id="reasonChart" width="500" height="500"></canvas>
</body>
<script>
	let reason = null

	// FastAPI 서버 주소에서 데이터 가져오기
	fetch('http://127.0.0.1:8000/elec-rate')
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				const raw = data.result;
				// 연도
				const labels = Object.keys(raw);
				// 전체 화재 수, 전기 화재 수
				const total = labels.map(year => raw[year][0]);
				const elec = labels.map(year => raw[year][1]);
				
				const ctx = document.getElementById('elecRateChart').getContext('2d');
				new Chart(ctx, {
					type: 'line',
					data: {
						labels: labels,
						datasets: [{
							label: '전체 화재 수',
							data: total,
							backgroundColor: 'rgba(54, 162, 235, 0.2)',
							borderColor: 'rgba(54, 162, 235, 1)',
							fill: true,
							tension: 0.4},
						{
							label: '전기 화재 수',
							data: elec,
							backgroundColor: 'rgba(255, 99, 132, 0.2)',
							borderColor: 'rgba(255, 99, 132, 1)',
							fill: true,
							tension: 0.4
						}]
					},
					options: {
						responsive: true,
						plugins: {
							title: {
								display: true,
								text: '연도별 화재 발생 추이'
							}
						},
						scales: {
							y: {
								beginAtZero: true,
								max: 50000,
								title: {
								display: true,
								text: '화재 건수'
								}
							}
						}
					}
				});
			} else {
				alert("데이터를 불러오는 데 실패했습니다.");
			}
		})
		.catch(error => {
			console.error("에러 발생:", error);
		});
	
	function loadReasonChart(type) {
		const url = type === 'fire' ? 'http://127.0.0.1:8000/fire_reason' : 'http://127.0.0.1:8000/shock_reason';
		
		fetch(url)
			.then(response => response.json())
			.then(data => {
				if(data.status == 'success'){
					const result = data.result;
					const labels = Object.keys(result);
					const values = Object.values(result);
					
					//기존 차트 제거
					if(reason){
						reason.destroy();
					}
					
					const ctx = document.getElementById('reasonChart').getContext('2d');
					reason = new Chart(ctx,{
						type: 'doughnut',
						data: {
							labels: labels,
							datasets: [{
								label: type === 'fire' ? "화재 원인" : "감전 원인",
								data: values,
								backgroundColor: [
									'#ff6384', '#36a2eb', '#ffcd56', '#4bc0c0',
									'#9966ff', '#c9cbcf', '#ff9f40', '#9ccc65',
									'#ba68c8', '#f06292'
								]
							}]
						},
						options:{
							responsive: true,
							plugins: {
								title: {
									display: true,
									text: type == 'fire' ? '전기 화재 주요 요인' : '감전 주요 요인'
								},
								legend: {
									position: 'bottom'
								}
							}
						}
					});
				} else {
					alert("데이터를 불러오지 못했습니다.");
				}
			});
	}
	
	window.onload = () => {
		loadReasonChart('fire');
	};
  </script>
</html>