<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/login.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">로그인</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<div class="container">
			<div class="inner-container">
				<!-- 로그인 유형 탭 -->
				<ul id="login-tabs">
					<li><a href="#" data-tab="user" class="active">일반 로그인</a></li>
					<li><a href="#" data-tab="staff">직원 로그인</a></li>
					<li><a href="#" data-tab="guest">비회원 로그인</a></li>
				</ul>
			</div>
			<div class="login-form-wrap">
				<!-- 일반 로그인 폼 -->
				<div id="user" class="login-form active">
					<form action="/login/user" method="post" onsubmit="return validateLoginForm('user')">
						<div class="input-box">
							<input type="text" name="user_id" placeholder="아이디" autocomplete="off">
							<input type="password" name="user_pw" placeholder="비밀번호" autocomplete="off">
						</div>
						<button type="submit">로그인</button>
					</form>
					
					<a href='/signup'>회원가입</a>
					
					<div style="display: flex; align-items: center; text-align: center;margin-top:20px;">
					    <hr style="flex: 1; border: none; height: 1px; background-color: #ccc;">
					    <span style="padding: 0 10px; color: #888;">SNS 로그인</span>
					    <hr style="flex: 1; border: none; height: 1px; background-color: #ccc;">
					</div>
										
					<div class="form-group-center">
						<!-- 소셜로그인(구글) -->       
		            	<a href="/login/googleLogin"> 
		            		<img class="login-button" src="../../resources/img/googleIcon.png" alt="Google 로그인">
						</a>
			            <!-- 소셜로그인(네이버) --> 
						<a href="/login/naverLogin"> 
		            		<img class="login-button" src="../../resources/img/naverIcon.png" alt="네이버 로그인">
		            	</a>
			            <!-- 소셜로그인(카카오) --> 
		            	<a href="/login/kakaoLogin"> 
							<img class="login-button" src="../../resources/img/kakaotalkIcon.png" alt="카카오 로그인">
						</a>
		            </div>
				</div>
			
				<!-- 직원 로그인 폼 -->
				<div id="staff" class="login-form">
					<form action="/login/staff" method="post" onsubmit="return validateLoginForm('staff')">
						<div class="input-box">
							<input type="text" name="staff_id" placeholder="직원 ID" autocomplete="off">
							<input type="password" name="staff_pw" placeholder="비밀번호" autocomplete="off">
						</div>
						<button type="submit">로그인</button>
					</form>
				</div>
			  
				<!-- 비회원 로그인 폼 -->
			  	<div id="guest" class="login-form">
					<form action="/login/guest" method="post">
						<div class="input-box">
							<input type="text" name="guest_nm" placeholder="이름" autocomplete="off">
							<input type="text" name="guest_mail" placeholder="이메일" autocomplete="off">
							<input type="text" id="guest_code" name="guest_code" placeholder="인증코드" autocomplete="off" required>
							<small id="codeMsg"></small><br>
						</div>
						<div class="input-box">
							<button type="button" id="sendCodeBtn">코드 발송</button>
   	 						<button type="button" id="verifyCodeBtn">인증 확인</button>
							<button type="submit" id="guestLoginBtn" disabled>로그인</button>
						</div>
					</form>
				</div>
			</div>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp" %>

	<script>
		const tabs = document.querySelectorAll('#login-tabs a');
		const forms = document.querySelectorAll('.login-form');
	
		tabs.forEach(tab => {
			tab.addEventListener('click', function(e) {
				e.preventDefault();
				const targetId = this.dataset.tab;

				// 탭 버튼 상태 변경
				tabs.forEach(t => t.classList.remove('active'));
				this.classList.add('active');

				// 폼 보여주기/숨기기
				forms.forEach(form => {
				form.classList.toggle('active', form.id === targetId);
				});
			});
		});
		
		// 게스트 인증 부분 분리해야해서.. 다시 보기(staff 랑 user만 반영)
		function validateLoginForm(formType) {
			const prefix = formType === "staff" ? "#staff" : "#user";

			const id = document.querySelector(prefix + ' input[name="' + formType + '_id"]')?.value.trim();
			const pw = document.querySelector(prefix + ' input[name="' + formType + '_pw"]')?.value.trim();
			if (!id) {
				alert('아이디를 입력하세요.');
				return false;
			}
			if (formType !== "guest" && !pw) {
				alert('비밀번호를 입력하세요.');
				return false;
			}
			return true;
		}
		
		const sendCodeBtn = document.getElementById('sendCodeBtn');
		const codeInput = document.getElementById('guest_code');
		const codeMsg = document.getElementById('codeMsg');
		const loginBtn = document.getElementById('guestLoginBtn');
		const guestMailInput = document.querySelector('input[name="guest_mail"]');

		sendCodeBtn.addEventListener('click', function() {
		    const email = guestMailInput.value.trim();
		    if(!email) {
		        alert('이메일을 입력하세요.');
		        return;
		    }

		    fetch('/login/guest/send-code', {
		        method: 'POST',
		        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		        body: 'guest_mail=' + encodeURIComponent(email)
		    })
		    .then(response => {
		        if(!response.ok) throw new Error('인증코드 발송 실패');
		        return response.text();
		    })
		    .then(text => {
		        alert('인증코드가 발송되었습니다.');
		    })
		    .catch(error => {
		        alert('인증코드 발송에 실패했습니다.');
		    });
		});

		const verifyCodeBtn = document.getElementById('verifyCodeBtn');
		verifyCodeBtn.addEventListener('click', function() {
		    const code = codeInput.value.trim();
		    if(code.length !== 6) {
		        alert('인증코드를 6자리 입력하세요.');
		        return;
		    }
		    fetch('/login/guest/verify-code', {
		        method: 'POST',
		        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		        body: 'guest_code=' + encodeURIComponent(code)
		    })
		    .then(res => res.text())
		    .then(text => {
		        codeMsg.textContent = text;
		        if(text === "인증코드 확인 완료") {
		            codeMsg.style.color = 'green';
		            loginBtn.disabled = false;
		        } else {
		            codeMsg.style.color = 'red';
		            loginBtn.disabled = true;
		        }
		    })
		    .catch(() => {
		        codeMsg.textContent = '서버 오류';
		        codeMsg.style.color = 'red';
		        loginBtn.disabled = true;
		    });
		});


	</script>

</body>
</html>