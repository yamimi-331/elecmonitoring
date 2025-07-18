// resources/js/report.js

$(document).ready(function() {

    // ===========================================================================
    // 1. 리포트 목록 조회 및 렌더링 함수
    // ===========================================================================

    /**
     * 리포트 목록을 서버에서 가져와 테이블을 렌더링하고 페이지네이션을 업데이트합니다.
     * 이 함수는 백엔드에서 1-based 페이지 번호를 받는다고 가정합니다.
     * @param {string} local - 지역 검색어 (기본값 빈 문자열)
     * @param {number} page - 요청할 페이지 번호 (1-based), 기본값 1
     */
    function fetchReportList(local = "", page = 1) { // page 기본값 1 유지, size 파라미터 제거
        $.ajax({
            url: '/report/reportList', // 서버 API 엔드포인트
            method: 'GET',
            data: {
                local: local,
                page: page // 1-based 페이지 번호 전달
            },
            dataType: 'json',
            success: function(response) {
                // 서버 응답 구조: { "list": [...], "totalCount": N }
                console.log("응답 데이터 (report):", response);

                renderTable(response.list); // 받아온 데이터(list)로 테이블 그리기
                // 페이지네이션 렌더링 함수 호출
                // response.totalCount는 총 항목 수, page는 현재 페이지 (1-based)
                renderPagination(response.totalCount, page, local);
            },
            error: function(xhr, status, error) {
                console.error("에러 발생 (report):", xhr.responseText);
                alert('신고 목록 정보를 불러오는 중 오류가 발생했습니다.');
            }
        });
    }

    // ===========================================================================
    // 2. 페이징 렌더링 함수
    // ===========================================================================

    /**
     * 리포트 목록의 페이지네이션을 렌더링하는 함수.
     * notice.js의 스타일과 동일하게 맞추며, 1-based 페이지 번호를 사용합니다.
     * @param {number} totalCount - 전체 항목 수
     * @param {number} currentPage - 현재 페이지 번호 (1-based)
     * @param {string} localValue - 현재 검색 조건 (조회 유지용)
     */
    function renderPagination(totalCount, currentPage, localValue) {
        const pageSize = 10; // 페이지당 항목 수는 고정 (서버와 맞춰야 함)
        const totalPages = Math.ceil(totalCount / pageSize); // 총 페이지 수 계산

        const paginationContainer = $('#pagination'); // HTML의 페이지네이션 div ID
        paginationContainer.empty(); // 기존 페이지네이션 내용 비우기

        if (totalPages <= 1) { // 전체 페이지가 1개 이하면 페이지네이션 불필요
            return;
        }

        // 'custom-pagination' 클래스를 추가하여 CSS를 적용합니다.
        const ul = $('<ul>').addClass('pagination custom-pagination justify-content-center');

        // '이전' 버튼 생성
        const prevLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === 1);
        const prevLink = $('<a>').addClass('page-link').attr('href', '#').html('&laquo;'); // &laquo;는 이중 왼쪽 화살표
        prevLink.on('click', function(e) {
            e.preventDefault();
            if (currentPage > 1) {
                fetchReportList(localValue, currentPage - 1); // 이전 페이지 (1-based)로 조회
            }
        });
        ul.append(prevLi.append(prevLink)); // <li>에 <a>를 추가한 후, <ul>에 <li>를 추가

        // 페이지 번호 버튼들 생성 (예: 5개씩 그룹화하여 보여줌)
        const pageGroupSize = 5; // 한 번에 보여줄 페이지 번호 개수
        // 1-based 시작 페이지 그룹 계산
        const startPageGroup = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
        const endPageGroup = Math.min(startPageGroup + pageGroupSize - 1, totalPages);

        for (let i = startPageGroup; i <= endPageGroup; i++) { // 루프 조건 1-based
            const pageLi = $('<li>').addClass('page-item').toggleClass('active', i === currentPage);
            const pageLink = $('<a>').addClass('page-link').attr('href', '#').text(i); // 사용자에게는 i 그대로 보여줌 (1-based)
            pageLink.on('click', function(e) {
                e.preventDefault();
                fetchReportList(localValue, i); // 클릭한 페이지 (1-based)로 조회
            });
            ul.append(pageLi.append(pageLink)); // <li>에 <a>를 추가한 후, <ul>에 <li>를 추가
        }

        // '다음' 버튼 생성
        const nextLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === totalPages);
        const nextLink = $('<a>').addClass('page-link').attr('href', '#').html('&raquo;'); // &raquo;는 이중 오른쪽 화살표
        nextLink.on('click', function(e) {
            e.preventDefault();
            if (currentPage < totalPages) {
                fetchReportList(localValue, currentPage + 1); // 다음 페이지 (1-based)로 조회
            }
        });
        ul.append(nextLi.append(nextLink)); // <li>에 <a>를 추가한 후, <ul>에 <li>를 추가

        paginationContainer.append(ul); // 완성된 <ul>을 페이지네이션 컨테이너에 추가
    }

    // ===========================================================================
    // 3. 이벤트 리스너 설정
    // ===========================================================================

    // 검색 버튼 클릭 이벤트
    $('#search_addr_btn').on("click", function () {
        const selectedLocal = document.getElementById("local").value;
        // 검색 시에는 항상 첫 페이지(1)부터 조회하도록 설정합니다.
        fetchReportList(selectedLocal === "전체" ? "" : selectedLocal, 1);
    });

    // ===========================================================================
    // 4. 날짜 포맷팅 유틸리티 함수
    // ===========================================================================

    // yyyy-MM-dd 형식으로 날짜 포맷
    function formatRegisterDate(dateObj) {
        if (!dateObj) return '-';
        // 서버에서 넘어온 LocalDateTime 객체 형태를 처리
        if (typeof dateObj === 'object' && dateObj.year !== undefined) {
            const { year, monthValue, dayOfMonth } = dateObj;
            return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')}`;
        }
        return dateObj; // 이미 문자열이면 그대로 반환
    }

    // yyyy-MM-dd HH:mm 형식으로 날짜 포맷
    function formatUpdateDate(dateObj) {
        if (!dateObj) return '-';
        // 서버에서 넘어온 LocalDateTime 객체 형태를 처리
        if (typeof dateObj === 'object' && dateObj.year !== undefined) {
            const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
            return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
        }
        return dateObj; // 이미 문자열이면 그대로 반환
    }

    // ===========================================================================
    // 5. 테이블 렌더링 함수
    // ===========================================================================

    /**
     * 받아온 데이터로 리포트 테이블 내용을 렌더링합니다.
     * @param {Array<Object>} data - 리포트 VO 객체 배열 (response.list)
     */
    function renderTable(data) {
        // #reportList는 테이블의 부모 요소일 수 있으며, #reportTableBody는 tbody의 ID입니다.
        const tbody = $('#reportTableBody'); // 직접 tbody ID를 사용
        tbody.empty(); // 기존 테이블 내용 비우기

        if (!data || data.length === 0) {
            tbody.append('<tr><td colspan="6">신고 내역이 없습니다.</td></tr>');
            return;
        }

        // 리포트 목록 행 추가
        data.forEach(item => {
            const reportDtFormatted = formatRegisterDate(item.report_dt);
            const updateDtFormatted = formatUpdateDate(item.update_dt);

            // report_dt와 update_dt가 동일하면 수정일시를 "-"로 표시
            const isSameDateTime = (item.report_dt && item.update_dt &&
                                    item.report_dt.year === item.update_dt.year &&
                                    item.report_dt.monthValue === item.update_dt.monthValue &&
                                    item.report_dt.dayOfMonth === item.update_dt.dayOfMonth &&
                                    item.report_dt.hour === item.update_dt.hour &&
                                    item.report_dt.minute === item.update_dt.minute);

            const displayUpdateDt = isSameDateTime ? "-" : updateDtFormatted;

            tbody.append(`
                <tr>
                    <td>${item.report_cd}</td>
                    <td>${item.local || '-'}</td>
                    <td><a href="/report/detail?report_cd=${item.report_cd}">${item.title}</a></td>
                    <td>${item.staff_nm || '미지정'}</td>
                    <td>${reportDtFormatted}</td>
                    <td>${displayUpdateDt}</td>
                </tr>
            `);
        });
    }

    // ===========================================================================
    // 6. 페이지 로드 시 초기 데이터 로드
    // ===========================================================================
    // 페이지 로드 시 초기 리포트 목록(첫 페이지, 1-based)을 불러옵니다.
    fetchReportList("", 1); // 지역 검색어 없음, 1페이지부터 시작

    // ===========================================================================
    // 7. 폼 유효성 검사 (report-form)
    // ===========================================================================
    // 'report-form'이 현재 JSP 페이지에 없을 경우를 대비하여 null 체크를 추가합니다.
    const form = document.getElementById("report-form");
    if (form) { // form 요소가 존재할 때만 이벤트 리스너를 추가
        form.addEventListener("submit", function (e) {
            // FormData 디버깅 (console 출력)
            const formData = new FormData(form);
            for (let pair of formData.entries()) {
                console.log(`${pair[0]}: ${pair[1]}`);
            }

            // 입력 필드 가져오기
            const title = document.getElementById("title");
            const phone = document.getElementById("phone");
            const local = document.getElementById("local");
            const type = document.getElementById("type");
            const content = document.getElementById("content");

            // 유효성 검사
            if (title.value.trim() === "") {
                alert("제목을 입력해주세요.");
                title.focus();
                e.preventDefault();
                return;
            }

            if (phone.value.trim() === "") {
                alert("신고자 전화번호를 입력해주세요.");
                phone.focus();
                e.preventDefault();
                return;
            }

            if (local.value === "-" || local.value === "") {
                alert("지역을 선택해주세요.");
                local.focus();
                e.preventDefault();
                return;
            }

            if (type.value === "-" || type.value === "") {
                alert("재해 유형을 선택해주세요.");
                type.focus();
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