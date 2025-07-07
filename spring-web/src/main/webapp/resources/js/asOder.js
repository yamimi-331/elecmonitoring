/**
 * AS 일정 상세 정보 모달 열기
 * @param {number} asCd - AS 코드
 */
function openModal(asCd) {
  $.ajax({
    url: `/as/detail/${asCd}`,
    method: 'GET',
    success: function(asData) {
      // 모달 내 각 요소에 데이터 세팅
      document.getElementById('modalAsCd').innerText = asData.as_cd;
      document.getElementById('modalAsTitle').innerText = asData.as_title;
      document.getElementById('modalStaffNm').innerText = asData.staff_nm || '미지정';
      document.getElementById('modalAsContent').innerText = asData.as_content;
      document.getElementById('modalAsDate').innerText = asData.as_date;
      document.getElementById('modalAsAddr').innerText = asData.as_addr;
      document.getElementById('modalAsFacility').innerText = asData.as_facility;

      // 진행 상태 select 초기화 및 옵션 생성
      const select = document.getElementById('statusSelect');
      select.innerHTML = '';
      const statusList = ['신고 접수', '기사 배정 중', '기사 배정 완료', 'A/S 작업 중', '작업 완료', '작업 취소'];
      statusList.forEach(status => {
        const option = document.createElement('option');
        option.value = status;
        option.text = status;
        if (status === asData.as_status) option.selected = true;
        select.appendChild(option);
      });

      // 모달 보여주기
      document.getElementById('statusModal').style.display = 'block';
    },
    error: function() {
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
