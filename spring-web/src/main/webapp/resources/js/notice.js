
$(document).ready(function() {
	    // ===========================================================================
    // 1. 공지사항 목록 조회 및 렌더링 함수
    // ===========================================================================

    /**
     * 공지사항 목록을 서버에서 가져와 테이블을 렌더링하고 페이지네이션을 업데이트합니다.
     * @param {string} search_word - 검색어 (기본값 빈 문자열)
     * @param {number} page - 요청할 페이지 번호 (서버 기준: 0-based), 기본값 0
     * @param {number} size - 페이지당 항목 수, 기본값 10
     */
	function fetchNoticeList(search_word = "", page = 0, size = 10) {
		$.ajax({
			url: '/notice/noticeList',  // 서버 API 엔드포인트
			method: 'GET', // 쿼리 파라미터로 날짜 전달
			data:  {
                search_word: search_word,
                page: page, // 페이지 번호 전달 (0-based)
                size: size // 페이지 사이즈 전달
            },
			dataType: 'json',
			success: function(response) {
				renderTable(response.content); // 받아온 데이터(content)로 테이블 그리기
                // 페이지네이션 렌더링 함수 호출
                // response.currentPage는 서버에서 넘어온 0-based 페이지 번호
                renderPagination(response.currentPage, response.totalPages, search_word);
			},
			error: function(xhr, status, error) {
				console.error("에러 발생:", xhr.responseText);
				alert('문의 게시판 정보를 불러오는 중 오류가 발생했습니다.');
			}
		});
	}
    // ===========================================================================
    // 2. 이벤트 리스너 설정
    // ===========================================================================

	$('#search_word_btn').on("click", function () {
		const searchWord = document.getElementById("search_word").value;
		 // 검색 시에는 항상 첫 페이지(0)부터 조회하도록 설정합니다.
        fetchNoticeList(searchWord, 0, 10);
	});
 	
	// ===========================================================================
    // 3. 날짜 포맷팅 유틸리티 함수
    // ===========================================================================

	// yyyy-MM-dd 형식으로 날짜 포맷
	function formatRegisterDate(dateObj){
		if(!dateObj) return '-';
		 // 서버에서 넘어온 LocalDateTime 객체 형태를 처리
		if (typeof dateObj === 'object'){
			const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
			return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')}`;
		}
		return dateObj;
	}
	// yyyy-MM-dd HH:mm 형식으로 날짜 포맷
	function formatUpdateDate(dateObj){
		if(!dateObj) return '-';
		if (typeof dateObj === 'object'){
			const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
			return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
		}
		return dateObj;
	}
 
	// ===========================================================================
    // 4. 테이블 렌더링 함수
    // ===========================================================================

	  /**
     * 받아온 데이터로 공지사항 테이블 내용을 렌더링합니다.
     * @param {Array<Object>} data - NoticeVO 객체 배열 (NoticePageResponseDTO.content)
     */
	function renderTable(data) {
		const tbody = $('#noticeTableBody');
		tbody.empty(); // 기존 테이블 내용 비우기
	
		if (!data || data.length === 0) {	// 데이터가 없으면 메시지 표시
			tbody.append('<tr><td colspan="5">공지사항이 없습니다.</td></tr>');
			return;
		}
		
		// 일정 목록 행 추가
		  data.forEach(item => {
            const createDtFormatted = formatRegisterDate(item.create_dt);
            const updateDtFormatted = formatUpdateDate(item.update_dt);

            // create_dt와 update_dt가 동일하면 수정일시를 "-"로 표시
            // 객체 비교는 정확한 필드 값 비교가 필요합니다.
            const isSameDateTime = (item.create_dt && item.update_dt &&
                                    item.create_dt.year === item.update_dt.year &&
                                    item.create_dt.monthValue === item.update_dt.monthValue &&
                                    item.create_dt.dayOfMonth === item.update_dt.dayOfMonth &&
                                    item.create_dt.hour === item.update_dt.hour &&
                                    item.create_dt.minute === item.update_dt.minute);

            const displayUpdateDt = isSameDateTime ? "-" : updateDtFormatted;

            tbody.append(`
                <tr>
                    <td>${item.notice_cd}</td>
                    <td><a href="/notice/detail?notice_cd=${item.notice_cd}">${item.title}</a></td>
                    <td>${item.staff_nm || '-'}</td> <!-- 작성자가 없을 경우 '-' 처리 -->
                    <td>${createDtFormatted}</td>
                    <td>${displayUpdateDt}</td>
                </tr>
            `);
        });
	}
	    // ===========================================================================
    // 5. 페이지네이션 렌더링 함수
    // ===========================================================================

	/**
     * 페이지네이션 버튼들을 렌더링합니다.
     * @param {number} currentPage - 현재 페이지 번호 (서버 기준: 0-based)
     * @param {number} totalPages - 전체 페이지 수
     * @param {string} searchWord - 현재 적용된 검색어 (페이지 이동 시 검색 조건 유지)
     */
    function renderPagination(currentPage, totalPages, searchWord) {
        const paginationDiv = $('#pagination'); // HTML의 페이지네이션 div ID
        paginationDiv.empty(); // 기존 페이지네이션 내용 비우기

        if (totalPages <= 1) { // 전체 페이지가 1개 이하면 페이지네이션 불필요
            return;
        }

        // 'custom-pagination' 클래스를 추가하여 CSS를 적용합니다.
        const ul = $('<ul>').addClass('pagination custom-pagination justify-content-center');
        const pageSize = 10; // 페이지당 항목 수는 fetchNoticeList 함수와 동일하게 유지

        // '이전' 버튼 생성
        const prevLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === 0);
        const prevLink = $('<a>').addClass('page-link').attr('href', '#').html('&laquo;'); // &laquo;는 이중 왼쪽 화살표
        prevLink.on('click', function(e) {
            e.preventDefault();
            if (currentPage > 0) {
                fetchNoticeList(searchWord, currentPage - 1, pageSize); // 이전 페이지의 0-based 번호로 조회
            }
        });
        ul.append(prevLi.append(prevLink));

        // 페이지 번호 버튼들 생성 (예: 5개씩 그룹화하여 보여줌)
        const pageGroupSize = 5; // 한 번에 보여줄 페이지 번호 개수
        const startPage = Math.floor(currentPage / pageGroupSize) * pageGroupSize; // 현재 페이지 그룹의 시작 (0-based)
        const endPage = Math.min(startPage + pageGroupSize, totalPages); // 현재 페이지 그룹의 끝

        for (let i = startPage; i < endPage; i++) {
            const pageLi = $('<li>').addClass('page-item').toggleClass('active', i === currentPage);
            const pageLink = $('<a>').addClass('page-link').attr('href', '#').text(i + 1); // 사용자에게는 1-based로 보여줌
            pageLink.on('click', function(e) {
                e.preventDefault();
                fetchNoticeList(searchWord, i, pageSize); // 클릭한 페이지의 0-based 번호로 조회
            });
            ul.append(pageLi.append(pageLink));
        }

        // '다음' 버튼 생성
        const nextLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === totalPages - 1);
        const nextLink = $('<a>').addClass('page-link').attr('href', '#').html('&raquo;'); // &raquo;는 이중 오른쪽 화살표
        nextLink.on('click', function(e) {
            e.preventDefault();
            if (currentPage < totalPages - 1) {
                fetchNoticeList(searchWord, currentPage + 1, pageSize); // 다음 페이지의 0-based 번호로 조회
            }
        });
        ul.append(nextLi.append(nextLink));

        paginationDiv.append(ul); // 완성된 페이지네이션을 컨테이너에 추가
    }
	  
	// ===========================================================================
    // 6. 페이지 로드 시 초기 데이터 로드
    // ===========================================================================
    // 페이지가 로드되면 초기 공지사항 목록(첫 페이지)을 불러옵니다.
 
	fetchNoticeList();
	
	 // ===========================================================================
    // 7. 공지사항 폼 유효성 검사 (공지사항 작성 페이지에서 사용될 코드)
    // ===========================================================================
    // 이 스크립트가 공지사항 목록 페이지(notice/list.jsp)에 연결되어 있다면
    // 'notice-form' 요소는 존재하지 않을 수 있습니다.
    // 따라서 폼이 현재 페이지에 존재하는지 확인하는 로직을 추가하는 것이 안전합니다.

	const form = document.getElementById("notice-form");
	if (form) { // 폼이 현재 페이지에 존재하는 경우에만 실행
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
	}
});