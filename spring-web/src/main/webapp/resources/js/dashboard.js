/**
 * 
 */
 function getPrediction() {
    const region = $('#regionSelect').val();
    const years = $('#yearsInput').val();

   $.ajax({
	  url: `http://localhost:8000/predict?years=${years}&region=${region}`,
	  type: 'GET',
	  dataType: 'json',
	  success: function(data) {
	    console.log(data);
	
	    let parsed = typeof data === 'string' ? JSON.parse(data) : data;
	
	    if (parsed.status === 'success') {
	      drawComboChart(parsed.result);
	    } else {
	      alert('데이터 오류!');
	    }
	  },
	  error: function(err) {
	    console.error(err);
	    alert('예측 요청 실패!');
	  }
	});


}

$(document).ready(function() {
    //$('#predictBtn').click(function() {
    //    getPrediction();
    //});
	
	loadReasonChart('fire');
	
    $('#regionSelect').change(function() {
        getPrediction();
    });
    
    $('#fireBtn').click(function() {
    	loadReasonChart('fire');
    });
    $('#shockBtn').click(function() {
    	loadReasonChart('shock');
    });
});

let myChart;

function drawComboChart(data) {
  const ctx = document.getElementById('myChart').getContext('2d');

  const labels = data.map(item => item.year);
  const amountActual = data.map(item => item.amount_actual);
  const amountPredicted = data.map(item => item.amount_predicted);
  const countActual = data.map(item => item.count_actual);
  const countPredicted = data.map(item => item.count_predicted);
  const regionName = data[0].region;

  if (myChart) {
	  myChart.data.labels = labels;
	
	  myChart.data.datasets[0].data = amountActual;
	  myChart.data.datasets[1].data = amountPredicted;
	  myChart.data.datasets[2].data = countActual;
	  myChart.data.datasets[3].data = countPredicted;
	
	  myChart.options.plugins.title.text = `${regionName} 전기화재 예측 복합차트`;
	
	  myChart.update();
	} else {
	  myChart = new Chart(ctx, {
	    type: 'bar',
	    data: {
	      labels: labels,
	      datasets: [
	        {
	          type: 'line',
	          label: '실제 피해금액',
	          data: amountActual,
	          borderColor: 'rgba(255, 99, 132, 1)',
	          backgroundColor: 'rgba(255, 99, 132, 0.2)',
	          yAxisID: 'y',
	          tension: 0.3
	        },
	        {
	          type: 'line',
	          label: '예측 피해금액',
	          data: amountPredicted,
	          borderColor: 'rgba(153, 102, 255, 1)',
	          backgroundColor: 'rgba(153, 102, 255, 0.2)',
	          yAxisID: 'y',
	          tension: 0.3
	        },
	        {
	          type: 'bar',
	          label: '실제 발생건수',
	          data: countActual,
	          backgroundColor: 'rgba(54, 162, 235, 0.6)',
	          yAxisID: 'y2'
	        },
	        {
	          type: 'bar',
	          label: '예측 발생건수',
	          data: countPredicted,
	          backgroundColor: 'rgba(255, 206, 86, 0.6)',
	          yAxisID: 'y2'
	        }
	      ]
	    },
	    options: {
	      responsive: true,
	      interaction: {
	        mode: 'index',
	        intersect: false,
	      },
	      animation: {
	        duration: 1000,
	        easing: 'easeOutQuart'
	      },
	      stacked: false,
	      plugins: {
	        title: {
	          display: true,
	          text: `${regionName} 전기화재 예측 복합차트`
	        }
	      },
	      scales: {
	        y: {
	          type: 'linear',
	          display: true,
	          position: 'left',
	          title: {
	            display: true,
	            text: '피해금액'
	          }
	        },
	        y2: {
	          type: 'linear',
	          display: true,
	          position: 'right',
	          title: {
	            display: true,
	            text: '발생건수'
	          },
	          grid: {
	            drawOnChartArea: false,
	          },
	        }
	      }
	    }
	  });
	}

}


// 전체 화재수 대비 전기 화재수의 비율
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
	

// 화재 및 감전 원인
let reason = null;

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
