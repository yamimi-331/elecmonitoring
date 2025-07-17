
$(document).ready(function() {
	function fetchReportList(local = "", page = 1) {
		$.ajax({
			url: '/report/reportList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			data: { local: local, page: page },
			dataType: 'json',
			success: function(response) {
				renderTable(response.list);		// 받아온 데이터로 테이블 그리기
				renderPagination(response.totalCount, page, local);  // 받아온 데이터로 페이지 번호 그리기
			},
			error: function(xhr, status, error) {
				alert('신고 목록 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}
	
	// 페이징을위한 숫자 버튼	
	function renderPagination(totalCount, currentPage, localValue) {
		const pageSize = 10;
		const totalPages = Math.ceil(totalCount / pageSize);
	
		const paginationContainer = $('#pagination');
		paginationContainer.empty();
	
		// 이전 버튼
		if (currentPage > 1) {
			const prevLink = $('<a href="#"></a>')
				.text('◀ 이전')
				.on('click', function (e) {
					e.preventDefault();
					fetchReportList(localValue, currentPage - 1);
				});
			paginationContainer.append(prevLink);
		}
	
		// 페이지 번호
		for (let i = 1; i <= totalPages; i++) {
			const pageLink = $('<a href="#"></a>')
				.text(i)
				.addClass(i === currentPage ? 'active' : '')
				.on('click', function (e) {
					e.preventDefault();
					fetchReportList(localValue, i);
				});
			paginationContainer.append(pageLink);
		}
	
		// 다음 버튼
		if (currentPage < totalPages) {
			const nextLink = $('<a href="#"></a>')
				.text('다음 ▶')
				.on('click', function (e) {
					e.preventDefault();
					fetchReportList(localValue, currentPage + 1);
				});
			paginationContainer.append(nextLink);
		}
	}

	$('#search_addr_btn').on("click", function () {
		const selectedLocal = document.getElementById("local").value;
		fetchReportList(selectedLocal === "전체" ? "" : selectedLocal);
	});
	  
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
	
	function renderTable(data) {
		const tbody = $('#reportList').find('#reportTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {
			tbody.append('<tr><td colspan="6">신고 내역이 없습니다.</td></tr>');
			return;
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
		
	// 처음 로드시 리스트 조회
	fetchReportList();
});