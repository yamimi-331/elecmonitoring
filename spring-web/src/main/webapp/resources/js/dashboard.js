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

//  전역 변수
let currentReasonType = 'fire';
let $currentSelectedProvince = null;
let selectedRegionId = 'seoul';
let selectedRegionName = '서울특별시';
let lastSelectedRegionId = 'seoul';
let summaryCaption;

$(document).ready(function() {

  const $mapElements = $('#koreaMapSvg').find('polyline, path');

  // 지도 기본 스타일
  $mapElements.css({
    'stroke': 'white',
    'stroke-width': '1',
    'fill': 'gray',
    'cursor': 'pointer',
    'transition': 'stroke 0.3s, stroke-width 0.3s, fill 0.3s'
  });

  // 기본: 서울 선택
  $currentSelectedProvince = $('#seoul');
  if ($currentSelectedProvince.length) {
    $currentSelectedProvince.addClass('selected').css({
      'stroke': 'white',
      'stroke-width': '3',
      'fill': '#4287f5'
    });
  }
  $('#regionSelect').val('seoul');
  summaryCaption = document.getElementById('summaryCaption');

  getPrediction(selectedRegionName);
  updateSummary();
  loadReasonChart(currentReasonType);

  // 전국 선택 함수
  function selectNation() {
    $mapElements.addClass('selected').css({
      'stroke': 'white',
      'stroke-width': '3',
      'fill': '#4287f5'
    });

    selectedRegionId = 'nation';
    selectedRegionName = '전국';
    lastSelectedRegionId = 'nation';
    $currentSelectedProvince = null;

    $('#regionSelect').val('nation');

    getPrediction(selectedRegionName);
    updateSummary();
  }

  // 전체 선택 해제 함수
  function deselectAll() {
    $mapElements.removeClass('selected').css({
      'stroke': 'white',
      'stroke-width': '1',
      'fill': 'gray'
    });

    selectedRegionId = '';
    selectedRegionName = '';
    lastSelectedRegionId = '';
    $currentSelectedProvince = null;

    $('#regionSelect').val('');
  }

  // #regionSelect 변경 시
  $('#regionSelect').change(function() {
    const regionId = $(this).val();
    const regionText = $('#regionSelect option:selected').text();

    if (regionId === 'nation') {
      selectNation();
    } else if (regionId === '') {
      // 선택 해제 옵션
      deselectAll();
    } else {
      deselectAll();

      const $target = $('#' + regionId);
      if ($target.length) {
        $target.addClass('selected').css({
          'stroke': 'white',
          'stroke-width': '3',
          'fill': '#4287f5'
        });
        selectedRegionId = regionId;
        selectedRegionName = regionText;
        lastSelectedRegionId = regionId;
        $currentSelectedProvince = $target;

        getPrediction(selectedRegionName);
        updateSummary();
      }
    }
  });

  // #predictYear 변경 시
  $('#predictYear').change(function() {
    getPrediction(selectedRegionName);
  });

  // #yearSelect 변경 시
  $('#yearSelect').change(function() {
    updateSummary();
    loadReasonChart(currentReasonType);
  });

  // 원인 버튼
  $('#fireBtn').click(function() {
    currentReasonType = 'fire';
    loadReasonChart('fire');
  });

  $('#shockBtn').click(function() {
    currentReasonType = 'shock';
    loadReasonChart('shock');
  });

  // 지도 Hover
  $mapElements.hover(
    function() {
      if (!$(this).hasClass('selected')) {
        $(this).css({
          'stroke': 'white',
          'stroke-width': '4',
          'fill': '#a8cfff'
        });
      }
    },
    function() {
      if (!$(this).hasClass('selected')) {
        $(this).css({
          'stroke': 'white',
          'stroke-width': '1',
          'fill': 'gray'
        });
      }
    }
  );

  // 🔹 지도 클릭
  $mapElements.on('click', function() {
    const clickedId = $(this).attr('id');

    if (selectedRegionId === 'nation') {
      // 전국 선택 상태면 → 개별 선택
      deselectAll();
    }

    if (selectedRegionId === clickedId) {
      // 같은 지역 다시 클릭 → 선택 해제
      deselectAll();
    } else {
      // 다른 지역 선택
      deselectAll();

      $(this).addClass('selected').css({
        'stroke': 'white',
        'stroke-width': '3',
        'fill': '#4287f5'
      });

      selectedRegionId = clickedId;
      selectedRegionName = $('#regionSelect option[value="' + clickedId + '"]').text();
      lastSelectedRegionId = clickedId;
      $currentSelectedProvince = $(this);

      $('#regionSelect').val(clickedId);

      getPrediction(selectedRegionName);
      updateSummary();
    }
  });

  // 빈영역 클릭 → 전국 선택 or 해제
  $('#koreaMapSvg').on('click', function(e) {
    if (!$(e.target).is('polyline, path')) {
      if (selectedRegionId === 'nation') {
        deselectAll();
      } else {
        selectNation();
      }
    }
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
  const region = selectedRegionName;
  const year = $('#yearSelect').val();
  summaryCaption.textContent = `${year}년 ${selectedRegionName} 전기 재해 통계`;

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