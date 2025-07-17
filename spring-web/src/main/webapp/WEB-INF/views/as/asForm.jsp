<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>A/S 신고</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../../resources/js/asForm.js?after"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/reportForm.css?after" />
<script type="text/javascript">
//주소 API
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            const fullAddress = data.roadAddress || data.jibunAddress;
            document.getElementById("as_addr_display").value = fullAddress;
        }
    }).open();
}
</script>
</head>
<body>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
		<main class="main">
			<h2>A/S 신고</h2>
			<div class=container>
				<form action="/as/insertCommon" method="post" id="as-form">
					<span>신고자 정보</span>
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<c:choose>
							<c:when test="${sessionScope.userType == 'common'}">
								<tr>
									<th><label for="user_nm">이름</label></th>
									<td><input type="text" name="user_nm" id="user_nm" value="${currentUserInfo.user_nm}" readonly></td>
								</tr>
								<tr>
									<th><label for="user_mail">이메일</label></th>
									<td><input type="text" name="user_mail" id="user_mail" value="${currentUserInfo.user_mail}"></td>
								</tr>
							</c:when>
							<c:when test="${sessionScope.userType == 'guest'}">
								<tr>
									<th><label for="user_nm">이름</label></th>
									<td><input type="text" name="guest_nm" id="guest_nm" value="${currentUserInfo.guest_nm}" readonly></td>
								</tr>
								<tr>
									<th><label for="user_mail">이메일</label></th>
									<td><input type="text" name="guest_mail" id="guest_mail" value="${currentUserInfo.guest_mail}"></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<th><label for="user_nm">이름</label></th>
									<td><input type="text" name="user_nm" id="user_nm" value="" readonly></td>
								</tr>
								<tr>
									<th><label for="user_mail">이메일</label></th>
									<td><input type="text" name="user_mail" id="user_mail" value=""></td>
								</tr>   
							</c:otherwise>
						</c:choose>
					</table>
					<span>시설물 정보</span>
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="as_facility">종류</label></th>
							<td>
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
							</td>
						</tr>
						<tr>
							<th><label for="as_addr">주소</label></th>
							<td>
								<div class="addr-search">
									<input type="text" id="as_addr_display" autocomplete="off" readonly>
									<button type="button" onclick="searchAddress()">주소 검색</button>
									<input type="hidden" name="as_addr" id="as_addr_hidden" autocomplete="off">
								</div>
							</td>
						</tr>
						<tr>
							<th><label for="as_detail">상세 주소</label></th>
							<td><input type="text" name="as_detail" id="as_detail" autocomplete="off"></td>
						</tr>
					</table>
					<span>상세 정보</span>
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="as_title">문제 종류</label></th>
							<td>
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
							</td>
						</tr>
						<tr>
							<th><label for="as_content">상세 정보</label></th>
							<td><textarea name="as_content" id="as_content" placeholder="내용"></textarea></td>
						</tr>
						<tr>
							<th><label for="reserve_date">예약 일자</label></th>
							<td><input type="date" name="reserve_date" id="reserve_date"></td>
						</tr>
						<tr class="time-container">
							<th><label>예약 시간</label></th>
							<td>
								<div>
									<div id="time-options-first" class="time-options">
										<input type='radio' id='reserve_time_09:00' name='reserve_time' value='09:00' disabled>
										<label for='reserve_time_09:00'>09:00</label>
										<input type='radio' id='reserve_time_10:00' name='reserve_time' value='10:00' disabled>
										<label for='reserve_time_10:00'>10:00</label>
										<input type='radio' id='reserve_time_11:00' name='reserve_time' value='11:00' disabled>
										<label for='reserve_time_11:00'>11:00</label>
										<input type='radio' id='reserve_time_12:00' name='reserve_time' value='12:00' disabled>
										<label for='reserve_time_12:00'>12:00</label>
										<input type='radio' id='reserve_time_13:00' name='reserve_time' value='13:00' disabled>
										<label for='reserve_time_13:00'>13:00</label>
									</div>
									<div id="time-options-second" class="time-options">
										<input type='radio' id='reserve_time_14:00' name='reserve_time' value='14:00' disabled>
										<label for='reserve_time_14:00'>14:00</label>
										<input type='radio' id='reserve_time_15:00' name='reserve_time' value='15:00' disabled>
										<label for='reserve_time_15:00'>15:00</label>
										<input type='radio' id='reserve_time_16:00' name='reserve_time' value='16:00' disabled>
										<label for='reserve_time_16:00'>16:00</label>
										<input type='radio' id='reserve_time_17:00' name='reserve_time' value='17:00' disabled>
										<label for='reserve_time_17:00'>17:00</label>
									</div>
								</div>
							</td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="form-submit">등록하기</button>
						<button type="button" onclick="location.href='/as/detail'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>