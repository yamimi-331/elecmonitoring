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

    $('#regionSelect').change(function() {
        getPrediction();
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
