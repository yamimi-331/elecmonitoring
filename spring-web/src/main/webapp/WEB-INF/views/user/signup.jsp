<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/signup.css?after" />
<script type="text/javascript">
//주소 API
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            const fullAddress = data.roadAddress || data.jibunAddress;
            document.getElementById("user_addr").value = fullAddress;
        }
    }).open();
}
</script>

</head>
<body>
 <div class="wrapper">
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
	<main class="main">
		<div class="container">
			<form action="signup" method="post" class="signup-form">
				<h2>회원가입</h2>
			    <label for="user_id">아이디</label>
			    <input type="text" id="user_id" name="user_id" required maxlength="50" autocomplete="off" />
			    <button type="button" id="checkIdBtn">중복검사</button>
			    <div id="idMsg" class="msg"></div>
			
			    <label for="user_pw">비밀번호</label>
			    <input type="password" id="user_pw" name="user_pw" required maxlength="50" />
			    
			    <label for="user_pw_confirm">비밀번호 확인</label>
			    <input type="password" id="user_pw_confirm" name="user_pw_confirm" required maxlength="50" />
			    <div id="pwMsg" class="msg"></div>
			    <label for="user_nm">이름</label>
			    <input type="text" id="user_nm" name="user_nm" required maxlength="100" autocomplete="off"/>
			    
			    <label for="user_addr">주소</label>
			    <input type="text" id="user_addr" name="user_addr" maxlength="200" readonly/>
				<button type="button" onclick="searchAddress()">주소 검색</button>
							    
			    <label for="user_mail">이메일</label>
			    <input type="email" id="user_mail" name="user_mail" required maxlength="100"  autocomplete="off"/>
			    
			    <button type="submit" id="submitBtn" disabled>회원가입</button>
				<button id="goback" type="button" onclick="location.href='/'">돌아가기</button>
			</form>
		</div>
	</main>
</div>	
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
<script>
$(document).ready(function() {

    // 중복검사 결과 상태 저장
    let isIdAvailable = false;
    let isPwMatch = false;

    function checkFormValid() {
        const pw = $('#user_pw').val();
        const pwConfirm = $('#user_pw_confirm').val();
        const name = $('#user_nm').val().trim();
        const mail = $('#user_mail').val().trim();
        const mailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        const isNameValid = name.length > 0;
        const isMailValid = mailPattern.test(mail);

        // 여기서 isPwMatch 사용!
        if (isIdAvailable && isPwMatch && isNameValid && isMailValid) {
            $('#submitBtn').prop('disabled', false);
        } else {
            $('#submitBtn').prop('disabled', true);
        }
    }

    // 아이디 중복검사 버튼 클릭
    $('#checkIdBtn').on('click', function() {
        var userId = $('#user_id').val().trim();

        if (userId.length === 0) {
            $('#idMsg').text('아이디를 입력해주세요.').removeClass('success').addClass('error');
            isIdAvailable = false;
            checkFormValid();
            return;
        }

        $.ajax({
            url: '/signup/checkId',
            type: 'GET',
            data: { user_id: userId },
            success: function(response) {
                if (response === '사용 가능') {
                    $('#idMsg').text('사용 가능한 아이디입니다.').removeClass('error').addClass('success');
                    isIdAvailable = true;
                } else {
                    $('#idMsg').text(response).removeClass('success').addClass('error');
                    isIdAvailable = false;
                }
                checkFormValid();
            },
            error: function() {
                $('#idMsg').text('서버 오류가 발생했습니다.').removeClass('success').addClass('error');
                isIdAvailable = false;
                checkFormValid();
            }
        });
    });

    // 비밀번호 입력 변경
   	//  비밀번호, 비밀번호 확인 입력 시
    $('#user_pw, #user_pw_confirm').on('input', function() {
        const pw = $('#user_pw').val();
        const pwConfirm = $('#user_pw_confirm').val();

        if (pw && pwConfirm) {
            if (pw === pwConfirm) {
                $('#pwMsg').text('비밀번호가 일치합니다.').removeClass('error').addClass('success');
                isPwMatch = true;
            } else {
                $('#pwMsg').text('비밀번호가 일치하지 않습니다.').removeClass('success').addClass('error');
                isPwMatch = false;
            }
        } else {
            $('#pwMsg').text('');
            isPwMatch = false;
        }

        checkFormValid();
    });

    // 이름, 이메일 입력 변경
    $('#user_nm, #user_mail').on('input', function() {
        checkFormValid();
    });

    // submit 최종 유효성
    $('form').on('submit', function(e) {
        if ($('#submitBtn').prop('disabled')) {
            e.preventDefault();
            return;
        }
    });

});

</script>

</body>
</html>
