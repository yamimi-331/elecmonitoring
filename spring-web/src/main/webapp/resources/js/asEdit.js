document.addEventListener("DOMContentLoaded", () => {
	// select에서 '기타' 선택 시 input 등장
    const facilitySelect = document.getElementById("as_facility");
    const facilityInput = document.getElementById("as_facility_custom");
    
    const titleSelect = document.getElementById("as_title");
    const titleInput = document.getElementById("as_title_custom");

    function toggleCustomInput(selectElem, inputElem) {
        if (selectElem.value === "기타") {
            inputElem.style.display = "block";
        } else {
            inputElem.style.display = "none";
            inputElem.value = "";
        }
    }
    
	// 이벤트 연결
    facilitySelect.addEventListener("change", () => {
        toggleCustomInput(facilitySelect, facilityInput);
    });
    titleSelect.addEventListener("change", () => {
        toggleCustomInput(titleSelect, titleInput);
    });

    // 초기 로딩시 '기타' 선택시 커스텀 input 보여주기
    toggleCustomInput(facilitySelect, facilityInput);
    toggleCustomInput(titleSelect, titleInput);

    // 오늘 날짜 이후부터 선택 가능
    const dateInput = document.getElementById("reserve_date");
    const today = new Date();
    today.setDate(today.getDate() + 1);
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    const minDate = yyyy+ "-" +mm+ "-" +dd;
    dateInput.min = minDate;

    // 예약된 시간 선택 비활성화 및 기존 예약 시간 체크
	const timeOptions1 = document.getElementById("time-options-first");
	const timeOptions2 = document.getElementById("time-options-second");
    const existingDate = dateInput.value;
    const existingTime = timeOptions1.dataset.existingTime || "";

	// 예약 시간 선택란 반복문으로 출력
    function renderTimeOptions(bookedTimes) {
        let html1 = "";
        let html2 = "";
        let checkedSet = false;
        
        for (let hour = 9; hour <= 13; hour++) {
            const hourStr = (hour < 10 ? "0" : "") + hour + ":00";
            const isBooked = bookedTimes.includes(hourStr);
            let disabled = "";
            let checked = "";
            
            if (hourStr === existingTime) {
                checked = "checked";
                checkedSet = true;
            } else if (isBooked) {
                disabled = "disabled";
            }

            const id = "reserve_time_" + hourStr;
            html1 += "<input type='radio' id='" + id + "' name='reserve_time' value='" + hourStr + "' " + disabled + " " + checked + ">";
            html1 += "<label for='" + id + "'>" + hourStr + "</label>";
        }
        timeOptions1.innerHTML = html1;
        
        for (let hour = 14; hour <= 17; hour++) {
            const hourStr = (hour < 10 ? "0" : "") + hour + ":00";
            const isBooked = bookedTimes.includes(hourStr);

            let disabled = "";
            let checked = "";

            if (hourStr === existingTime && !checkedSet) {
                checked = "checked";
                checkedSet = true;
            } else if (isBooked) {
                disabled = "disabled";
            }

            const id = "reserve_time_" + hourStr;

            html2 += "<input type='radio' id='" + id + "' name='reserve_time' value='" + hourStr + "' " + disabled + " " + checked + ">";
            html2 += "<label for='" + id + "'>" + hourStr + "</label>";
        }
        timeOptions2.innerHTML = html2;
    }

	// 예약이 완료된 시간 json 형태로 받아오기
    function fetchBookedTimes(selectedDate) {
    	if (!selectedDate) {
            timeOptions.innerHTML = "";
            return;
        }

    	const regionInput = document.getElementById("as_addr");
		let fullRegion = regionInput.value;

		// 공백 기준으로 앞자리만 가져오기 (예: "서울특별시")
		let region = fullRegion.split(' ')[0];
		
        $.ajax({
            url: "/as/form/booked-times",
            method: "GET",
            data: { selectedDate, region },
            dataType: "json",
            success: function(bookedTimes) {
                renderTimeOptions(bookedTimes);
            },
            error: function() {
                alert("예약된 시간을 불러오는 데 실패했습니다.");
            }
        });
    }

    // 최초 렌더링 시 예약시간 옵션 생성
    if (existingDate) {
        fetchBookedTimes(existingDate);
    }

    // 날짜 변경시 예약된 시간 다시 불러오기
    dateInput.addEventListener("change", function() {
        fetchBookedTimes(this.value);
    });
    
    // 유효성 검증
    const form = document.querySelector('#updateForm');
    form.addEventListener("submit", function (e) {
        const userMail = document.getElementById("user_mail");
        const asFacility = document.getElementById("as_facility");
        const asFacilityCustom = document.getElementById("as_facility_custom");
        const asAddr = document.getElementById("as_addr");
        const asTitle = document.getElementById("as_title");
        const asTitleCustom = document.getElementById("as_title_custom");
        const asContent = document.getElementById("as_content");
        const reserveDate = document.getElementById("reserve_date");
        const reserveTimeRadios = document.querySelectorAll('input[name="reserve_time"]');
        
        // 필수값 검증
        if (userMail.value.trim() === "") {
            alert("이메일을 입력해주세요.");
            userMail.focus();
            e.preventDefault();
            return;
        }
        if (asFacility.value === "") {
            alert("시설 종류를 선택해주세요.");
            asFacility.focus();
            e.preventDefault();
            return;
        }
        if (asFacility.value === "기타" && asFacilityCustom.value.trim() === "") {
            alert("시설 종류를 직접 입력해주세요.");
            asFacilityCustom.focus();
            e.preventDefault();
            return;
        }
        if (asAddr.value.trim() === "") {
            alert("주소를 입력해주세요.");
            asAddr.focus();
            e.preventDefault();
            return;
        }
        if (asTitle.value === "") {
            alert("문제 종류를 선택해주세요.");
            asTitle.focus();
            e.preventDefault();
            return;
        }
        if (asTitle.value === "기타" && asTitleCustom.value.trim() === "") {
            alert("문제 종류를 직접 입력해주세요.");
            asTitleCustom.focus();
            e.preventDefault();
            return;
        }
        if (asContent.value.trim() === "") {
            alert("상세 정보를 입력해주세요.");
            asContent.focus();
            e.preventDefault();
            return;
        }
        if (reserveDate.value === "") {
            alert("예약 일자를 선택해주세요.");
            reserveDate.focus();
            e.preventDefault();
            return;
        }
        let timeSelected = false;
        reserveTimeRadios.forEach(radio => {
            if (radio.checked) timeSelected = true;
        });
        if (!timeSelected) {
            alert("예약 시간을 선택해주세요.");
            e.preventDefault();
            return;
        }
    });
    
	window.confirmCancel = function () {
		return confirm(
			"정말로 예약을 취소하시겠습니까?\n동일 시간으로 다시 예약하실 수 없을 수 있습니다."
		);
	};
});