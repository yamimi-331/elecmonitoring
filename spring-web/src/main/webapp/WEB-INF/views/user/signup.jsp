<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
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
<c:if test="${not empty message}">
    <script>
        alert('${message}');
    </script>
</c:if>
<script>
$(document).ready(function() {
    // 아이디 중복검사 버튼 클릭
    $('#checkIdBtn').on('click', function() {
        var userId = $('#user_id').val().trim();

        if (userId.length === 0) {
            $('#idMsg').text('아이디를 입력해주세요.').removeClass('success').addClass('error');
            $('#submitBtn').prop('disabled', true);
            return;
        }

        $.ajax({
            url: '/signup/checkId',
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

    // 비밀번호 확인 체크 (폼 제출 전 유효성)
    $('#user_pw, #user_pw_confirm').on('input', function() {
        var pw = $('#user_pw').val();
        var pwConfirm = $('#user_pw_confirm').val();

        if (pw && pwConfirm && pw === pwConfirm) {
            if ($('#idMsg').hasClass('success')) {
                $('#submitBtn').prop('disabled', false);
            }
        } else {
            $('#submitBtn').prop('disabled', true);
        }
    });

    // 이제 AJAX로 폼 submit 안함! 일반 제출
    $('form').on('submit', function(e) {
        const pw = $('#user_pw').val();
        const pwConfirm = $('#user_pw_confirm').val();
        const name = $('#user_nm').val().trim();
        const mail = $('#user_mail').val().trim();
        const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (name === '') {
            alert('이름을 입력해주세요.');
            $('#user_nm').focus();
            e.preventDefault();
            return;
        }

        if (pw !== pwConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            e.preventDefault();
            return;
        }

        if (mail && !pattern.test(mail)) {
            alert('이메일 형식을 확인해주세요.');
            $('#user_mail').focus();
            e.preventDefault();
            return;
        }
    });
});
</script>

</body>
</html>
