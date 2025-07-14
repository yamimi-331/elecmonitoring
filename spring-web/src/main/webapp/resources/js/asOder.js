/**
 * 문서 로딩 시 실행되는 초기화 함수
 * - 오늘 날짜를 기본값으로 셋팅
 * - jQuery UI datepicker 적용
 * - 조회 버튼 클릭 시 fetchSchedule 호출
 * - 페이지 로드 시 오늘 날짜 일정 조회
 */
$(function() {
	const today = new Date();
	const sevenDaysLater = new Date();
	sevenDaysLater.setDate(today.getDate() + 7);
	
	const formatDate = (date) => date.toISOString().split('T')[0]; // YYYY-MM-DD 형식 오늘 날짜

	$('#startDate').val(formatDate(today));  // input에 오늘 날짜 기본값 설정
	$('#endDate').val(formatDate(sevenDaysLater));
	const staffInfo = $('#searchStaff').val() || '';
	
	fetchSchedule(formatDate(today), formatDate(sevenDaysLater), '');
	
	// 조회 버튼 클릭 시 선택한 날짜 일정 조회
	$('#btnSearch').click(function() {
		const startDate = $('#startDate').val().substring(0, 10);
		const endDate = $('#endDate').val().substring(0, 10);
		const staffInfo = $('#searchStaff').val() || '';
		
		if (startDate > endDate) {
			alert('시작일은 종료일보다 이전이어야 합니다.');
			return;
		}
		
		const maxRange = 93;
		const dateDiff = (new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24);
		if(dateDiff > maxRange) {
			alert("조회기간은 최대 3개월 까지만 가능합니다.");
			return;
		}
    	fetchSchedule(startDate, endDate, staffInfo);
  }); 
});

/**
 * 지정한 날짜에 해당하는 AS 일정 데이터를 서버에서 가져오는 AJAX 함수
 * @param {string} date - 조회할 날짜 (YYYY-MM-DD)
 */
function fetchSchedule(startDate, endDate, staffInfo) {
  $.ajax({
    url: `/as/schedule`,  // 서버 API 엔드포인트
    method: 'GET',
    data: { startDate, endDate, staffInfo }, // 쿼리 파라미터로 날짜 전달
    dataType: 'json',
    success: function(data) {
      renderTable(data);  // 받아온 데이터로 테이블 그리기
    },
    error: function() {
      alert('일정 정보를 불러오는 중 오류가 발생했습니다.');
    }
  });
}

/**
 * AS 일정 데이터를 받아서 테이블에 출력하는 함수
 * @param {Array} data - AS 일정 객체 배열
 */
function renderTable(data) {
  const tbody = $('#asTable tbody');
  tbody.empty(); // 기존 테이블 내용 비우기

  if (data.length === 0) {
    // 일정 없을 때 메시지 표시
    tbody.append('<tr><td colspan="6">해당 날짜에 일정이 없습니다.</td></tr>');
    return;
  }
  
  function formatDate(dateObj){
    if(!dateObj) return '-';
    if (typeof dateObj === 'object'){
      const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
      return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
    }
    return dateObj;
  }

  // 일정 목록 행 추가
  data.forEach(item => {
  console.log(item.as_time);	
    tbody.append(`
      <tr>
        <td>${item.as_cd}</td>
        <td>${item.as_title}</td>
        <td>${item.staff_nm || '미지정'}</td>
        <td>${item.as_status}</td>
        <td>${formatDate(item.as_date)}</td>
        <td>
          <button onclick="openModal(${item.as_cd})">상세보기</button>
        </td>
      </tr>
    `);
  });
}

/**
 * AS 일정 상세 정보 모달 열기
 * @param {number} asCd - AS 코드
 */
function openModal(asCd) {
  $.ajax({
    url: `/as/task/${asCd}`, // 상세정보 API 호출
    method: 'GET',
    dataType: 'json',
    success: function(asData) {
      console.log(asData);

      // 날짜 포맷팅 처리 (객체면 상세하게, 아니면 그대로)
      const dateObj = asData.as_date;
      let formattedDate = '';
      if (typeof dateObj === 'object') {
        formattedDate = `${dateObj.year}-${String(dateObj.monthValue).padStart(2, '0')}-${String(dateObj.dayOfMonth).padStart(2, '0')} ${String(dateObj.hour).padStart(2, '0')}:${String(dateObj.minute).padStart(2, '0')}`;
      } else {
        formattedDate = asData.as_date;
      }

      // 모달 내 각 필드에 데이터 세팅
      document.getElementById('modalAsCd').innerText = asData.as_cd;
      document.getElementById('modalAsTitle').innerText = asData.as_title;
      document.getElementById('modalStaffNm').innerText = asData.staff_nm || '미지정';
      document.getElementById('modalAsContent').innerText = asData.as_content;
      document.getElementById('modalAsDate').innerText = formattedDate;
      document.getElementById('modalAsAddr').innerText = asData.as_addr;
      document.getElementById('modalAsFacility').innerText = asData.as_facility;

      // 신청자 이름 필드 설정
      document.getElementById('modalUserNm').innerText = asData.user_nm || '신청자 정보 없음';

      // 상태 셀렉트 박스 초기화 및 옵션 세팅
      const select = document.getElementById('statusSelect');
      select.innerHTML = '';

      const statusList = ['신고 접수', 'A/S 작업 중', '작업 완료', '작업 취소'];

      // 현재 상태 인덱스 찾기
      const currentIndex = statusList.indexOf(asData.as_status);

      // 현재 상태부터 하위 상태만 옵션으로 생성
      const allowedStatuses = currentIndex >= 0 ? statusList.slice(currentIndex) : statusList;

      allowedStatuses.forEach(status => {
        const option = document.createElement('option');
        option.value = status;
        option.text = status;
        if (status === asData.as_status) option.selected = true;
        select.appendChild(option);
      });

      // 모달 표시
      document.getElementById('statusModal').style.display = 'block';
    },
    error: function(xhr) {
      console.log(xhr.responseText);
      alert('상세 정보를 불러오는 중 오류가 발생했습니다.');
    }
  });
}

/**
 * 모달 닫기 함수
 */
function closeModal() {
  document.getElementById('statusModal').style.display = 'none';
}

/**
 * 진행 상태 저장 함수
 * - 모달에서 선택한 상태를 서버에 POST 요청으로 전송
 * - 성공 시 알림 띄우고 페이지 새로고침
 */
function saveStatus() {
  const asCd = document.getElementById('modalAsCd').innerText;
  const newStatus = document.getElementById('statusSelect').value;

  $.ajax({
    type: 'POST',
    url: '/as/updateStatus',
    data: {
      as_cd: asCd,
      as_status: newStatus
    },
    success: function() {
      alert('상태가 변경되었습니다.');
      location.reload();
    },
    error: function() {
      alert('오류가 발생했습니다.');
    }
  });
}

document.addEventListener('DOMContentLoaded', function () {
	const calendarEl = document.getElementById('calendar');
	let selectedStaff = '';

	const calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth', // month view
		locale: 'ko',
		height: 'auto',
		dayMaxEvents: 3,
		
		eventContent: function(arg) {
			const status = arg.event.extendedProps?.status || '';
			let color = '#3788d8';
			let textStyle = {};

			switch (status) {
				case '신고 접수':
					color = '#439b43';
					break;
				case 'A/S 작업 중':
					color = '#f0ad4e';
					break;
				case '작업 취소':
					color = '#ff6b6b';
					break;
				case '작업 완료':
					color = '#cccccc';
					textStyle = {
						color: '#aaa',
						opacity: '0.6',
						fontStyle: 'italic'
					};
					break;
			}

			const dot = document.createElement('span');
			dot.style.display = 'inline-block';
			dot.style.width = '10px';
			dot.style.height = '10px';
			dot.style.borderRadius = '50%';
			dot.style.marginRight = '5px';
			dot.style.backgroundColor = color;

			const text = document.createElement('span');
			text.innerText = arg.event.title;
			Object.assign(text.style, textStyle);

			const container = document.createElement('div');
			container.style.display = 'flex';
			container.style.alignItems = 'center';
			container.appendChild(dot);
			container.appendChild(text);

			return { domNodes: [container] };
		},
		events: function(fetchInfo, successCallback, failureCallback) {
			$.ajax({
				url: '/as/calendar-data',
				type: 'GET',
				dataType: "json",
				data: {
					staff: selectedStaff
				},
				success: function(data) {
					const role = data.role;
					const rowEvents = data.events;
					const events = rowEvents.map(item => {
						const asDate = item.as_date;
						let startDateStr = '';

						if (typeof asDate === 'string') {
							startDateStr = asDate.replace(' ', 'T')
						} else if (asDate && typeof asDate === 'object' && asDate.year) {
							const dateObj = new Date(
								asDate.year,
								asDate.monthValue - 1, // JS는 0부터 시작
								asDate.dayOfMonth,
								asDate.hour || 0,
								asDate.minute || 0,
								asDate.second || 0
							);
							startDateStr = dateObj.toISOString();
						} else {
							startDateStr = new Date().toISOString();
						}
						
						return {
							title: (role === 'admin')
							? `[${item.staff_nm}] ${item.as_time} ${item.as_status}`  // 관리자용
							: `${item.as_time} ${item.as_status}`,                   // 직원용
							start: startDateStr,
							extendedProps: {
				                asCd: item.as_cd,
				                status: item.as_status,
				                title: item.as_title,
				                staff: item.staff_nm
							}
						};
					});
					successCallback(events);
	    		},
				error: function(xhr) {
					const message = xhr?.responseJSON?.message || xhr?.statusText || '알 수 없는 오류입니다.';
					console.error('캘린더 요청 실패:', message);
					alert('캘린더 데이터를 불러오는 데 실패했습니다.\n' + message);
					failureCallback();
				}
			});
		},
	    eventClick: function(info) {
			const asCd = info.event.extendedProps?.asCd;
			if (!asCd) {
				alert('일정 정보를 찾을 수 없습니다.');
				return;
			}
			openModal(asCd);
	    }
  });
	calendar.render();
	
	const $searchInput = $('#searchCalendarStaff');
	$('#btnCalendarSearch').on('click', function () {
		selectedStaff = $searchInput.val().trim(); // 공백이면 전체
		calendar.refetchEvents();
	});
});
