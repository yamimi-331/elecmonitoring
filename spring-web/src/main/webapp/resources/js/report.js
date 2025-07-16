
$(document).ready(function() {
	function fetchReportList() {
		$.ajax({
			url: '/report/reportList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			dataType: 'json',
			success: function(data) {
				renderTable(data);  // 받아온 데이터로 테이블 그리기
			},
			error: function() {
				alert('신고 목록 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}
	

	function renderTable(data) {
		const tbody = $('#reportTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {
			tbody.append('<tr><td colspan="6">신고 내역이 없습니다.</td></tr>');
			return;
		}
		  
		function formatRegisterDate(dateObj){
			if(!dateObj) return '-';
			if (typeof dateObj === 'object'){
				const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
				return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')}`;
			}
			return dateObj;
		}
		function formatUpdateDate(dateObj){
			if(!dateObj) return '-';
			if (typeof dateObj === 'object'){
				const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
				return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
			}
			return dateObj;
		}
	
		  // 일정 목록 행 추가
		data.forEach(item => {
			let update_date = formatUpdateDate(item.update_dt);
			
			const reportDateTime = formatUpdateDate(item.report_dt);
    		const updateDateTime = formatUpdateDate(item.update_dt);
    		
			if(reportDateTime == updateDateTime){
				update_date = "-";
			}

			tbody.append(`
				<tr>
					<td>${item.report_cd}</td>
					<td>${item.local || '-'}</td>
					<td><a href="/report/detail?report_cd=${item.report_cd}">${item.title}</a></td>
					<td>${item.staff_nm || '미지정'}</td>
					<td>${formatRegisterDate(item.report_dt)}</td>
					<td>${update_date}</td>
				</tr>
			`);
		});
	}
	
	fetchReportList();
});