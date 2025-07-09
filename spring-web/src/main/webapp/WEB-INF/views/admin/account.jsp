<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<style>
.modal {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex; justify-content: center; align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  width: 400px;
  position: relative;
}

.modal-close-btn {
  position: absolute;
  top: 10px; right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.modal-actions button {
  padding: 8px 16px;
  cursor: pointer;
}

</style>
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
			<table id="account-table">
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
			<table id="rollback-table">
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
	          <input type="text" name="staff_nm" id="modal-staff-nm" required />
	        </div>
	
	        <div class="form-group">
	          <label for="modal-staff-role">직급</label>
	          <select name="staff_role" id="modal-staff-role">
	            <option value="ADMIN">관리자</option>
	            <option value="MANAGER">매니저</option>
	            <option value="STAFF">직원</option>
	          </select>
	        </div>
	
	        <div class="form-group">
	          <label for="modal-staff-addr">주소</label>
	          <input type="text" name="staff_addr" id="modal-staff-addr" />
	        </div>
	
	        <div class="modal-actions">
	          <button type="button" onclick="closeModal()">취소</button>
	          <button type="submit">저장</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>

	
	<script>
	// 직원 정보수정 테이블의 조회 버튼 클릭
	function searchStaff(){
		const staffId = document.getElementById('searchStaffById').value;
		
		jQuery.ajax({
			url: "/admin/search-users",
			type: "GET",
			data: { staffId },
			dataType: "json", // JSON으로 받기
			success: function(staffs) {
				const tbody = jQuery("#account-table tbody").empty();
				// 테이블 초기화
				const accountTbody = jQuery("#account-table tbody").empty();
				const noDataRow = '<tr><td colspan="6">조회된 데이터가 없습니다.</td></tr>';
					
				if (staffs.length === 0) {
					// No users found, display a message
					const noStaffsRow = '<tr><td colspan="6">조회된 데이터가 없습니다.</td></tr>';
					tbody.append(noStaffsRow);
				} else {
					staffs.forEach(staff => {
						console.log(staff);
						 // 각 항목을 안전하게 문자열로 변환
						  const rowHtml =
							  '<tr>' +
							  '<td>' + staff.staff_cd + '</td>' +
							    '<td>' + staff.staff_id + '</td>' +
							    '<td>' + staff.staff_nm + '</td>' +
							    '<td>' + staff.staff_addr.split(' ')[0] + '</td>' +
							    '<td>' + staff.staff_role + '</td>' +
							    '<td><button onclick="accountDetail(\'' + staff.staff_id + '\')">상세 보기</button></td>' +
							  '</tr>';
						    tbody.append(rowHtml);
					});
				}
			},
		    error: function(xhr, status, error) {
		        alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
		        console.error("서버 오류 내용:", xhr.responseText);
		    }
		});
		
	}
	// 계정 복구 테이블의 조회 버튼 클릭
	function searchDeletedAccount() {
		const userType = document.getElementById('usertype').value;
		const userId = document.getElementById('searchDeletedUserById').value;

		jQuery.ajax({
			url: "/admin/search-deleted-users",
			type: "GET",
			data: { userType: userType, id: userId },
			dataType: "json",
			success: function(users) {
				const tbody = jQuery("#rollback-table tbody").empty();

				if (users.length === 0) {
					const noUsersRow = '<tr><td colspan="4">조회된 데이터가 없습니다.</td></tr>';
					tbody.append(noUsersRow);
				} else {
					users.forEach(user => {
						let rowHtml = '';
						if (userType === 'staff') {
							// staff VO에 맞게 출력
							rowHtml =
								'<tr>' +
									'<td>' + user.staff_cd + '</td>' +
									'<td>' + user.staff_id + '</td>' +
									'<td>' + user.staff_nm + '</td>' +
									'<td><button onclick="restoreAccount(\'' + user.staff_id + '\' , \'' + userType + '\')">복구</button></td>' +
								'</tr>';
						} else if (userType === 'common') {
							// 일반 사용자 VO에 맞게 출력
							rowHtml =
								'<tr>' +
									'<td>' + user.user_cd + '</td>' +
									'<td>' + user.user_id + '</td>' +
									'<td>' + user.user_nm + '</td>' +
									'<td><button onclick="restoreAccount(\'' + user.user_id + '\', \'' + userType + '\')">복구</button></td>' +
								'</tr>';
						}
						tbody.append(rowHtml);
					});
				}
			},
			error: function(xhr, status, error) {
				alert("서버 오류 발생: " + xhr.status + " " + xhr.statusText);
				console.error("서버 오류 내용:", xhr.responseText);
			}
		});
	}
	// 계정 복구 버튼
	function restoreAccount(id, userType) {
		if (!confirm("해당 계정을 복구하시겠습니까?")) {
			return; // 취소 시 종료
		}

		jQuery.ajax({
			url: "/admin/restore-account",
			type: "POST",
			data: {
				userType: userType,
				id: id
			},
			success: function(result) {
				alert("계정이 성공적으로 복구되었습니다.");
				searchDeletedAccount(); // 리스트 다시 조회
			},
			error: function(xhr, status, error) {
				alert("복구 실패: " + xhr.status + " " + xhr.statusText);
				console.error("서버 오류 내용:", xhr.responseText);
			}
		});
	}
	// 모달로 정보보고 수정
	$("#staff-detail-form").on("submit", function(e) {
		e.preventDefault(); // 기본 submit 막음
		
		const staffData = {
		  staff_cd: $("#modal-staff-cd").val(),
		  staff_id: $("#modal-staff-id").val(),
		  staff_nm: $("#modal-staff-nm").val(),
		  staff_role: $("#modal-staff-role").val(),
		  staff_addr: $("#modal-staff-addr").val(),
		};
		
		$.ajax({
		  url: "/admin/update-staff",
		  type: "POST",
		  data: staffData,
		  success: function() {
		    alert("수정 완료!");
		    closeModal();
		    searchStaff(); // 테이블 갱신
		  },
		  error: function() {
		    alert("오류 발생");
		  }
		});
	});
		
	// 모달 닫기
	function closeModal() {
	  $("#staff-modal").hide();
	}
	function accountDetail(staffId) {
		  $.ajax({
		    url: "/admin/get-staff-detail",
		    type: "GET",
		    data: { staffId: staffId },
		    dataType: "json",
		    success: function(staff) {
		      // 모달에 값 세팅
		      $("#modal-staff-cd").val(staff.staff_cd);
		      $("#modal-staff-id").val(staff.staff_id);
		      $("#modal-staff-nm").val(staff.staff_nm);
		      $("#modal-staff-role").val(staff.staff_role);
		      $("#modal-staff-addr").val(staff.staff_addr);

		      // 모달 열기
		      $("#staff-modal").show();
		    },
		    error: function(xhr) {
		      alert("직원 상세 정보 가져오기 실패");
		      console.error(xhr.responseText);
		    }
		  });
		}

	</script>
</body>
</html>