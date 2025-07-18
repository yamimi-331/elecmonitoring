<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- --------------------------- 공통 헤더 영역 Start --------------------------- -->
<link rel="stylesheet" href="../../resources/css/header.css?after" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
//로그아웃 로직
function logoutConfirm() {
    if (confirm('정말 로그아웃 하시겠습니까?')) {
        window.location.href = '/logout';
    }
}
// 로그인 경고 얼러트
function requireLogin() {
  alert('로그인이 필요합니다.');
  location.href = '/login';
  return false;
}

// 로그인 세션 시간(30분)
const sessionTimeoutInSec = <c:out value="${session.maxInactiveInterval}" default="1800" />;
let sessionStartTime = Date.now();

// 세션 남은 시간 format
function formatTime(sec) {
	const minutes = String(Math.floor(sec / 60)).padStart(2, '0');
	const seconds = String(sec % 60).padStart(2, '0');
	
	return '(' + minutes + ':' + seconds + ')';
}


function updateSessionTimer() {
	const now = Date.now();
	const elapsedSec = Math.floor((now - sessionStartTime) / 1000);
	const remainingSec = sessionTimeoutInSec - elapsedSec;

	const timerEl = document.getElementById('session-timer');
	const extendBtn = document.getElementById('extend-session-btn');
	
	if (remainingSec > 0) {
		timerEl.textContent = formatTime(remainingSec);
	} else {
		extendBtn.style.display = 'none';
		clearInterval(timerInterval);
	    location.href = '/logout/auto';
	}
}

//'계속하기' 버튼 클릭 시 세션 연장 요청
document.addEventListener('DOMContentLoaded', function () {
	const extendBtn = document.getElementById('extend-session-btn');
	if (extendBtn) {
		extendBtn.addEventListener('click', () => {
			fetch('/login/extend-session', { method: 'POST' })
				.then(response => {
					if (response.ok) {
						alert('세션이 연장되었습니다.');
						sessionStartTime = Date.now();  // 타이머 초기화
					} else {
						alert('세션 연장에 실패했습니다.');
						location.href = '/login';
					}
				})
				.catch(() => {
					alert('서버 응답이 없습니다.');
					location.href = '/login';
				});
		});
	}
});
</script>
<header class="main-header">
	<c:if test="${not empty sessionScope.currentUserInfo}">
		<div class="session-timer-container">
			<span id="session-timer"></span>
			<button id="extend-session-btn">연장하기</button>
		</div>
		<script>
			const timerInterval = setInterval(updateSessionTimer, 1000);
			updateSessionTimer();
		</script>
	</c:if>
	<h2 class="header-title"><a href="/">전기재해 모니터링 및 노후시설 A/S 신고 관리 시스템</a></h2>
	<nav class="main-nav">
		<ul class="nav-list">
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-monitor">전기 재해 모니터링</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-as">노후시설 A/S 신고</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-notice">고객지원</a>
			</li>
		</ul>
		<c:choose>
	        <c:when test="${empty currentUserInfo}">
		        <button class="login-btn" onclick="location.href='/login'">로그인</button>
		    </c:when>
 			
 			<c:otherwise>
 				<c:choose>
		            <%-- 사용자 이름 가져오기 --%>
		            <c:when test="${userType == 'common'}">
				        <c:set var="userName" value="${currentUserInfo.user_nm}" />
				    </c:when>
				    <c:when test="${userType == 'staff' || userType == 'admin'}">
				        <c:set var="userName" value="${currentUserInfo.staff_nm}" />
				    </c:when>
				    <c:when test="${userType == 'guest'}">
				        <c:set var="userName" value="${currentUserInfo.guest_nm}" />
				    </c:when>
				    <c:otherwise>
				        <c:set var="userName" value="알 수 없는 사용자" />
				    </c:otherwise>
 				</c:choose>
	
	            <%-- 헤더에 원형 프로필 아이콘 --%>
	            <div id="profileArea">
	                <div id="profileIcon">
	                    <span>${fn:substring(userName, 0, 1)}</span>
	                </div>
	
	                <div id="profilePopup" class="hidden">
	                    <p><strong>${userName}님</strong></p>
	                    <ul>
	                        <li><a href="/profileEdit">회원정보 수정</a></li>
	                        <li><button onclick="logoutConfirm()">로그아웃</button></li>
	                    </ul>
	                </div>
	            </div>
	    	</c:otherwise>
    	</c:choose>
	</nav>

    <div class="common-mega-menu-dropdown">
    	<%-- 전기재해 모니터링 메뉴 --%>
        <div id="mega-monitor" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>전기 재해 모니터링</h3>
                <ul>
                    <li><a href="/dashboard">전기 재해 모니터링 대시보드</a></li>
                    <li><a href="/report">전기 재해 신고 목록</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>모니터링 대시보드 정보</h3>
                <p>전기 재해 정보를 바탕으로 미래 전기적 요인으로 인한 사건사고를 예측하고 예방하기위한 모니터링 대시보드 페이지 입니다.</p>
            </div>

            <div class="mega-menu-column">
                <h3>긴급 문의</h3>
                <p>긴급 전기 재해의 경우 고객센터로 직접 문의해주세요.</p>
                <p>📞 0000-1234</p>
            </div>
        </div>
        
        <%-- 고객 지원 메뉴 --%>
        <div id="mega-notice" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>고객 지원 안내</h3>
                <ul>
                    <li><a href="/notice">전체 공지사항</a></li>
                    <c:if test="${sessionScope.userType == 'admin'}">
						<li><a href="/admin/account">계정 관리 페이지</a></li>
                    </c:if>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>주요 공지사항</h3>
                <p>최신 공지사항을 확인하여 시스템 이용에 불편함이 없도록 해주세요. 중요한 변경사항이나 공지사항을 놓치지 않도록 주기적인 확인을 권장합니다.</p>
            </div>
            <div class="mega-menu-column">
                <h3 class="visually-hidden">바로가기</h3>
                <a href="#" class="menu-shortcut-btn">공지사항 전체보기</a>
                <p style="text-align: center; font-size: 3em; margin-top: 15px;">📢</p> 
            </div>
        </div>
        
		<%-- A/S 관련 메뉴 --%>
        <div id="mega-as" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>A/S 서비스 신청</h3>
                <ul>
                <%-- 로그인 안한 경우 --%>
			    <c:choose>
			      <c:when test="${empty sessionScope.userType}">
			        <li><a href="#" onclick="return requireLogin();">노후 시설 A/S 신고</a></li>
			        <li><a href="#" onclick="return requireLogin();">A/S 진행 현황</a></li>
			      </c:when>
			
			      <%-- 일반 사용자(userType == 'common') --%>
			      <c:when test="${sessionScope.userType == 'common'}">
			        <li><a href="/as/form">노후 시설 A/S 신고</a></li>
			        <li><a href="/as/detail">A/S 진행 현황</a></li>
			      </c:when>
			
			 	  <%-- 게스트 로그인 사용자(userType == 'guest') --%>
			      <c:when test="${sessionScope.userType == 'guest'}">
			        <li><a href="/as/form">노후 시설 A/S 신고</a></li>
			        <li><a href="/as/detail">A/S 진행 현황</a></li>
			      </c:when>
			      
			      <%-- 직원 (userType == 'staff') --%>
			      <c:when test="${sessionScope.userType == 'staff'}">
			        <li><a href="/as/order">A/S 진행 현황 관리</a></li>
			        <li><a href="/as/calendar">전체 AS 일정 확인 페이지</a></li>
			      </c:when>
				  
				  <%-- 관리자(userType == 'admin') --%>
			      <c:when test="${sessionScope.userType == 'admin'}">
			        <li><a href="/as/order">A/S 진행 현황 관리</a></li>
			        <li><a href="/as/calendar">전체 AS 일정 확인 페이지</a></li>
			      </c:when>
			      
			      <%-- 기타 예외 --%>
			      <c:otherwise>
			        <li><a href="#" onclick="return requireLogin();">노후 시설 A/S 신고</a></li>
			        <li><a href="#" onclick="return requireLogin();">A/S 진행 현황</a></li>
			      </c:otherwise>
			    </c:choose>
                </ul>
            </div>
            
            <div class="mega-menu-column">
                <h3>신청 가이드</h3>
                <p>간편하게 A/S를 신청하고 처리 현황을 확인하세요. 전문 기사가 신속하고 정확하게 처리해드립니다. 온라인으로 편리하게 접수 가능합니다.</p>
             </div>
             
             <div class="mega-menu-column">
                <h3>긴급 문의</h3>
                <p>긴급한 A/S는 고객센터로 직접 문의해주세요.</p>
                <p>📞 0000-1234</p>
            </div>
        </div>
		
    </div>
</header>

<div class="overlay"></div>

<script src="../../resources/js/header.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<!-- --------------------------- 공통 헤더 영역 End --------------------------- -->