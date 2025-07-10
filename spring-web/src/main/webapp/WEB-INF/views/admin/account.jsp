<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="../../resources/js/account.js?after"></script>
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
			  </select>
			</div>

	
	        <div class="modal-actions">
	          <button type="button" onclick="closeModal()">취소</button>
	          <button type="submit">저장</button>
	        </div>
	      </form>
	    </div>
	  </div>
	  <!-- 모달 2 end -->
	</div>

	
</body>
</html>