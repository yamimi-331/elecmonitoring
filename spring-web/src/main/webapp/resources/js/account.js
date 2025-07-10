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
            staff.staff_addr.split(" ")[0] +
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
$("#staff-detail-form").on("submit", function (e) {
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
    success: function () {
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

      let fullAddr = staff[0].staff_addr; // 예: "울산광역시 남구"

      // select option 돌면서 포함 여부 확인 후 선택
      $("#modal-staff-addr option").each(function () {
        const optVal = $(this).val();
        if (fullAddr.includes(optVal)) {
          $("#modal-staff-addr").val(optVal);
          return false;
        }
      });

      // 모달 열기
      $("#staff-modal").show();
    },
    error: function (xhr) {
      alert("직원 상세 정보 가져오기 실패");
      console.error(xhr.responseText);
    },
  });
}
