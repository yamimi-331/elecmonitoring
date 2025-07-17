
$(document).ready(function() {
	function fetchinquiryList(search_word = "", onlyMine = false) {
	
		const userCd = window.currentUserCd;
		const requestData = {
			search_word: search_word
		};
	
		if (onlyMine && userCd) {
			requestData.user_cd = userCd;
		}
		
		$.ajax({
			url: '/inquiry/inquiryList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			data: requestData,
			dataType: 'json',
			success: function(data) {
				renderTable(data);  // 받아온 데이터로 테이블 그리기
			},
			error: function(xhr, status, error) {
				alert('문의 게시판 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}

	$('#search_word_btn').on("click", function () {
		const searchWord = document.getElementById("search_word").value;
		
		const checkbox = document.getElementById("personal-inquiry");
		const onlyMine = checkbox ? checkbox.checked : false;
		
		fetchinquiryList(searchWord, onlyMine);
	});
	
	const checkbox = document.getElementById("personal-inquiry");
	if (checkbox) {
		checkbox.addEventListener("change", function () {
			const searchWord = document.getElementById("search_word").value;
			const onlyMine = this.checked;
			fetchinquiryList(searchWord, onlyMine);
		});
	}

	function formatDate(dateObj){
		if(!dateObj) return '-';
		if (typeof dateObj === 'object'){
			const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
			return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')}`;
		}
		return dateObj;
	}

	function renderTable(data) {
		const tbody = $('#inquiryTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {
			tbody.append('<tr><td colspan="5">게시글이 없습니다.</td></tr>');
			return;
		}
		
		const currentUserCd = window.currentUserCd;
		const userRole = window.userRole;
		
		  // 일정 목록 행 추가
		data.forEach(item => {
			let titleHtml = '';
			const isSecret = item.secret_yn === 'Y';
			const isAuthor = currentUserCd === item.user_cd;
			const isStaff = userRole === 'staff' || userRole === 'admin';
			
			if (isSecret && !(isAuthor || isStaff)) {
				titleHtml = `<span class="locked">비밀글입니다.</span>`;
			} else {
				titleHtml = `<a href="/inquiry/detail?inquiry_cd=${item.inquiry_cd}">${item.inquiry_title}</a>`;
			}
			
			tbody.append(`
				<tr>
					<td>${item.inquiry_cd}</td>
					<td>${titleHtml}</td>
					<td>${item.user_nm}</td>
					<td>${formatDate(item.created_dt)}</td>
					<td>${item.inquiry_status}</td>
				</tr>
			`);
		});
	}
	
	fetchinquiryList();
});