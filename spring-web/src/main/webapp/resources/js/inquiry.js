
$(document).ready(function() {
	function fetchinquiryList() {
		$.ajax({
			url: '/inquiry/inquiryList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			dataType: 'json',
			success: function(data) {
				renderTable(data);  // 받아온 데이터로 테이블 그리기
			},
			error: function(xhr, status, error) {
				alert('문의 게시판 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}

	function renderTable(data) {
		const tbody = $('#inquiryTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {
			tbody.append('<tr><td colspan="5">게시글이 없습니다.</td></tr>');
			return;
		}
		  
		function formatDate(dateObj){
			if(!dateObj) return '-';
			if (typeof dateObj === 'object'){
				const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
				return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')}`;
			}
			return dateObj;
		}
	
		  // 일정 목록 행 추가
		data.forEach(item => {
			tbody.append(`
				<tr>
					<td>${item.inquiry_cd}</td>
					<td><a href="/inquiry/detail?inquiry_cd=${item.inquiry_cd}">${item.inquiry_title}</a></td>
					<td>${item.user_nm}</td>
					<td>${formatDate(item.created_dt)}</td>
					<td>${item.inquiry_status}</td>
				</tr>
			`);
		});
	}
	
	fetchinquiryList();
});