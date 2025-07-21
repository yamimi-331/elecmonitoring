// asOder.js 또는 해당 JavaScript 파일

// ===========================================================================
// 1. 공통 유틸리티 함수 (날짜 포맷 등)
// ===========================================================================
const formatDate = (date) => date.toISOString().split('T')[0]; // YYYY-MM-DD 형식


// ===========================================================================
// 2. 페이징 렌더링 함수 (가장 먼저 정의되어야 함)
// ===========================================================================
/**
 * 계정 관리 페이지의 두 테이블 (직원 정보수정 및 계정 복구)에 대한 페이지네이션을 렌더링하는 범용 함수.
 * @param {number} currentPage - 현재 페이지 번호 (서버 응답 기준, 0-based)
 * @param {number} totalPages - 전체 페이지 수
 * @param {string} tableType - 'account' (직원 정보수정) 또는 'rollback' (계정 복구)
 * @param {string} searchKeyword - 현재 적용된 검색 키워드 (직원 ID 또는 삭제된 사용자 ID)
 * @param {string} [userType=''] - 'rollback' 테이블일 경우, 추가로 필요한 userType (staff/common)
 */
function renderPagination(currentPage, totalPages, tableType, searchKeyword, userType = '') {
    // 테이블 타입에 따라 페이지네이션 컨테이너 ID 설정
    const paginationContainerId = (tableType === 'account') ? '#account-pagination' : '#rollback-pagination';
    const paginationContainer = jQuery(paginationContainerId);
    paginationContainer.empty(); // 기존 페이지네이션 내용 비우기

    // 사용자에게 보여줄 페이지 번호는 1-based로 변환
    const displayCurrentPage = currentPage + 1;

    // 전체 페이지가 1개 이하면 페이지네이션 불필요
    if (totalPages <= 1) {
        return;
    }

    const ul = jQuery('<ul>').addClass('pagination justify-content-center custom-pagination'); // 공통 CSS 클래스 적용
    const pageSize = 10; // 페이지당 항목 수는 고정 (searchStaff/searchDeletedAccount 함수에서도 사용)

    // '이전' 페이지 버튼 생성
    const prevLi = jQuery('<li>').addClass('page-item').toggleClass('disabled', displayCurrentPage === 1);
    const prevLink = $('<a>').addClass('page-link').attr('href', '#').html('&laquo;');
    prevLink.on('click', function(e) {
        e.preventDefault();
        if (displayCurrentPage > 1) {
            if (tableType === 'account') {
                searchStaff(currentPage - 1, pageSize); // 이전 페이지의 0-based 번호 전달
            } else {
                // searchDeletedAccount 호출 시 userType과 userId를 다시 가져와야 함
                // userId는 searchKeyword로 전달됨
                searchDeletedAccount(currentPage - 1, pageSize);
            }
        }
    });
    ul.append(prevLi.append(prevLink));

    // 페이지 번호 버튼들 생성
    const pageGroupSize = 5; // 한 번에 보여줄 페이지 번호 개수
    const startPage = Math.floor((displayCurrentPage - 1) / pageGroupSize) * pageGroupSize + 1;
    const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

    for (let i = startPage; i <= endPage; i++) {
        const pageLi = jQuery('<li>').addClass('page-item').toggleClass('active', i === displayCurrentPage);
        const pageLink = jQuery('<a>').addClass('page-link').attr('href', '#').text(i);
        pageLink.on('click', function(e) {
            e.preventDefault();
            if (tableType === 'account') {
                searchStaff(i - 1, pageSize); // 클릭한 페이지의 0-based 번호 전달
            } else {
                searchDeletedAccount(i - 1, pageSize); // 클릭한 페이지의 0-based 번호 전달
            }
        });
        ul.append(pageLi.append(pageLink));
    }

    // '다음' 페이지 버튼 생성
    const nextLi = jQuery('<li>').addClass('page-item').toggleClass('disabled', displayCurrentPage === totalPages);
    const nextLink = $('<a>').addClass('page-link').attr('href', '#').html('&raquo;');
    nextLink.on('click', function(e) {
        e.preventDefault();
        if (displayCurrentPage < totalPages) {
            if (tableType === 'account') {
                searchStaff(currentPage + 1, pageSize); // 다음 페이지의 0-based 번호 전달
            } else {
                searchDeletedAccount(currentPage + 1, pageSize); // 다음 페이지의 0-based 번호 전달
            }
        }
    });
    ul.append(nextLi.append(nextLink));

    paginationContainer.append(ul); // 완성된 페이지네이션을 컨테이너에 추가
}


// ===========================================================================
// 3. 직원 정보수정 및 계정 복구 관련 함수
// ===========================================================================

// 직원 정보수정 테이블의 조회 버튼 클릭
function searchStaff(page = 0, size = 10) { // 서버가 0-based 페이지를 사용한다고 가정하여 기본값 0 설정
    const staffId = document.getElementById("searchStaffById").value; // 검색어

    jQuery.ajax({
        url: "/admin/search-users-paged", // 페이징 적용된 새로운 API 엔드포인트 호출
        type: "GET",
        data: { staffId: staffId, page: page, size: size }, // 페이징 파라미터와 검색어 전달
        dataType: "json",
        success: function (response) { // 서버 응답이 StaffPageResponseDTO 객체라고 가정
            const tbody = jQuery("#account-table tbody").empty(); // 테이블 내용 초기화

            if (response.content.length === 0) { // 조회된 데이터가 없는 경우
                const noStaffsRow = '<tr><td colspan="6">조회된 데이터가 없습니다.</td></tr>';
                tbody.append(noStaffsRow);
            } else {
                response.content.forEach((staff) => { // 서버에서 받은 데이터(content)를 순회하며 테이블 행 생성
                    const addr = staff.staff_addr && staff.staff_addr.trim() !== ""
                                ? staff.staff_addr.split(" ")[0]
                                : "-";

                    const rowHtml =
                        "<tr>" +
                        "<td>" + staff.staff_cd + "</td>" +
                        "<td>" + staff.staff_id + "</td>" +
                        "<td>" + staff.staff_nm + "</td>" +
                        "<td>" + staff.staff_role + "</td>" +
                        "<td>" + addr + "</td>" +
                        "<td><button class=\"btn btn-info btn-sm\" onclick=\"accountDetail('" +
                        staff.staff_id +
                        "')\">상세 보기</button></td>" +
                        "</tr>";
                    tbody.append(rowHtml);
                });
            }
            // 직원 정보수정 테이블의 페이지네이션 렌더링
            // response.currentPage는 0-based, totalPages는 총 페이지 수
            renderPagination(response.currentPage, response.totalPages, 'account', staffId); // searchKeyword로 staffId 전달
        },
        error: function (xhr, status, error) {
            alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
            console.error("서버 오류 내용:", xhr.responseText);
        },
    });
}

// 계정 복구 테이블의 조회 버튼 클릭
function searchDeletedAccount(page = 0, size = 10) { // 서버가 0-based 페이지를 사용한다고 가정하여 기본값 0 설정
    const userType = document.getElementById("usertype").value; // 검색할 사용자 유형
    const userId = document.getElementById("searchDeletedUserById").value; // 검색어

    jQuery.ajax({
        url: "/admin/search-deleted-users-paged", // 페이징 적용된 새로운 API 엔드포인트 호출
        type: "GET",
        data: { userType: userType, id: userId, page: page, size: size }, // 페이징 파라미터와 검색어 전달
        dataType: "json",
        success: function (response) { // 서버 응답이 UserPageResponseDTO 또는 StaffPageResponseDTO 객체라고 가정
            const tbody = jQuery("#rollback-table tbody").empty(); // 테이블 내용 초기화

            if (response.content.length === 0) { // 조회된 데이터가 없는 경우
                const noUsersRow = '<tr><td colspan="4">조회된 데이터가 없습니다.</td></tr>';
                tbody.append(noUsersRow);
            } else {
                response.content.forEach((user) => { // 서버에서 받은 데이터(content)를 순회하며 테이블 행 생성
                    let rowHtml = "";
                    if (userType === "staff") {
                        // staff VO에 맞게 출력
                        rowHtml =
                            "<tr>" +
                            "<td>" + user.staff_cd + "</td>" +
                            "<td>" + user.staff_id + "</td>" +
                            "<td>" + user.staff_nm + "</td>" +
                            "<td><button class=\"btn btn-warning btn-sm\" onclick=\"restoreAccount('" +
                            user.staff_id +
                            "' , '" + userType + "')\">복구</button></td>" +
                            "</tr>";
                    } else if (userType === "common") {
                        // 일반 사용자 VO에 맞게 출력
                        rowHtml =
                            "<tr>" +
                            "<td>" + user.user_cd + "</td>" +
                            "<td>" + user.user_id + "</td>" +
                            "<td>" + user.user_nm + "</td>" +
                            "<td><button class=\"btn btn-warning btn-sm\" onclick=\"restoreAccount('" +
                            user.user_id +
                            "', '" + userType + "')\">복구</button></td>" +
                            "</tr>";
                    }
                    tbody.append(rowHtml);
                });
            }
            // 계정 복구 테이블의 페이지네이션 렌더링
            // response.currentPage는 0-based, totalPages는 총 페이지 수
            renderPagination(response.currentPage, response.totalPages, 'rollback', userId, userType); // searchKeyword로 userId 전달, userType도 전달
        },
        error: function (xhr, status, error) {
            alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
            console.error("서버 오류 내용:", xhr.responseText);
        },
    });
}

// 계정 복구 버튼 (restoreAccount) 함수 내에서 searchDeletedAccount 호출 부분 변경
function restoreAccount(id, userType) {
    if (!confirm("해당 계정을 복구하시겠습니까?")) {
        return; // 취소 시 종료
    }

    jQuery.ajax({
        url: "/admin/restore-account",
        type: "POST",
        data: {
            userType: userType,
            id: id,
        },
        success: function (result) {
            alert("계정이 성공적으로 복구되었습니다.");
            // 복구 후 계정 복구 리스트 다시 조회 (0페이지, 10개씩)
            searchDeletedAccount(0, 10);
        },
        error: function (xhr, status, error) {
            alert("복구 실패: " + xhr.status + " " + xhr.statusText);
            console.error("서버 오류 내용:", xhr.responseText);
        },
    });
}

// 모달로 정보보고 수정 (submit 이벤트 핸들러)
$(document).on("submit", "#staff-detail-form", function (e) {
    e.preventDefault(); // 기본 submit 막음

    const staffData = {
        staff_cd: $("#modal-staff-cd").val(),
        staff_id: $("#modal-staff-id").val(),
        staff_nm: $("#modal-staff-nm").val(),
        staff_role: $("#modal-staff-role").val(),
        staff_addr: $("#modal-staff-addr").val(),
    };

    $.ajax({
        url: "/admin/update-staff",
        type: "POST",
        data: staffData,
        success: function (response) {
            alert("수정 완료!");
            closeModal();
            // 수정 후 직원 정보 테이블 갱신 (0페이지, 10개씩)
            searchStaff(0, 10);
        },
        error: function () {
            alert("오류 발생");
        },
    });
});

// 모달 닫기
function closeModal() {
    $("#staff-modal").hide();
    $("#new-staff-modal").hide();
}

// accountDetail 함수는 변경 없음 (기존 /admin/search-users 엔드포인트 사용)
function accountDetail(staffId) {
    jQuery.ajax({ // jQuery로 변경
        url: "/admin/search-users", // 이 엔드포인트는 페이징이 없는 기존 엔드포인트를 사용합니다.
        type: "GET",
        data: { staffId: staffId },
        dataType: "json",
        success: function (staff) {
            // 모달에 값 세팅
            $("#modal-staff-cd").val(staff[0].staff_cd);
            $("#modal-staff-id").val(staff[0].staff_id);
            $("#modal-staff-nm").val(staff[0].staff_nm);
            $("#modal-staff-role").val(staff[0].staff_role);

            let fullAddr = staff[0].staff_addr || ""; // null 방지

            let matched = false;

            $("#modal-staff-addr option").each(function () {
                const optVal = $(this).val();
                if (fullAddr.includes(optVal)) {
                    $("#modal-staff-addr").val(optVal);
                    matched = true;
                    return false; // 첫 일치에서 종료
                }
            });

            // 아무것도 못 찾으면 "-" 선택
            if (!matched) {
                $("#modal-staff-addr").val("-"); // 기본 '없음' 옵션 선택
            }

            // 모달 열기
            $("#staff-modal").show();
        },
        error: function (xhr) {
            alert("직원 상세 정보 가져오기 실패");
            console.error(xhr.responseText);
        },
    });
}

function createStaff() {
    $("#new-staff-modal").show();

    // 탭 초기화 (직원 탭으로)
    $('#newStaff-tabs a').removeClass('active');
    $('#newStaff-tabs a[data-tab="staff"]').addClass('active');

    $('.newStaff-form').removeClass('active');
    $('#staff').addClass('active');
}

// ===========================================================================
// 4. 초기 로딩 및 이벤트 리스너 등록
// ===========================================================================
document.addEventListener("DOMContentLoaded", function () {
    // 직원 정보 테이블 초기 로드
    searchStaff(0, 10); // 0페이지부터 10개씩 조회

    // 계정 복구 테이블 초기 로드 (필요하다면 주석 해제)
    // usertype 드롭다운의 초기값에 따라 호출됩니다.
    // document.getElementById("usertype").value = "staff"; // 예시: 기본값 'staff' 설정
    // searchDeletedAccount(0, 10); // userType이 설정된 후 호출

    // 직원 계정 생성 모달 관련 탭 이벤트 리스너 (기존 코드 유지)
    document.querySelectorAll('#newStaff-tabs a').forEach(function(tab) {
        tab.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('data-tab');

            // 탭 active
            document.querySelectorAll('#newStaff-tabs a').forEach(function(t) {
                t.classList.remove('active');
            });
            this.classList.add('active');

            // 폼 active
            document.querySelectorAll('.newStaff-form').forEach(function(form) {
                if (form.id === targetId) {
                    form.classList.add('active');
                } else {
                    form.classList.remove('active');
                    form.querySelectorAll('input[type="text"], input[type="password"]').forEach(function(input) {
                        input.value = '';
                    });
                    form.querySelectorAll('small').forEach(function(small) {
                        small.innerText = '';
                    });
                    form.querySelector('button[type="submit"]').disabled = false;
                }
            });
        });
    });

    // 상태 변수 폼별로 분리
    const formState = {
        staff: { isIdChecked: false, isPwdValid: false },
        admin: { isIdChecked: false, isPwdValid: false }
    };

    // 아이디 중복 확인
    function checkDuplicateId(formType) {
        const input = document.querySelector("#staff_id_"+formType);
        const userId = input.value.trim();
        const msgEl = document.querySelector("#idCheckMsg_"+formType);

        if (!userId) {
            alert("아이디를 입력하세요.");
            input.focus();
            return;
        }

        fetch("/admin/checkId?staff_id="+userId)
            .then(res => res.text())
            .then(result => {
                if (result === "이미 사용 중인 아이디입니다.") {
                    msgEl.textContent = "이미 사용 중인 아이디입니다.";
                    msgEl.style.color = "red";
                    formState[formType].isIdChecked = false;
                } else {
                    msgEl.textContent = "사용 가능한 아이디입니다!";
                    msgEl.style.color = "green";
                    formState[formType].isIdChecked = true;
                }
            })
            .catch(err => {
                console.error("중복 확인 에러:", err);
                msgEl.textContent = "오류 발생";
                msgEl.style.color = "red";
                formState[formType].isIdChecked = false;
            });
    }

    // 비밀번호 일치 확인
    function checkPwd(formType) {
        const pw = document.querySelector("#staff_pw_" + formType).value;
        const pw_ck = document.querySelector("#staff_pw_ck_"+formType).value;
        const pwCkInput = document.querySelector("#staff_pw_ck_"+formType);
        const msgEl = document.querySelector("#pwCheckMsg_"+formType);

        msgEl.textContent = "";
        msgEl.style.color = "";

        if (!pw || !pw_ck || pw !== pw_ck) {
            pwCkInput.classList.remove("is-valid");
            pwCkInput.classList.add("is-invalid");
            msgEl.textContent = "비밀번호가 일치하지 않습니다.";
            msgEl.style.color = "red";
            formState[formType].isPwdValid = false;
        } else {
            pwCkInput.classList.remove("is-invalid");
            pwCkInput.classList.add("is-valid");
            msgEl.textContent = "";
            formState[formType].isPwdValid = true;
        }
    }

    // 유효성 검사
    function validateForm(formType) {
        const prefix = formType === "staff" ? "#staff" : "#admin";

        const idInput = document.querySelector(prefix+' input[name="staff_id"]');
        const pwInput = document.querySelector(prefix+' input[name="staff_pw"]');
        const nameInput = document.querySelector(prefix+' input[name="staff_nm"]');
        const addrInput = document.querySelector(prefix+' input[name="staff_addr"]');

        if (!idInput.value.trim()) {
            alert("아이디를 입력해주세요.");
            idInput.focus();
            return false;
        }
        if (!formState[formType].isIdChecked) {
            alert("아이디 중복 확인을 해주세요.");
            idInput.focus();
            return false;
        }
        if (!pwInput.value.trim()) {
            alert("비밀번호를 입력해주세요.");
            pwInput.focus();
            return false;
        }
        if (!formState[formType].isPwdValid) {
            alert("비밀번호 확인을 완료해주세요.");
            document.querySelector("#staff_pw_ck_"+formType).focus();
            return false;
        }
        if (!nameInput.value.trim()) {
            alert("이름을 입력해주세요.");
            nameInput.focus();
            return false;
        }
        if (formType === "staff" && !addrInput.value.trim()) {
            alert("주소를 입력해주세요.");
            addrInput.focus();
            return false;
        }

        return true;
    }

    // 기타 유효성 검사 및 중복 확인 이벤트 리스너 (기존 코드 유지)
    document.querySelector("#staff_id_ck_staff").addEventListener("click", () => checkDuplicateId("staff"));
    document.querySelector("#staff_pw_ck_staff").addEventListener("input", () => checkPwd("staff"));
    document.querySelector("#staff form").addEventListener("submit", function (e) {
        if (!validateForm("staff")) {
            e.preventDefault();
        }
    });
    document.querySelector("#staff_id_staff").addEventListener("input", () => formState.staff.isIdChecked = false);

    document.querySelector("#staff_id_ck_admin").addEventListener("click", () => checkDuplicateId("admin"));
    document.querySelector("#staff_pw_ck_admin").addEventListener("input", () => checkPwd("admin"));
    document.querySelector("#admin form").addEventListener("submit", function (e) {
        if (!validateForm("admin")) {
            e.preventDefault();
        }
    });
    document.querySelector("#staff_id_admin").addEventListener("input", () => formState.admin.isIdChecked = false);
});