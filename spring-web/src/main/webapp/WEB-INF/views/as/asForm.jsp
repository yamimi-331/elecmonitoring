<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../../resources/js/asForm.js?after"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<script type="text/javascript">
//주소 API
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            const fullAddress = data.roadAddress || data.jibunAddress;
            document.getElementById("as_addr").value = fullAddress;
        }
    }).open();
}
</script>
<style>
.container {
	margin: 0;
	padding: 0;
	min-height: 90vh;  
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-direction: column;
	font-family: Arial, sans-serif;
}
.container button {
  padding: 10px;
  background: #007BFF;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.container button:hover {
  background: #0056b3;
}

.container #goback,
.container #form-submit {
  margin-top: 10px;
  background: #6c757d;
}

.container #goback:hover,
.container #form-submit:hover {
  background: #5a6268;
}

.inner-container{
	margin: 30px 0;
	display: flex;
	justify-content: flex-start;
	align-items: flex-start;
	flex-direction: column;
	gap: 10px;
}
.inner-container span{
	font-size: 18px;
	font-weight: bold;
}
.inner-container label{
	width: 100px;
	display: inline-block;
}
.inner-container input[type="text"]{
	width: 350px;
	height: 35px;
}
.inner-container select{
	width: 357px;
	height: 30px;
}

.address-container{
	display: flex;
    gap: 4px;
    align-items: center;
}
.address-container input[type="text"]{
	width: 274px;
}
.address-container button{
	height: 40px;
}

.time-container{
	display: flex;
}
.time-options {
	display: flex;
	gap: 5px;
	margin: 5px 0;
}
.time-options input[type="radio"] {
	display: none; /* 기본 라디오 숨김 */
}
.time-options label {
	padding: 5px 10px;
	width: 40px;
	font-size: 14px;
	text-align: center;
	border: 2px solid #ccc;
	border-radius: 6px;
	cursor: pointer;
	background-color: #f9f9f9;
	transition: all 0.2s ease-in-out;
}
.time-options input[type="radio"]:checked + label {
	background-color: #0070C0;
	color: white;
	border-color: #0070C0;
}
.time-options input[type="radio"]:disabled + label{
	background: #ccc;
	cursor: not-allowed;  /* 👉 커서 금지 표시 */
	opacity: 0.7;
}

.select-container{
    display: flex;
    gap: 5px;
    align-items: center;
}
.select-inner-container{
	display: flex;
    flex-direction: column;
    gap: 10px;
}

</style>
</head>
<body>
<body>
	<div class="wrapper">
		<h2 class="header-title">A/S 신고</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<h2>A/S 신고</h2>
			<div class=container>
				<form action="/as/insertCommon" method="post">
					<div class="inner-container">
						<span>신고자 정보</span>
						<div class="i">
							<label for="user_nm">이름</label>
							<input type="text" name="user_nm" id="user_nm" value="${currentUserInfo.user_nm}" readonly>
						</div>
						<div class="i">
							<label for="user_mail">이메일</label>
							<input type="text" name="user_mail" id="user_mail" value="${currentUserInfo.user_mail}">
						</div>
					</div>
					<div class="inner-container">
						<span>시설물 정보</span>
						<div class="select-container">
							<label for="as_facility">종류</label>
							<div class="select-inner-container">
							<select id="as_facility" name="as_facility">
								<option value="">-- 시설 선택 --</option>
								<option value="전기배선">전기배선</option>
								<option value="분전반">분전반</option>
								<option value="누전차단기">누전차단기</option>
								<option value="전등설비">전등설비</option>
								<option value="옥내배선">옥내배선</option>
								<option value="기타">기타 (직접 입력)</option>
							</select>
							<input type="text" name="as_facility_custom" id="as_facility_custom" placeholder="직접 입력" style="display:none;" autocomplete="off">
							</div>
						</div>
						<div class="address-container">
							<label for="as_addr">주소</label>
							<input type="text" name="as_addr" id="as_addr" autocomplete="off" readonly>
							<button type="button" onclick="searchAddress()">주소 검색</button>
						</div>
					</div>
					<div class="inner-container">
						<span>상세 정보</span>
						<div class="select-container">
							<label for="as_title">문제 종류</label>
							<div class="select-inner-container">
							<select id="as_title" name="as_title">
								<option value="">-- 문제 유형 선택 --</option>
								<option value="합선 위험">합선 위험</option>
								<option value="과열 현상">과열 현상</option>
								<option value="스파크 발생">스파크 발생</option>
								<option value="노출된 전선">노출된 전선</option>
								<option value="차단기 작동 불능">차단기 작동 불능</option>
								<option value="기타">기타 (직접 입력)</option>
							</select>
							<input type="text" id="as_title_custom" name="as_title_custom" placeholder="직접 입력" style="display:none;" autocomplete="off">
							</div>
						</div>
						<div class="i">
							<label for="as_content">상세 정보</label>
							<input type="text" name="as_content" id="as_content" autocomplete="off">
						</div>
						<div class="i">
							<label for="reserve_date">예약 일자</label>
							<input type="date" name="reserve_date" id="reserve_date">
						</div>
						<div class="time-container">
							<label>예약 시간</label>
							<div>
								<div id="time-options-first" class="time-options"></div>
								<div id="time-options-second" class="time-options"></div>
							</div>
						</div>
					</div>
					<button type="submit" id="form-submit">제출하기</button>
					<button id="goback" type="button" onclick="location.href='/as/detail'">돌아가기</button>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>