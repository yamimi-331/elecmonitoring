/**
 *
 */
function getPrediction(region = null) {
	
	if(region == null){
		region = '서울특별시';
	}
  const years = $('#predictYear').val();

  fetch(`http://localhost:8000/predict?years=${years}&region=${region}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('네트워크 응답 실패');
      }
      return response.json();
    })
    .then(data => {
      let parsed = data; // 대부분의 fetch API는 자동으로 JSON 파싱을 해줍니다.

      if (parsed.status === 'success') {
        drawComboChart(parsed.result);
      } else {
        alert('데이터 오류! (status: fail)');
      }
    })
    .catch(err => {
      console.error(err);
      alert('예측 요청 실패!');
    });
}

const regionMap = {
  gangwon: '강원도',
  jeonbuk: '전라북도',
  jeonnam: '전라남도',
  chungbuk: '충청북도',
  chungnam: '충청남도',
  gyeonggi: '경기도',
  gyeongbuk: '경상북도',
  gyeongnam: '경상남도',
  seoul: '서울특별시',
  busan: '부산광역시',
  daegu: '대구광역시',
  incheon: '인천광역시',
  gwangju: '광주광역시',
  daejeon: '대전광역시',
  ulsan: '울산광역시',
  sejong: '세종특별자치시',
  jeju: '제주특별자치도',
  koreaMapSvg: '전국'
};

let currentReasonType = 'fire';
let $currentSelectedProvince = null;  // 여기서 전역으로 선언
let selectedRegionName = '서울특별시';
let summaryCaption;

$(document).ready(function() {
  // 초기 로드 시 예측 데이터 및 요약 정보 로드
  getPrediction(); // mapRegionId, mapRegionValue 없이 호출되므로 #regionSelect 값 사용
  summaryCaption = document.getElementById('summaryCaption');
  updateSummary();
  loadReasonChart(currentReasonType);

  // #predictYear 변경 시 getPrediction 호출 (기존 로직 유지)
  $('#predictYear').change(function() {
    getPrediction(); // #regionSelect 값을 사용하도록 호출
  });

  // #regionSelect 변경 시 getPrediction 및 updateSummary 호출 (기존 로직 유지)
  $('#regionSelect').change(function() {
    getPrediction(); // #regionSelect 값을 사용하도록 호출
    updateSummary();
  });

  // #yearSelect 변경 시 updateSummary 및 loadReasonChart 호출 (기존 로직 유지)
  $('#yearSelect').change(function() {
    updateSummary();
    loadReasonChart(currentReasonType);
  });

  // #fireBtn 클릭 시
  $('#fireBtn').click(function() {
    currentReasonType = 'fire';
    loadReasonChart('fire');
  });

  // #shockBtn 클릭 시
  $('#shockBtn').click(function() {
    currentReasonType = 'shock';
    loadReasonChart('shock');
  });

  $('#koreaMapSvg').on('click', function(e) {
	  const clickedElement = e.target;
	 
	  // polyline이면 부모나 id 확인
	  const provinceId = $(clickedElement).attr('id');
	  console.log('provinceId:', provinceId);
		
	  if (provinceId) {
	    if ($currentSelectedProvince) {
	      $currentSelectedProvince.removeClass('selected');
	    }
	
	    $(clickedElement).addClass('selected');
	    $currentSelectedProvince = $(clickedElement);
	
	    const regionValue = regionMap[provinceId];
		console.log('파라미터용 :', regionValue);
	    if (regionValue) {
	      selectedRegionName = regionValue
	      getPrediction(regionValue);
	      updateSummary();
	    } else {
	      alert(`ID: ${provinceId} 는 매핑되지 않았습니다.`);
	    }
	  }
	});
	
	  const range = document.getElementById('predictYear');
	  const label = document.getElementById('rangeValue');
	
	  range.addEventListener('change', function() {
	    const realValue = this.value;
	    console.log("realValue " + realValue);
	    console.log("this.value " + this.value);
	    label.textContent = realValue;
	  });
});


// 나머지 함수들은 그대로 유지
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
          datasets: [
            {
              label: '전기 화재 건수',
              data: elec,
              backgroundColor: 'rgba(255, 206, 86, 0.7)',
              borderColor: 'rgba(255, 206, 86, 1)',
              fill: true,
              tension: 0.4
            },
            {
              label: '전체 화재 건수',
              data: total,
              backgroundColor: 'rgba(54, 162, 235, 0.2)',
              borderColor: 'rgba(54, 162, 235, 1)',
              fill: true,
              tension: 0.4
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
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
  const year = $('#yearSelect').val();
  const url = type === 'fire' ? 'http://127.0.0.1:8000/fire_reason?year=' + year : 'http://127.0.0.1:8000/shock_reason?year=' + year;

  fetch(url)
    .then(response => response.json())
    .then(data => {
      if (data.status == 'success') {
        const result = data.result;
        const labels = Object.keys(result);
        const values = Object.values(result);

        //기존 차트 제거
        if (reason) {
          reason.destroy();
        }

        const ctx = document.getElementById('reasonChart').getContext('2d');
        reason = new Chart(ctx, {
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
              ],
              hoverOffset: 30
            }]
          },
          options: {
            responsive: true,
            interaction: {
              mode: 'nearest',
              intersect: true
            },
            plugins: {
              tooltip: {
                enabled: true,
                callbacks: {
                  label: function(context) {
                    const label = context.label || '';
                    const value = context.raw;
                    const total = context.chart._metasets[context.datasetIndex].total;
                    const percentage = ((value / total) * 100).toFixed(1);
                    return `${label}: ${value}건 (${percentage}%)`;
                  }
                }
              },
              title: {
                display: true,
                text: year + '년 ' + (type === 'fire' ? "전기 화재 주요 원인" : "감전 주요 원인")
              },
              legend: {
                position: 'right'
              }
            }
          }
        });
      } else {
        alert("데이터를 불러오지 못했습니다.");
      }
    });
}


// 테이블의 값들을 넣을 <td>들을 미리 셀렉트
const tableCells = document.querySelectorAll('table tr td:nth-child(2)');
// select 값 테이블로 보여주기
function updateSummary() {
  summaryCaption.textContent = `${selectedRegionName}의 전기 재해 현황`;
  const region = selectedRegionName;
  const year = $('#yearSelect').val();

  fetch("http://127.0.0.1:8000/summary?region=" + encodeURIComponent(region) + "&year=" + year)
    .then(response => response.json())
    .then(data => {
      if (data.status === 'success') {
        const result = data.result;
        const summaryTable = $('.summary-table');
        const rows = summaryTable.find('tr');
        // 순서대로 테이블 td에 값 채우기
        $(rows[0]).find('td').eq(1).text(result.fire_count.toLocaleString() + ' 건');
        $(rows[1]).find('td').eq(1).text(result.fire_amount.toLocaleString() + ' 원');
        $(rows[2]).find('td').eq(1).text(result.fire_injury.toLocaleString() + ' 명');
        $(rows[3]).find('td').eq(1).text(result.fire_death.toLocaleString() + ' 명');
        $(rows[4]).find('td').eq(1).text(result.shock_injury.toLocaleString() + ' 명');
        $(rows[5]).find('td').eq(1).text(result.shock_death.toLocaleString() + ' 명');
      } else {
        alert('데이터를 불러오는 데 실패했습니다.');
      }
    })
    .catch(err => {
      console.error(err);
      alert('서버 오류 발생');
    });
}