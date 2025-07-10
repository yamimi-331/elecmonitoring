<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="../../resources/js/account.js?after" defer></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<link rel="stylesheet" href="../../resources/css/account.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">관리자 페이지</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>
		<main class="main">
			<!-- 직원 계정 관리 -->
			<h3>직원 계정 관리</h3>
			<div>
				<input type="search" id="searchStaffById" placeholder="ID 입력" autocomplete="off" />
				<button onclick="searchStaff()">조회</button>
				<button onclick="createStaff()">계정 생성</button>
			</div>
			<table id="account-table" class="custom-table">
				<thead>
					<tr>
						<th style="width: 15%">직원번호</th>
						<th style="width: 20%">아이디</th>
						<th style="width: 20%">이름</th>
						<th style="width: 15%">직급</th>
						<th style="width: 20%">담당 주소</th>
						<th style="width: 10%">상세 정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="6">조회된 데이터가 없습니다.</td>
					</tr>
				</tbody>
			</table>

			<!-- 계정 복구 -->
			<h3>계정 복구</h3>
			<div class="search-area">
				<select id="usertype" name="usertype">
					<option value="staff">직원</option>
					<option value="common">일반 사용자</option>
				</select> 
				<input type="search" id="searchDeletedUserById" placeholder="ID 입력" autocomplete="off" />
				<button onclick="searchDeletedAccount()">조회</button>
			</div>
			<table id="rollback-table" class="custom-table">
				<thead>
					<tr>
						<th style="width: 25%">계정 번호</th>
						<th style="width: 25%">아이디</th>
						<th style="width: 25%">이름</th>
						<th style="width: 25%">상세 정보</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="4">조회된 데이터가 없습니다.</td>
					</tr>
				</tbody>
			</table>

		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
	<div id="modal-popup">
	  <!-- 모달 -->
	  <div id="staff-modal" class="modal" style="display: none;">
	    <div class="modal-content">
	      <h3>직원 상세 정보</h3>
	      <!-- 직원 상세 폼 -->
	      <form id="staff-detail-form" method="post" action="/admin/update-staff">
	        <input type="hidden" name="staff_cd" id="modal-staff-cd" />
	        <input type="hidden" name="staff_id" id="modal-staff-id" />
	
	        <div class="form-group">
	          <label for="modal-staff-nm">이름</label>
	          <input type="text" name="staff_nm" id="modal-staff-nm" required readonly/>
	        </div>
	
	        <div class="form-group">
	          <label for="modal-staff-role">직급</label>
	          <input name="staff_role" id="modal-staff-role" readonly/>
	        </div>
	
	        <div class="form-group">
			  <label for="modal-staff-addr">주소 (시/도)</label>
			  <select name="staff_addr" id="modal-staff-addr">
			    <option value="서울">서울특별시</option>
			    <option value="부산">부산광역시</option>
			    <option value="대구">대구광역시</option>
			    <option value="인천">인천광역시</option>
			    <option value="광주">광주광역시</option>
			    <option value="대전">대전광역시</option>
			    <option value="울산">울산광역시</option>
			    <option value="세종">세종특별자치시</option>
			    <option value="경기">경기도</option>
			    <option value="강원">강원도</option>
			    <option value="충북">충청북도</option>
			    <option value="충남">충청남도</option>
			    <option value="전북">전라북도</option>
			    <option value="전남">전라남도</option>
			    <option value="경북">경상북도</option>
			    <option value="경남">경상남도</option>
			    <option value="제주">제주특별자치도</option>
			    <option value="-">없음</option>
			  </select>
			</div>

	        <div class="modal-actions">
	          <button type="button" onclick="closeModal()">취소</button>
	          <button type="submit">저장</button>
	        </div>
	      </form>
	    </div>
	  </div>
	  <!-- 모달1 end -->
	  <!-- 모달2 -->
	  <div id="new-staff-modal" class="modal" style="display: none;">
	    <div class="modal-content">
	      	<!-- 로그인 유형 탭 -->
			<ul id="newStaff-tabs">
				<li><a href="#" data-tab="staff" class="active">직원 계정 생성</a></li>
				<li><a href="#" data-tab="admin">관리자 계정 생성</a></li>
			</ul>
		
			<!-- 직원 계정 생성 폼 -->
			<div id="staff" class="newStaff-form active">
				<form action="/admin/staff" method="post">
					<div class="form-group">
						<label for="staff_id_staff">아이디</label>
						<input type="text" name="staff_id" id="staff_id_staff" autocomplete="off">
						<button type="button" id="staff_id_ck_staff">아이디 중복 확인</button>
						<small id="idCheckMsg_staff"></small>
					</div>
	
					<div class="form-group">
						<label for="staff_pw">새 비밀번호</label>
						<input type="password" name="staff_pw" id="staff_pw_staff" autocomplete="new-password">
					</div>
				  
					<div class="form-group">
						<label for="staff_pw_ck">새 비밀번호 확인</label>
						<input type="password" name="staff_pw_ck" id="staff_pw_ck_staff" autocomplete="new-password">
						<small id="pwCheckMsg_staff"></small>
					</div>
					
					<div class="form-group">
						<label for="staff_nm_staff">이름</label>
						<input type="text" name="staff_nm" id="staff_nm_staff" autocomplete="off">
					</div>
					
					<div class="form-group">
						<label for="staff_addr_staff">담당 주소</label>
						  <select name="staff_addr" id="staff_addr_staff">
						    <option value="서울">서울특별시</option>
						    <option value="부산">부산광역시</option>
						    <option value="대구">대구광역시</option>
						    <option value="인천">인천광역시</option>
						    <option value="광주">광주광역시</option>
						    <option value="대전">대전광역시</option>
						    <option value="울산">울산광역시</option>
						    <option value="세종">세종특별자치시</option>
						    <option value="경기">경기도</option>
						    <option value="강원">강원도</option>
						    <option value="충북">충청북도</option>
						    <option value="충남">충청남도</option>
						    <option value="전북">전라북도</option>
						    <option value="전남">전라남도</option>
						    <option value="경북">경상북도</option>
						    <option value="경남">경상남도</option>
						    <option value="제주">제주특별자치도</option>
						    <option value="-">없음</option>
						  </select>
					</div>
					
					<input type="hidden" name="staff_role" id="staff_role_staff" value="staff">
					
			        <div class="modal-actions">
						<button type="submit" id="submitBtn_staff">직원 계정 생성</button>
						<button type="button" onclick="closeModal()">취소</button>
		    	    </div>
				</form>
			</div>
		
			<!-- 관리자 계정 생성 폼 -->
			<div id="admin" class="newStaff-form">
				<form action="/admin/admin" method="post">
					<div class="form-group">
						<label for="staff_id_admin">아이디</label>
						<input type="text" name="staff_id" id="staff_id_admin" autocomplete="off">
						<button type="button" id="staff_id_ck_admin">아이디 중복 확인</button>
						<small id="idCheckMsg_admin"></small>
					</div>
	
					<div class="form-group">
						<label for="staff_pw">새 비밀번호</label>
						<input type="password" name="staff_pw" id="staff_pw_admin" autocomplete="new-password">
					</div>
				  
					<div class="form-group">
						<label for="staff_pw_ck">새 비밀번호 확인</label>
						<input type="password" name="staff_pw_ck" id="staff_pw_ck_admin" autocomplete="new-password">
						<small id="pwCheckMsg_admin"></small>
					</div>
					
					<div class="form-group">
						<label for="staff_nm_admin">이름</label>
						<input type="text" name="staff_nm" id="staff_nm_admin" autocomplete="off">
					</div>
					
					<input type="hidden" name="staff_role" id="staff_role_admin" value="admin">
					
			        <div class="modal-actions">
						<button type="submit" id="submitBtn_admin">관리자 계정 생성</button>
						<button type="button" onclick="closeModal()">취소</button>
					</div>
				</form>
			</div>
	    </div>
	  </div>
	  <!-- 모달 2 end -->
	</div>
</body>
</html>