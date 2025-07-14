<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>A/S 상세 정보</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="../../resources/js/asEdit.js?after"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/asForm.css?after" />
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

<div class="wrapper">
		<h2 class="header-title">A/S 상세 정보</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<h2>A/S 상세 정보</h2>
			<div class="container">
				<form action="<c:choose>
					            <c:when test='${sessionScope.userType == "common"}'>/as/updateCommon</c:when>
					            <c:when test='${sessionScope.userType == "guest"}'>/as/updateGuest</c:when>
					            <c:otherwise>/as/updateCommon</c:otherwise>
					          </c:choose>" method="post" class="as-form" id="updateForm">
					<input type="hidden" name="as_cd" value="${asVO.as_cd}" />
					<input type="hidden" name="as_status" value="${asVO.as_status}" />
					<input type="hidden" name="staff_cd" value="${asVO.staff_cd}" />
					<div class="inner-container">
						<span>신고자 정보</span>
						<c:choose>
						  <c:when test="${sessionScope.userType == 'common'}">
							<div class="i">
								<label for="user_nm">이름</label>
								<input type="text" name="user_nm" id="user_nm" value="${currentUserInfo.user_nm}" readonly>
							</div>
							<div class="i">
								<label for="user_mail">이메일</label>
								<input type="text" name="user_mail" id="user_mail" value="${asVO.user_mail}">
							</div>
						  </c:when>
						  <c:when test="${sessionScope.userType == 'guest'}">
							   <div class="i">
									<label for="user_nm">이름</label>
								    <input type="text" name="guest_nm" id="guest_nm" value="${asVO.guest_nm}" readonly>
								</div>
								<div class="i">
									<label for="user_mail">이메일</label>
								    <input type="text" name="guest_mail" id="guest_mail" value="${asVO.guest_mail}">
								</div>
						  </c:when>
						  <c:otherwise>
							<div class="i">
								<label for="user_nm">이름</label>
								<input type="text" name="user_nm" id="user_nm" value="" readonly>
							</div>
							<div class="i">
								<label for="user_mail">이메일</label>
							    <input type="text" name="user_mail" id="user_mail" value="">
							</div>    
						  </c:otherwise>
						</c:choose>
					</div>
					<div class="inner-container">
						<span>시설물 정보</span>
						<div class="select-container">
							<label for="as_facility">종류</label> 
							<div class="select-inner-container">
							<select id="as_facility" name="as_facility">
								<option value="">-- 시설 선택 --</option>
								<option value="전기배선" ${asVO.as_facility == '전기배선' ? 'selected' : ''}>전기배선</option>
								<option value="분전반" ${asVO.as_facility == '분전반' ? 'selected' : ''}>분전반</option>
								<option value="누전차단기" ${asVO.as_facility == '누전차단기' ? 'selected' : ''}>누전차단기</option>
								<option value="전등설비" ${asVO.as_facility == '전등설비' ? 'selected' : ''}>전등설비</option>
								<option value="옥내배선" ${asVO.as_facility == '옥내배선' ? 'selected' : ''}>옥내배선</option>
								<c:set var="facilityKnown" value="false" />
							    <c:if test="${asVO.as_facility == '전기배선' or asVO.as_facility == '분전반' or asVO.as_facility == '누전차단기' or asVO.as_facility == '전등설비' or asVO.as_facility == '옥내배선'}">
							        <c:set var="facilityKnown" value="true" />
							    </c:if>
							    <option value="기타" <c:if test="${!facilityKnown}">selected</c:if>>기타 (직접 입력)</option>
							</select>
							<input type="text" name="as_facility_custom" id="as_facility_custom" placeholder="직접 입력" autocomplete="off" style="display:none;"
	    					value="<c:if test='${!facilityKnown}'>${asVO.as_facility}</c:if>"/>
	    					</div>
						</div>
						<div class="address-container">
							<label for="as_addr">주소</label>
							<input type="text" id="as_addr_display" autocomplete="off" readonly value="${base_addr}">
							<button type="button" onclick="searchAddress()">주소 검색</button>
							<input type="hidden" name="as_addr" id="as_addr_hidden" autocomplete="off">
						</div>
						<div class="i">
							<label for="as_detail">상세 주소</label>
							<input type="text" name="as_detail" id="as_addr_detail" autocomplete="off" value="${detail_addr}">
						</div>
					</div>
					<div class="inner-container">
						<span>상세 정보</span>
						<div class="select-container">
		                    <label for="as_title">문제 종류</label> 
		                    <div class="select-inner-container">
		                    <select id="as_title" name="as_title">
		                        <option value="">-- 문제 유형 선택 --</option>
		                        <option value="합선 위험" ${asVO.as_title == '합선 위험' ? 'selected' : ''}>합선 위험</option>
		                        <option value="과열 현상" ${asVO.as_title == '과열 현상' ? 'selected' : ''}>과열 현상</option>
		                        <option value="스파크 발생" ${asVO.as_title == '스파크 발생' ? 'selected' : ''}>스파크 발생</option>
		                        <option value="노출된 전선" ${asVO.as_title == '노출된 전선' ? 'selected' : ''}>노출된 전선</option>
		                        <option value="차단기 작동 불능" ${asVO.as_title == '차단기 작동 불능' ? 'selected' : ''}>차단기 작동 불능</option>
		                        <c:set var="titleKnown" value="false" />
							    <c:if test="${asVO.as_title == '합선 위험' or asVO.as_title == '과열 현상' or asVO.as_title == '스파크 발생' or asVO.as_title == '노출된 전선' or asVO.as_title == '차단기 작동 불능'}">
							        <c:set var="titleKnown" value="true" />
							    </c:if>
							    <option value="기타" <c:if test="${!titleKnown}">selected</c:if>>기타 (직접 입력)</option>
							</select>
		                    <input type="text" id="as_title_custom" name="as_title_custom" placeholder="직접 입력" autocomplete="off" style="display:none;"
	    						value="<c:if test='${!titleKnown}'>${asVO.as_title}</c:if>"/>
	    					</div>
		                </div>
						<div class="i">
							<label for="as_content">상세 정보</label>  
							<input type="text" name="as_content" id="as_content" autocomplete="off" value="${asVO.as_content}">
						</div>
						<div class="i">
							<label for="reserve_date">예약 일자</label>  
							<input type="date" name="reserve_date" id="reserve_date" value="${fn:substring(asVO.as_date.toString(),0,10)}">
						</div>
						<div class="time-container">
							<label>예약 시간</label>
							<div>
								<div id="time-options-first" class="time-options" data-existing-time="${fn:substring(asVO.as_date.toString(), 11,16)}"></div>
								<div id="time-options-second" class="time-options"></div>
							</div>
						</div>
					</div>
					<div class="multiform-button-container">
						<c:if test="${asVO.as_status == '신고 접수'}">
							<button type="submit" form="updateForm">수정하기</button>
						</c:if>
						<c:if test="${asVO.as_status == '신고 접수'}">
							<button type="submit" form="cancleForm" onclick="return confirmCancel()">예약 취소</button>
						</c:if>
						<button id="goback" type="button" onclick="location.href='/as/detail'">돌아가기</button>
					</div>
				</form>
				<form id="cancleForm" action="/as/cancleCommon" method="post">
					<input type="hidden" name="as_cd" value="${asVO.as_cd}" />
				</form>
			</div>
		</main>
		</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>