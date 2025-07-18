/**
 * 문서 로딩 시 실행되는 초기화 함수
 * - 오늘 날짜를 기본값으로 셋팅
 * - jQuery UI datepicker 적용 (주석 처리됨, 필요시 활성화)
 * - 조회 버튼 클릭 시 fetchSchedule2 호출 (페이징 적용)
 * - 페이지 로드 시 오늘 날짜 일정 조회 (fetchSchedule2 사용)
 */
$(function() {
    const today = new Date();
    const sevenDaysLater = new Date();
    sevenDaysLater.setDate(today.getDate() + 7);

    const formatDate = (date) => date.toISOString().split('T')[0]; // YYYY-MM-DD 형식

    $('#startDate').val(formatDate(today));     // input에 오늘 날짜 기본값 설정
    $('#endDate').val(formatDate(sevenDaysLater));
    // const staffInfo = $('#searchStaff').val() || ''; // 초기 로딩 시에는 직접 사용하지 않음

    // 페이지 로드 시 초기 데이터 로드 (fetchSchedule2 사용)
    // 기존: fetchSchedule(formatDate(today), formatDate(sevenDaysLater), '');
    fetchSchedule2(formatDate(today), formatDate(sevenDaysLater), '', 1, 10); // 페이징 적용: 1페이지, 10개씩

    // 조회 버튼 클릭 시 선택한 날짜 일정 조회
    $('#btnSearch').click(function() {
        const startDate = $('#startDate').val().substring(0, 10);
        const endDate = $('#endDate').val().substring(0, 10);
        const staffInfo = $('#searchStaff').val() || ''; // 검색 필드의 ID가 'searchStaff'인 것으로 가정

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
        // 기존: fetchSchedule(startDate, endDate, staffInfo);
        fetchSchedule2(startDate, endDate, staffInfo, 1, 10); // 페이징 적용: 검색 시 항상 1페이지부터
    });

    // jQuery UI datepicker 적용 (필요시 활성화)
    // $('#startDate').datepicker({ dateFormat: 'yy-mm-dd' });
    // $('#endDate').datepicker({ dateFormat: 'yy-mm-dd' });
});

//----------------------------------------------------------------------------------------------------

/**
 * 지정한 날짜에 해당하는 AS 일정 데이터를 서버에서 가져오는 AJAX 함수 (페이징 없는 기존 함수)
 * ⚠️ 이제 fetchSchedule2를 사용하므로 이 함수는 직접 호출되지 않습니다. 롤백을 위해 주석 처리합니다.
 * @param {string} startDate - 조회 시작 날짜 (YYYY-MM-DD)
 * @param {string} endDate - 조회 종료 날짜 (YYYY-MM-DD)
 * @param {string} staffInfo - 담당자 정보 (선택 사항)
 */
/*
function fetchSchedule(startDate, endDate, staffInfo) {
    $.ajax({
        url: `/as/schedule`, // 서버 API 엔드포인트
        method: 'GET',
        data: { startDate, endDate, staffInfo }, // 쿼리 파라미터로 날짜 전달
        dataType: 'json',
        success: function(data) {
            renderTable(data); // 받아온 데이터로 테이블 그리기
        },
        error: function() {
            alert('일정 정보를 불러오는 중 오류가 발생했습니다.');
        }
    });
}
*/

//----------------------------------------------------------------------------------------------------

/**
 * AS 일정 데이터를 받아서 테이블에 출력하는 함수
 * @param {Array} data - AS 일정 객체 배열 (AsScheduleResponseDTO.content)
 */
function renderTable(data) {
    // ⚠️ HTML에서 AS 일정 테이블의 ID가 'asTable'인지 'asScheduleTable'인지 확인하고 통일하세요.
    // 현재 코드에서는 'asTable'을 사용하도록 통일했습니다.
    const tbody = $('#asTable tbody');
    tbody.empty(); // 기존 테이블 내용 비우기

    if (!data || data.length === 0) { // data가 null이거나 비어있을 때 처리
        // 일정 없을 때 메시지 표시
        tbody.append('<tr><td colspan="6" class="text-center">조회된 AS 일정이 없습니다.</td></tr>');
        return;
    }

    function formatDate(dateObj){
        if(!dateObj) return '-';
        // 서버에서 LocalDateTime이 JSON으로 직렬화될 때 객체 형태로 올 수 있습니다.
        if (typeof dateObj === 'object' && dateObj.year !== undefined){
            const { year, monthValue, dayOfMonth, hour, minute } = dateObj;
            return `${year}-${String(monthValue).padStart(2, '0')}-${String(dayOfMonth).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
        }
        // 이미 포맷된 문자열이거나 Date 객체인 경우 (toLocaleString은 Date 객체에만 적용)
        try {
            return new Date(dateObj).toLocaleString();
        } catch (e) {
            return dateObj; // 변환 실패 시 원본 문자열 반환
        }
    }

    // 일정 목록 행 추가
    data.forEach(item => {
        // console.log(item.as_time); // ASListDTO에 as_time이 별도로 없다면 이 로그는 불필요할 수 있습니다.
        tbody.append(`
            <tr>
                <td>${item.as_cd}</td>
                <td>${item.as_title}</td>
                <td>${item.staff_nm || '미지정'}</td>
                <td>${item.as_status}</td>
                <td>${formatDate(item.as_date)}</td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="openModal('${item.as_cd}', window.userType)">상세보기</button>
                </td>
            </tr>
        `);
    });
}

//----------------------------------------------------------------------------------------------------

/**
 * AS 일정 상세 정보 모달 열기
 * @param {string} asCd - AS 코드
 */
function openModal(asCd, userType) {
    $.ajax({
        url: `/as/task/${asCd}`, // 상세정보 API 호출
        method: 'GET',
        dataType: 'json',
        success: function(asData) {
            console.log(asData);

            // 날짜 포맷팅 처리
            const dateObj = asData.as_date;
            let formattedDate = '';
            if (typeof dateObj === 'object' && dateObj.year !== undefined) {
                formattedDate = `${dateObj.year}-${String(dateObj.monthValue).padStart(2, '0')}-${String(dateObj.dayOfMonth).padStart(2, '0')} ${String(dateObj.hour).padStart(2, '0')}:${String(dateObj.minute).padStart(2, '0')}`;
            } else {
                formattedDate = asData.as_date; // 이미 문자열로 온 경우
            }

            // 모달 내 필드 세팅
            document.getElementById('modalAsCd').innerText = asData.as_cd;
            document.getElementById('modalAsTitle').innerText = asData.as_title;
            document.getElementById('modalStaffNm').innerText = asData.staff_nm || '미지정';
            document.getElementById('modalAsContent').innerText = asData.as_content;
            document.getElementById('modalAsDate').innerText = formattedDate;
            document.getElementById('modalAsAddr').innerText = asData.as_addr;
            document.getElementById('modalAsFacility').innerText = asData.as_facility;
            document.getElementById('modalUserNm').innerText = asData.user_nm || '신청자 정보 없음';

            // 상태 셀렉트 박스 초기화
            const select = document.getElementById('statusSelect');
            select.innerHTML = '';

            const statusList = ['신고 접수', 'A/S 작업 중', '작업 완료', '작업 취소'];

            const currentIndex = statusList.indexOf(asData.as_status);

            let allowedStatuses = [];

            if (userType === 'admin') {
                // 관리자는 전체 상태 선택 가능
                allowedStatuses = statusList;
            } else if (userType === 'staff') {
                // 담당자는 현재 이후 상태만
                allowedStatuses = currentIndex >= 0 ? statusList.slice(currentIndex) : statusList;
            } else {
                // 기본 fallback (현재 상태 포함 이후 상태)
                allowedStatuses = currentIndex >= 0 ? statusList.slice(currentIndex) : statusList;
            }

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

//----------------------------------------------------------------------------------------------------

/**
 * 모달 닫기 함수
 */
function closeModal() {
    document.getElementById('statusModal').style.display = 'none';
}

//----------------------------------------------------------------------------------------------------

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
            // 기존: location.reload();
            // 상태 변경 후 테이블 데이터를 다시 로드하여 최신 정보 반영
            // 현재 조회 조건을 유지하면서 테이블과 페이지네이션만 업데이트합니다.
            $('#btnSearch').click(); // 검색 버튼 클릭 이벤트를 트리거하여 현재 조건으로 다시 조회
            closeModal(); // 모달 닫기
        },
        error: function() {
            alert('오류가 발생했습니다.');
        }
    });
}

//----------------------------------------------------------------------------------------------------

/**
 * 페이징이 적용된 AS 일정 데이터를 서버로부터 가져오는 함수.
 * @param {string} startDate - 조회 시작 날짜 (YYYY-MM-DD)
 * @param {string} endDate - 조회 종료 날짜 (YYYY-MM-DD)
 * @param {string} staffInfo - 담당자 정보 (선택 사항)
 * @param {number} page - 요청할 페이지 번호 (1-based)
 * @param {number} size - 페이지당 항목 수
 */
function fetchSchedule2(startDate, endDate, staffInfo, page = 1, size = 10) {
    console.log(`Fetching schedule2: startDate=${startDate}, endDate=${endDate}, staffInfo=${staffInfo}, page=${page}, size=${size}`);

    $.ajax({
        url: `/as/schedule2`, // 새롭게 생성된 페이징 API 엔드포인트
        method: 'GET',
        data: {
            startDate: startDate,
            endDate: endDate,
            staffInfo: staffInfo,
            page: page,
            size: size
        },
        dataType: 'json',
        success: function(response) {
            console.log('API Response (schedule2):', response);
            // 서버에서 받은 AsScheduleResponseDTO 객체의 'content' 배열로 테이블을 렌더링
            renderTable(response.content);
            // 서버에서 받은 페이징 정보로 페이지네이션을 렌더링
            renderPagination(response.currentPage, response.totalPages, startDate, endDate, staffInfo);
        },
        error: function(xhr, status, error) {
            console.error('일정 정보를 불러오는 중 오류가 발생했습니다:', error);
            alert('일정 정보를 불러오는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
    });
}

//----------------------------------------------------------------------------------------------------

/**
 * 페이지네이션을 렌더링하는 함수
 * @param {number} currentPage - 현재 페이지 번호 (1-based)
 * @param {number} totalPages - 전체 페이지 수
 * @param {string} startDate - 현재 검색 조건 (조회 유지용)
 * @param {string} endDate - 현재 검색 조건 (조회 유지용)
 * @param {string} staffInfo - 현재 검색 조건 (조회 유지용)
 */
function renderPagination(currentPage, totalPages, startDate, endDate, staffInfo) {
    const paginationContainer = $('#pagination');
    paginationContainer.empty(); // 기존 페이지네이션 비우기

    // 페이지네이션이 필요 없는 경우 (전체 페이지가 1개 이하일 때)
    if (totalPages <= 1) {
        return;
    }

    const ul = $('<ul>').addClass('pagination justify-content-center custom-pagination');
    const pageSize = 10; // 페이지 크기는 fetchSchedule2 호출 시 사용한 값과 동일하게 유지

    // 이전 페이지 버튼
    const prevLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === 1);
    const prevLink = $('<a>').addClass('page-link').attr('href', '#').html('&laquo;');
    prevLink.on('click', function(e) {
        e.preventDefault();
        if (currentPage > 1) {
            fetchSchedule2(startDate, endDate, staffInfo, currentPage - 1, pageSize);
        }
    });
    ul.append(prevLi.append(prevLink));

    // 페이지 번호 버튼들 
    const pageGroupSize = 10; // 한 번에 보여줄 페이지 번호 개수
    const startPage = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
    const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

    for (let i = startPage; i <= endPage; i++) {
        const pageLi = $('<li>').addClass('page-item').toggleClass('active', i === currentPage);
        const pageLink = $('<a>').addClass('page-link').attr('href', '#').text(i);
        pageLink.on('click', function(e) {
            e.preventDefault();
            fetchSchedule2(startDate, endDate, staffInfo, i, pageSize);
        });
        ul.append(pageLi.append(pageLink));
    }

    // 다음 페이지 버튼
    const nextLi = $('<li>').addClass('page-item').toggleClass('disabled', currentPage === totalPages);
    const nextLink = $('<a>').addClass('page-link').attr('href', '#').html('&raquo;');
    nextLink.on('click', function(e) {
        e.preventDefault();
        if (currentPage < totalPages) {
            fetchSchedule2(startDate, endDate, staffInfo, currentPage + 1, pageSize);
        }
    });
    ul.append(nextLi.append(nextLink));

    paginationContainer.append(ul);
}

//----------------------------------------------------------------------------------------------------

// FullCalendar 초기화 및 이벤트 리스너 (기존 코드와 동일하게 유지)
document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');
    let selectedStaff = '';

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', // month view
        locale: 'ko',
        height: 'auto',
        dayMaxEvents: 3,

        eventContent: function(arg) {
            const status = arg.event.extendedProps?.status || '';
            let color = '#3788d8';
            let textStyle = {};

            switch (status) {
                case '신고 접수':
                    color = '#439b43';
                    break;
                case 'A/S 작업 중':
                    color = '#f0ad4e';
                    break;
                case '작업 취소':
                    color = '#ff6b6b';
                    break;
                case '작업 완료':
                    color = '#cccccc';
                    textStyle = {
                        color: '#aaa',
                        opacity: '0.6',
                        fontStyle: 'italic'
                    };
                    break;
            }

            const dot = document.createElement('span');
            dot.style.display = 'inline-block';
            dot.style.width = '10px';
            dot.style.height = '10px';
            dot.style.borderRadius = '50%';
            dot.style.marginRight = '5px';
            dot.style.backgroundColor = color;

            const text = document.createElement('span');
            text.innerText = arg.event.title;
            Object.assign(text.style, textStyle);

            const container = document.createElement('div');
            container.style.display = 'flex';
            container.style.alignItems = 'center';
            container.appendChild(dot);
            container.appendChild(text);

            return { domNodes: [container] };
        },
        events: function(fetchInfo, successCallback, failureCallback) {
            $.ajax({
                url: '/as/calendar-data',
                type: 'GET',
                dataType: "json",
                data: {
                    staff: selectedStaff
                },
                success: function(data) {
                    const role = data.role;
                    const rowEvents = data.events;
                    const events = rowEvents.map(item => {
                        const asDate = item.as_date;
                        let startDateStr = '';

                        if (typeof asDate === 'string') {
                            startDateStr = asDate.replace(' ', 'T')
                        } else if (asDate && typeof asDate === 'object' && asDate.year) {
                            const dateObj = new Date(
                                asDate.year,
                                asDate.monthValue - 1, // JS는 0부터 시작
                                asDate.dayOfMonth,
                                asDate.hour || 0,
                                asDate.minute || 0,
                                asDate.second || 0
                            );
                            startDateStr = dateObj.toISOString();
                        } else {
                            startDateStr = new Date().toISOString();
                        }

                        return {
                            title: (role === 'admin')
                            ? `[${item.staff_nm}] ${item.as_time} ${item.as_status}` // 관리자용
                            : `${item.as_time} ${item.as_status}`,                 // 직원용
                            start: startDateStr,
                            extendedProps: {
                                asCd: item.as_cd,
                                status: item.as_status,
                                title: item.as_title,
                                staff: item.staff_nm
                            }
                        };
                    });
                    successCallback(events);
                },
                error: function(xhr) {
                    const message = xhr?.responseJSON?.message || xhr?.statusText || '알 수 없는 오류입니다.';
                    console.error('캘린더 요청 실패:', message);
                    alert('캘린더 데이터를 불러오는 데 실패했습니다.\n' + message);
                    failureCallback();
                }
            });
        },
        eventClick: function(info) {
            const asCd = info.event.extendedProps?.asCd;
            if (!asCd) {
                alert('일정 정보를 찾을 수 없습니다.');
                return;
            }
            openModal(asCd, window.userType);
        }
    });
    calendar.render();

    const $searchInput = $('#searchCalendarStaff');
    $('#btnCalendarSearch').on('click', function () {
        selectedStaff = $searchInput.val().trim(); // 공백이면 전체
        calendar.refetchEvents();
    });
    // ⚠️ 중요: 이 부분은 $(function(){...})에서 이미 초기 로드를 처리하므로 중복 호출 방지를 위해 주석 처리합니다.
    // const today = new Date().toISOString().split('T')[0]; // YYYY-MM-DD 형식
    // fetchSchedule2(today, today, '', 1, 10);
});