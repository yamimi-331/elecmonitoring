
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

    fetch("/signup/check-id?user_id="+userId)
        .then(res => res.text())
        .then(result => {
            if (result === "duplicate") {
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

//비밀번호 확인
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

