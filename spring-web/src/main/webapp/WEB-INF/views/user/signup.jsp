<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
    .msg { font-size: 0.9em; margin-top: 4px; }
    .msg.success { color: green; }
    .msg.error { color: red; }
</style>
</head>
<body>
 <div class="wrapper">
	<h2 class="header-title">회원 가입</h2>
   <%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
	<main class="main">
	
	
<form action="signup" method="post">
    <label for="user_id">아이디:</label><br />
    <input type="text" id="user_id" name="user_id" required maxlength="50" autocomplete="off" />
    <button type="button" id="checkIdBtn">중복검사</button>
    <div id="idMsg" class="msg"></div>
    <br />

    <label for="user_pw">비밀번호:</label><br />
    <input type="password" id="user_pw" name="user_pw" required maxlength="50" /><br /><br />
    
    <label for="user_pw_confirm">비밀번호 확인:</label><br />
    <input type="password" id="user_pw_confirm" name="user_pw_confirm" required maxlength="50" /><br /><br />
    
    <label for="user_nm">이름:</label><br />
    <input type="text" id="user_nm" name="user_nm" required maxlength="100" /><br /><br />
    
    <label for="user_addr">주소:</label><br />
    <input type="text" id="user_addr" name="user_addr" maxlength="200" /><br /><br />
    
    <label for="user_mail">이메일:</label><br />
    <input type="email" id="user_mail" name="user_mail" required maxlength="100" /><br /><br />
    
    <button type="submit" id="submitBtn" disabled>회원가입</button>
</form>
	
	
	
	</main>
</div>	
<script>
$(document).ready(function() {
    // 아이디 중복검사
	$('#checkIdBtn').on('click', function() {
	    var userId = $('#user_id').val().trim();
	
	    if (userId.length === 0) {
	        $('#idMsg').text('아이디를 입력해주세요.').removeClass('success').addClass('error');
	        $('#submitBtn').prop('disabled', true);
	        return;
	    }
	
	    $.ajax({
	        url: '/signup/checkId',  // 컨트롤러 경로 맞춰서 변경
	        type: 'GET',
	        data: { user_id: userId },
	        success: function(response) {
	            if (response === '사용 가능') {
	                $('#idMsg').text('사용 가능한 아이디입니다.').removeClass('error').addClass('success');
	                $('#submitBtn').prop('disabled', false);
	            } else {
	                $('#idMsg').text(response).removeClass('success').addClass('error');
	                $('#submitBtn').prop('disabled', true);
	            }
	        },
	        error: function() {
	            $('#idMsg').text('서버 오류가 발생했습니다.').removeClass('success').addClass('error');
	            $('#submitBtn').prop('disabled', true);
	        }
	    });
	});


    // 폼 유효성 체크 및 제출 버튼 활성화
    function checkFormValid() {
        var pw = $('#user_pw').val();
        var pwConfirm = $('#user_pw_confirm').val();
        var userIdMsg = $('#idMsg').hasClass('success');

        if (pw && pwConfirm && pw === pwConfirm && userIdMsg) {
            $('#submitBtn').prop('disabled', false);
        } else {
            $('#submitBtn').prop('disabled', true);
        }
    }

    $('#user_pw, #user_pw_confirm').on('input', checkFormValid);

    // 폼 제출 이벤트
    $('#registerForm').on('submit', function(e) {
        e.preventDefault();

        var pw = $('#user_pw').val();
        var pwConfirm = $('#user_pw_confirm').val();

        if (pw !== pwConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        $.ajax({
            url: '/signup',
            type: 'POST',
            data: $(this).serialize(),
            success: function(response) {
                if (response === 'success') {
                    alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                    location.href = '/user/login';
                } else {
                    alert('회원가입에 실패했습니다. 다시 시도해주세요.');
                }
            },
            error: function() {
                alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
            }
        });
    });
});

</script>
</body>
</html>
