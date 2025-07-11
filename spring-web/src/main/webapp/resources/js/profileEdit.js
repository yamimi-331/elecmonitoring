$(document).ready(function () {
  // 상태 변수
  let isPwVerified = false;
  let isNewPwValid = false;
  let isPwMatch = false;

  const userSocial = $("#user_social").val(); // hidden input 값 가져옴
  // Basic이 아니면 기존 비밀번호 확인은 PASS 처리
  if (userSocial !== "Basic") {
    isPwVerified = true;
  }
  
  // 기존 비밀번호 입력 시 상태 초기화
  $("#prepw").on("input", function () {
    $("#pwVerifyMsg").text("").css("color", "");
    isPwVerified = false;
  });

  // 기존 비밀번호 확인 버튼 클릭
  $('button[type="button"]').on("click", function () {
    var inputPw = $("#prepw").val().trim();
    if (inputPw === "") {
      $("#pwVerifyMsg").text("비밀번호를 입력하세요.").css("color", "red");
      isPwVerified = false;
      return;
    }

    $.ajax({
      url: "/profileEdit/checkPassword",
      type: "POST",
      data: { inputPw: inputPw },
      success: function (result) {
        if (result) {
          $("#pwVerifyMsg")
            .text("비밀번호가 일치합니다.")
            .css("color", "green");
          isPwVerified = true;
        } else {
          $("#pwVerifyMsg")
            .text("비밀번호가 일치하지 않습니다.")
            .css("color", "red");
          isPwVerified = false;
        }
      },
      error: function () {
        $("#pwVerifyMsg").text("서버 오류가 발생했습니다.").css("color", "red");
        isPwVerified = false;
      },
    });
  });

  // 새 비밀번호 유효성 검사
  $("#pw").on("input", function () {
    var newPw = $(this).val();
    var msg = "";

    if (newPw.length < 8) {
      msg = "비밀번호는 8자 이상이어야 합니다.";
      isNewPwValid = false;
    } else if (!/[0-9]/.test(newPw) || !/[a-zA-Z]/.test(newPw)) {
      msg = "영문과 숫자를 포함해야 합니다.";
      isNewPwValid = false;
    } else {
      msg = "사용 가능한 비밀번호입니다.";
      isNewPwValid = true;
    }
    $("#newPwMsg")
      .text(msg)
      .css("color", isNewPwValid ? "green" : "red");
  });

  // 새 비밀번호 확인 일치 검사
  $("#user_pw_ck").on("input", function () {
    var newPw = $("#pw").val();
    var confirmPw = $(this).val();
    var msg = "";

    if (newPw === confirmPw && newPw !== "") {
      msg = "비밀번호가 일치합니다.";
      isPwMatch = true;
      $("#pwCheckMsg").css("color", "green");
    } else {
      msg = "비밀번호가 일치하지 않습니다.";
      isPwMatch = false;
      $("#pwCheckMsg").css("color", "red");
    }
    $("#pwCheckMsg").text(msg);
  });

  // 최종 제출 시 유효성 검사
  $("form").on("submit", function (e) {
    if (!isPwVerified) {
      alert("기존 비밀번호를 확인해주세요.");
      e.preventDefault();
      return false;
    }
    var newPw = $("#pw").val();
    if (newPw.trim() !== "") {
      // 새 비밀번호가 입력되었을 때만 검사
      if (!isNewPwValid) {
        alert("새 비밀번호가 유효하지 않습니다.");
        e.preventDefault();
        return false;
      }
      if (!isPwMatch) {
        alert("새 비밀번호가 일치하지 않습니다.");
        e.preventDefault();
        return false;
      }
    }
  });

  window.confirmDelete = function () {
    console.log("탈퇴함수 진입");
    if (!isPwVerified) {
      alert("현재 비밀번호 확인을 먼저 해주세요.");
      return false;
    }
    return confirm(
      "정말로 회원을 탈퇴하시겠습니까?\n계정복구는 당사로 문의해주시기 바랍니다.\n※ 탈퇴 시, 남아있는 모든 신고 접수 상태의 예약은 자동 취소됩니다."
    );
  };
});
