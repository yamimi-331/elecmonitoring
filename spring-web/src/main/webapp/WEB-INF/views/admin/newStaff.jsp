<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
	/* 폼 숨기기/보이기 */
	.newStaff-form { display: none; }
	.newStaff-form.active { display: block; }
	
	/* 탭 스타일 */
	#newStaff-tabs {
		display: flex;
		list-style: none;
		padding: 0;
		margin-bottom: 20px;
	}

	#newStaff-tabs li a {
		padding: 10px 20px;
		text-decoration: none;
		background-color: #eee;
		margin-right: 5px;
		border-radius: 5px 5px 0 0;
		color: #333;
		border: 1px solid #ccc;
		border-bottom: none;
	}

	#newStaff-tabs li a.active {
		background-color: white;
		font-weight: bold;
		border-bottom: 1px solid white;
	}
</style>
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">전기재해 모니터링 시스템</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<!-- 로그인 유형 탭 -->
			<ul id="newStaff-tabs">
				<li><a href="#" data-tab="staff" class="active">직원 계정 생성</a></li>
				<li><a href="#" data-tab="admin">관리자 계정 생성</a></li>
			</ul>
		
			<!-- 직원 계정 생성 폼 -->
			<div id="staff" class="newStaff-form active">
				<form action="/newStaff/staff" method="post">
					<div class="i">
						<label for="staff_id_staff">아이디</label><br>
						<input type="text" name="staff_id" id="staff_id_staff" autocomplete="off"><br>
						<button type="button" id="staff_id_ck_staff">아이디 중복 확인</button>
						<small id="idCheckMsgStaff"></small>
					</div>
	
					<div class="i">
						<label for="staff_pw">새 비밀번호</label><br>
						<input type="password" name="staff_pw" id="staff_pw_staff" autocomplete="new-password"><br>
					</div>
				  
					<div class="i">
						<label for="staff_pw_ck">새 비밀번호 확인</label><br>
						<input type="password" name="staff_pw_ck" id="staff_pw_ck_staff" autocomplete="new-password"><br>
						<small id="pwCheckMsgStaff"></small>
					</div>
					
					<div class="i">
						<label for="staff_nm">이름</label><br>
						<input type="text" name="staff_nm" id="staff_nm_staff" autocomplete="off"><br>
					</div>
					
					<div class="i">
						<label for="staff_addr">담당 주소</label><br>
						<input type="text" name="staff_addr" id="staff_addr_staff" autocomplete="off"><br>
					</div>
					
					<input type="hidden" name="staff_role" id="staff_role_staff" value="staff"><br>
					<button type="submit" id="submitBtnStaff">직원 계정 생성</button>
				</form>
			</div>
		
			<!-- 관리자 계정 생성 폼 -->
			<div id="admin" class="newStaff-form">
				<form action="/newStaff/admin" method="post">
					<div class="i">
						<label for="staff_id_admin">아이디</label><br>
						<input type="text" name="staff_id" id="staff_id_admin" autocomplete="off"><br>
						<button type="button" id="staff_id_ck_admin">아이디 중복 확인</button>
						<small id="idCheckMsgAdmin"></small>
					</div>
	
					<div class="i">
						<label for="staff_pw">새 비밀번호</label><br>
						<input type="password" name="staff_pw" id="staff_pw_admin" autocomplete="new-password"><br>
					</div>
				  
					<div class="i">
						<label for="staff_pw_ck">새 비밀번호 확인</label><br>
						<input type="password" name="staff_pw_ck" id="staff_pw_ck_admin" autocomplete="new-password"><br>
						<small id="pwCheckMsgAdmin"></small>
					</div>
					
					<div class="i">
						<label for="staff_nm">이름</label><br>
						<input type="text" name="staff_nm" id="staff_nm_admin" autocomplete="off"><br>
					</div>
					
					<input type="hidden" name="staff_role" id="staff_role_admin" value="admin"><br>
					<button type="submit" id="submitBtnAdmin">관리자 계정 생성</button>
				</form>
			</div>
		</main>
	</div>

	<c:if test="${not empty message}">
   	 	<script>
        	alert('${message}');
    	</script>
	</c:if>

	<script>
		//탭 전환
		const tabs = document.querySelectorAll('#newStaff-tabs a');
		const forms = document.querySelectorAll('.newStaff-form');
	
		tabs.forEach(tab => {
			tab.addEventListener('click', function(e) {
				e.preventDefault();
				const targetId = this.dataset.tab;

				// 탭 버튼 상태 변경
				tabs.forEach(t => t.classList.remove('active'));
				this.classList.add('active');

				// 폼 보여주기/숨기기
				forms.forEach(form => {
					if(form.id === targetId){
						form.classList.add('active');
					}else{
						form.classList.remove('active');
						form.querySelectorAll('input[type="text"], input[type="password"]').forEach(input => {
							input.value = '';
						});
						form.querySelectorAll('small').forEach(small => {
							small.textContent = '';
						});
						form.querySelector('button[type="submit"]').disabled = false;
					}
				});
			});
		});
	</script>
</body>
</html>