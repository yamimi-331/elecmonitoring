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

  // 일정 목록 행 추가
  data.forEach(item => {
  console.log(item.as_time);	
    tbody.append(`
      <tr>
        <td>${item.as_cd}</td>
        <td>${item.as_title}</td>
        <td>${item.staff_nm || '미지정'}</td>
        <td>${item.as_status}</td>
        <td>${item.as_time || '-'}</td>
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
