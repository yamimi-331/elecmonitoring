// 직원 정보수정 테이블의 조회 버튼 클릭
function searchStaff() {
  const staffId = document.getElementById("searchStaffById").value;

  jQuery.ajax({
    url: "/admin/search-users",
    type: "GET",
    data: { staffId },
    dataType: "json", // JSON으로 받기
    success: function (staffs) {
      const tbody = jQuery("#account-table tbody").empty();
      // 테이블 초기화
      const accountTbody = jQuery("#account-table tbody").empty();
      const noDataRow =
        '<tr><td colspan="6">조회된 데이터가 없습니다.</td></tr>';

      if (staffs.length === 0) {
        // No users found, display a message
        const noStaffsRow =
          '<tr><td colspan="6">조회된 데이터가 없습니다.</td></tr>';
        tbody.append(noStaffsRow);
      } else {
        staffs.forEach((staff) => {
          // 각 항목을 안전하게 문자열로 변환
          const addr = staff.staff_addr && staff.staff_addr.trim() !== ""
						  ? staff.staff_addr.split(" ")[0]
						  : "-";
						          
          const rowHtml =
            "<tr>" +
            "<td>" +
            staff.staff_cd +
            "</td>" +
            "<td>" +
            staff.staff_id +
            "</td>" +
            "<td>" +
            staff.staff_nm +
            "</td>" +
            "<td>" +
            staff.staff_role +
            "</td>" +
            "<td>" +
            addr +
            "</td>" +
            "<td><button onclick=\"accountDetail('" +
            staff.staff_id +
            "')\">상세 보기</button></td>" +
            "</tr>";
          tbody.append(rowHtml);
        });
      }
    },
    error: function (xhr, status, error) {
      alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
      console.error("서버 오류 내용:", xhr.responseText);
    },
  });
}
// 계정 복구 테이블의 조회 버튼 클릭
function searchDeletedAccount() {
  const userType = document.getElementById("usertype").value;
  const userId = document.getElementById("searchDeletedUserById").value;

  jQuery.ajax({
    url: "/admin/search-deleted-users",
    type: "GET",
    data: { userType: userType, id: userId },
    dataType: "json",
    success: function (users) {
      const tbody = jQuery("#rollback-table tbody").empty();

      if (users.length === 0) {
        const noUsersRow =
          '<tr><td colspan="4">조회된 데이터가 없습니다.</td></tr>';
        tbody.append(noUsersRow);
      } else {
        users.forEach((user) => {
          let rowHtml = "";
          if (userType === "staff") {
            // staff VO에 맞게 출력
            rowHtml =
              "<tr>" +
              "<td>" +
              user.staff_cd +
              "</td>" +
              "<td>" +
              user.staff_id +
              "</td>" +
              "<td>" +
              user.staff_nm +
              "</td>" +
              "<td><button onclick=\"restoreAccount('" +
              user.staff_id +
              "' , '" +
              userType +
              "')\">복구</button></td>" +
              "</tr>";
          } else if (userType === "common") {
            // 일반 사용자 VO에 맞게 출력
            rowHtml =
              "<tr>" +
              "<td>" +
              user.user_cd +
              "</td>" +
              "<td>" +
              user.user_id +
              "</td>" +
              "<td>" +
              user.user_nm +
              "</td>" +
              "<td><button onclick=\"restoreAccount('" +
              user.user_id +
              "', '" +
              userType +
              "')\">복구</button></td>" +
              "</tr>";
          }
          tbody.append(rowHtml);
        });
      }
    },
    error: function (xhr, status, error) {
      alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
      console.error("서버 오류 내용:", xhr.responseText);
    },
  });
}
// 계정 복구 버튼
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
      searchDeletedAccount(); // 리스트 다시 조회
    },
    error: function (xhr, status, error) {
      alert("복구 실패: " + xhr.status + " " + xhr.statusText);
      console.error("서버 오류 내용:", xhr.responseText);
    },
  });
}
// 모달로 정보보고 수정
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
      searchStaff(); // 테이블 갱신
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
function accountDetail(staffId) {
  $.ajax({
    url: "/admin/search-users",
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

//아이디 중복 확인
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

//비밀번호 일치 확인
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

//유효성 검사
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

//이벤트 등록
document.addEventListener("DOMContentLoaded", function () {
    // 직원
    document.querySelector("#staff_id_ck_staff").addEventListener("click", () => checkDuplicateId("staff"));
    document.querySelector("#staff_pw_ck_staff").addEventListener("input", () => checkPwd("staff"));
    document.querySelector("#staff form").addEventListener("submit", function (e) {
        if (!validateForm("staff")) {
            e.preventDefault();
        }
    });
    document.querySelector("#staff_id_staff").addEventListener("input", () => formState.staff.isIdChecked = false);

    // 관리자
    document.querySelector("#staff_id_ck_admin").addEventListener("click", () => checkDuplicateId("admin"));
    document.querySelector("#staff_pw_ck_admin").addEventListener("input", () => checkPwd("admin"));
    document.querySelector("#admin form").addEventListener("submit", function (e) {
        if (!validateForm("admin")) {
            e.preventDefault();
        }
    });
    document.querySelector("#staff_id_admin").addEventListener("input", () => formState.admin.isIdChecked = false);
});

