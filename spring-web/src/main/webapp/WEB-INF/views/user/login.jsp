<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
					<!-- 소셜로그인(구글) -->       
					<div class="form-group-center">
		            	<a href="/login/googleLogin"> 
		            		<img class="login-button" src="../../resources/img/google_signin.png" alt="Google 로그인">
		            	</a>
		            </div>
		            
		            <!-- 소셜로그인(네이버) --> 
		  			<div class="form-group-center">
		            	<a href="/login/naverLogin"> 
		            		<img class="login-button" src="../../resources/img/naver_login.png" alt="네이버 로그인">
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
					<form action="/login/guset" method="post">
						<div class="input-box">
							<input type="text" name="guset_nm" placeholder="이름" autocomplete="off">
							<input type="text" name="guset_mail" placeholder="이메일" autocomplete="off">
						</div>
						<button type="submit">비회원 로그인</button>
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
	</script>

</body>
</html>