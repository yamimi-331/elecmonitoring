<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">회원 정보 수정</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<form action="/profileEdit" method="post">
				<div class="i">
					<label for="user_id">아이디</label><br>
					<input type="text" name="id" id="id" value="${profileInfo.id}" readonly><br>
				</div>
				
				<div class="i">
					<label for="pw">기존 비밀번호 확인</label><br>
					<input type="password" name="prepw" id="prepw" autocomplete="new-password"><br>
					<button type="button">비밀번호 확인</button>
				</div>
				<div id="pwVerifyMsg" style="color:red; margin: 3px 0;">
				    <c:if test="${not empty errorMsg}">
				        ${errorMsg}
				    </c:if>
				</div>

				<div class="i">
					<label for="new_pw">새 비밀번호</label><br>
					<input type="password" name="pw" id="pw" autocomplete="new-password"><br>
					<small id="newPwMsg"></small>
				</div>
			  
				<div class="i">
					<label for="user_pw_ck">새 비밀번호 확인</label><br>
					<input type="password" name="user_pw_ck" id="user_pw_ck" autocomplete="new-password"><br>
					<small id="pwCheckMsg"></small>
				</div>
				
				<div class="i">
					<label for="nm">이름</label><br>
					<input type="text" name="nm" id="nm" value="${profileInfo.nm}"><br>
				</div>
				
				<div class="i">
					<c:choose>
				        <c:when test="${userType eq 'staff'}">
				            <label for="addr">담당 주소</label><br>
				            <input type="text" name="addr" id="addr" value="${profileInfo.addr}" readonly>
				        </c:when>
				        <c:otherwise>
				            <label for="addr">주소</label><br>
				            <input type="text" name="addr" id="addr" value="${profileInfo.addr}">
				        </c:otherwise>
				    </c:choose>
				</div>
				
				<c:if test="${userType eq 'common'}">
				    <div class="i">
				        <label for="mail">메일</label><br>
				        <input type="text" name="mail" id="mail" value="${profileInfo.mail}">
				    </div>
				</c:if>
				
				<button type="submit">제출하기</button>
			</form>
		</main>
	</div>
<script>
$(document).ready(function() {

    // 상태 변수
    let isPwVerified = false;
    let isNewPwValid = false;
    let isPwMatch = false;

    // 기존 비밀번호 입력 시 상태 초기화
    $('#prepw').on('input', function() {
        $('#pwVerifyMsg').text('').css('color', '');
        isPwVerified = false;
    });

    // 기존 비밀번호 확인 버튼 클릭
    $('button[type="button"]').on('click', function() {
        var inputPw = $('#prepw').val().trim();
        if (inputPw === '') {
            $('#pwVerifyMsg').text('비밀번호를 입력하세요.').css('color', 'red');
            isPwVerified = false;
            return;
        }

        $.ajax({
            url: '/profileEdit/checkPassword',
            type: 'POST',
            data: { inputPw: inputPw },
            success: function(result) {
                if (result) {
                    $('#pwVerifyMsg').text('비밀번호가 일치합니다.').css('color', 'green');
                    isPwVerified = true;
                } else {
                    $('#pwVerifyMsg').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
                    isPwVerified = false;
                }
            },
            error: function() {
                $('#pwVerifyMsg').text('서버 오류가 발생했습니다.').css('color', 'red');
                isPwVerified = false;
            }
        });
    });

    // 새 비밀번호 유효성 검사
    $('#pw').on('input', function() {
        var newPw = $(this).val();
        var msg = '';

        if (newPw.length < 8) {
            msg = '비밀번호는 8자 이상이어야 합니다.';
            isNewPwValid = false;
        } else if (!/[0-9]/.test(newPw) || !/[a-zA-Z]/.test(newPw)) {
            msg = '영문과 숫자를 포함해야 합니다.';
            isNewPwValid = false;
        } else {
            msg = '사용 가능한 비밀번호입니다.';
            isNewPwValid = true;
        }

        $('#newPwMsg').text(msg).css('color', isNewPwValid ? 'green' : 'red');
    });

    // 새 비밀번호 확인 일치 검사
    $('#user_pw_ck').on('input', function() {
        var newPw = $('#pw').val();
        var confirmPw = $(this).val();
        var msg = '';

        if (newPw === confirmPw && newPw !== '') {
            msg = '비밀번호가 일치합니다.';
            isPwMatch = true;
            $('#pwCheckMsg').css('color', 'green');
        } else {
            msg = '비밀번호가 일치하지 않습니다.';
            isPwMatch = false;
            $('#pwCheckMsg').css('color', 'red');
        }

        $('#pwCheckMsg').text(msg);
    });

    // 최종 제출 시 유효성 검사
    $('form').on('submit', function(e) {
        if (!isPwVerified) {
            alert('기존 비밀번호를 확인해주세요.');
            e.preventDefault();
            return false;
        }

        if (!isNewPwValid) {
            alert('새 비밀번호가 유효하지 않습니다.');
            e.preventDefault();
            return false;
        }

        if (!isPwMatch) {
            alert('새 비밀번호가 일치하지 않습니다.');
            e.preventDefault();
            return false;
        }
    });

});
</script>


	
</body>
</html>