/**
 * AS 일정 상세 정보 모달 열기
 * @param {number} asCd - AS 코드
 */
function openModal(asCd) {
  $.ajax({
    url: `/as/detail/${asCd}`,
    method: 'GET',
    dataType: 'json',
    success: function(asData) {
	  console.log(asData);
	
	  const dateObj = asData.as_date;
	  let formattedDate = '';
	  if (typeof dateObj === 'object') {
	    formattedDate = `${dateObj.year}-${String(dateObj.monthValue).padStart(2, '0')}-${String(dateObj.dayOfMonth).padStart(2, '0')} ${String(dateObj.hour).padStart(2, '0')}:${String(dateObj.minute).padStart(2, '0')}`;
	  } else {
	    formattedDate = asData.as_date;
	  }
	
	  document.getElementById('modalAsCd').innerText = asData.as_cd;
	  document.getElementById('modalAsTitle').innerText = asData.as_title;
	  document.getElementById('modalStaffNm').innerText = asData.staff_nm || '미지정';
	  document.getElementById('modalAsContent').innerText = asData.as_content;
	  document.getElementById('modalAsDate').innerText = formattedDate;
	  document.getElementById('modalAsAddr').innerText = asData.as_addr;
	  document.getElementById('modalAsFacility').innerText = asData.as_facility;
	
	  // 신청자 이름!
	  document.getElementById('modalUserNm').innerText = asData.user_nm || '신청자 정보 없음';
	
	  const select = document.getElementById('statusSelect');
      select.innerHTML = '';

      const statusList = ['신고 접수', '기사 배정 중', '기사 배정 완료', 'A/S 작업 중', '작업 완료', '작업 취소'];

      // 현재 상태 위치 찾기
      const currentIndex = statusList.indexOf(asData.as_status);

      // currentIndex부터 끝까지 상태만 필터링
      const allowedStatuses = currentIndex >= 0 ? statusList.slice(currentIndex) : statusList;

      allowedStatuses.forEach(status => {
        const option = document.createElement('option');
        option.value = status;
        option.text = status;
        if (status === asData.as_status) option.selected = true;
        select.appendChild(option);
      });

      document.getElementById('statusModal').style.display = 'block';
	},
    error: function(xhr) {
    	console.log( xhr.responseText);
      alert('상세 정보를 불러오는 중 오류가 발생했습니다.');
    }
  });
}

/**
 * 모달 닫기
 */
function closeModal() {
  document.getElementById('statusModal').style.display = 'none';
}

/**
 * 진행 상태 저장
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
