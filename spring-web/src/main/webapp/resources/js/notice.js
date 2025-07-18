
$(document).ready(function() {
	function fetchNoticeList(search_word = "") {
		$.ajax({
			url: '/notice/noticeList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			data: { search_word: search_word },
			dataType: 'json',
			success: function(data) {
				console.log("응답 데이터:", data);
				renderTable(data);  // 받아온 데이터로 테이블 그리기
			},
			error: function(xhr, status, error) {
				console.error("에러 발생:", xhr.responseText);
				alert('문의 게시판 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}

	$('#search_word_btn').on("click", function () {
		const searchWord = document.getElementById("search_word").value;
		fetchNoticeList(searchWord);
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
		const tbody = $('#noticeTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {
			tbody.append('<tr><td colspan="5">공지사항이 없습니다.</td></tr>');
			return;
		}
		
		  // 일정 목록 행 추가
		data.forEach(item => {
			let update_date = formatUpdateDate(item.update_dt);
			
			const reportDateTime = formatRegisterDate(item.create_dt);
    		const updateDateTime = formatUpdateDate(item.update_dt);
    		
			if(reportDateTime == updateDateTime){
				update_date = "-";
			}
			
			tbody.append(`
				<tr>
					<td>${item.notice_cd}</td>
					<td><a href="/notice/detail?notice_cd=${item.notice_cd}">${item.title}</a></td>
					<td>${item.staff_nm}</td>
					<td>${formatRegisterDate(item.create_dt)}</td>
					<td>${formatUpdateDate(item.update_dt)}</td>
				</tr>
			`);
		});
	}
	
	fetchNoticeList();
	
	const form = document.getElementById("notice-form");

	form.addEventListener("submit", function (e) {
		// FormData 디버깅 (console 출력)
		const formData = new FormData(form);
		for (let pair of formData.entries()) {
			console.log(`${pair[0]}: ${pair[1]}`);
		}

		// 입력 필드 가져오기
		const title = document.getElementById("title");
		const content = document.getElementById("content");

		// 유효성 검사
		if (title.value.trim() === "") {
			alert("제목을 입력해주세요.");
			title.focus();
			e.preventDefault();
			return;
		}

		if (content.value.trim() === "") {
			alert("내용을 입력해주세요.");
			content.focus();
			e.preventDefault();
			return;
		}
	});
});