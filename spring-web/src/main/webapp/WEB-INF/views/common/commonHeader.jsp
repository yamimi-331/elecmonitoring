<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- --------------------------- 공통 헤더 영역 Start --------------------------- -->
<link rel="stylesheet" href="../../resources/css/commonHeader.css?after" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
//로그아웃 로직
function logoutConfirm() {
    if (confirm('정말 로그아웃 하시겠습니까?')) {
        window.location.href = '/logout';
    }
}
</script>
<header class="main-header">
	<nav class="main-nav">
		<ul class="nav-list">
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-notice">공지사항</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-monitor">전기 재해 모니터링</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-as">노후시설 A/S 신고</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-page">페이지</a>
			</li>
			<li class="nav-item">
				<a href="#" class="nav-link" data-target="mega-etc">기타</a>
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
    	<%-- 공지사항 메뉴 --%>
        <div id="mega-monitor" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>전기 재해 모니터링</h3>
                <ul>
                    <li><a href="/dashboard">전기 재해 모니터링 대시보드</a></li>
                    <li><a href="#">시스템 업데이트</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>모니터링 대시보드 정보</h3>
                <p>전기 재해 정보를 바탕으로 미래 전기적 요인으로인한 사건사고를 예측하고 예방하기위한 데이터 모니터링 대시보드 페이지 입니다.</p>
            </div>
            <div class="mega-menu-column">
                <h3 class="visually-hidden">바로가기</h3>
                <a href="#" class="menu-shortcut-btn">오류 신고</a>
        	</div>
        </div>
        
        <%-- 전기재해 모니터링 메뉴 --%>
        <div id="mega-notice" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>공지사항 안내</h3>
                <ul>
                    <li><a href="/notice">전체 공지사항</a></li>
                    <li><a href="#">시스템 업데이트</a></li>
                    <li><a href="#">긴급 점검 안내</a></li>
                    <li><a href="#">이벤트 및 프로모션</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>주요 공지사항</h3>
                <p>최신 공지사항을 확인하여 시스템 이용에 불편함이 없도록 해주세요. 중요한 변경사항이나 공지사항을 놓치지 않도록 주기적인 확인을 권장합니다.</p>
            </div>
            <div class="mega-menu-column">
                <h3 class="visually-hidden">바로가기</h3>
                <a href="#" class="menu-shortcut-btn">공지사항 전체보기</a>
                <p style="text-align: center; font-size: 3em; margin-top: 15px;">📢</p> </div>
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
			        <li><a href="/admin/account">계정 관리 페이지</a></li>
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
		
		<%-- 페이지 메뉴 --%>
        <div id="mega-page" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>사이트 정보</h3>
                <ul>
                    <li><a href="/">메인 페이지</a></li>
                    <li><a href="/profileEdit">회원정보 수정</a></li>
                    <li><a href="#">자주 묻는 질문</a></li>
                    <li><a href="#">사이트맵</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>추가 정보</h3>
                <ul>
                    <li><a href="#">개인정보 처리방침</a></li>
                    <li><a href="#">이용약관</a></li>
                    <li><a href="#">오시는 길</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>유용한 링크</h3>
                <ul class="icon-list">
                    <li><a href="#"> <i class="fas fa-file-alt"></i> 자료실</a></li>
                    <li><a href="#"> <i class="fas fa-envelope"></i> 1:1 문의</a></li>
                    <li><a href="#"> <i class="fas fa-search"></i> 검색</a></li>
                </ul>
            </div>
        </div>
        
		<%-- 고객지원 메뉴 --%>
        <div id="mega-etc" class="mega-menu-content">
            <div class="mega-menu-column">
                <h3>고객지원</h3>
                <ul>
                    <li><a href="#">고객센터</a></li>
                    <li><a href="#">온라인 문의</a></li>
                    <li><a href="#">원격 지원 요청</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>회사 정보</h3>
                <ul>
                    <li><a href="#">회사 소개</a></li>
                    <li><a href="#">인재 채용</a></li>
                    <li><a href="#">제휴 문의</a></li>
                </ul>
            </div>
            <div class="mega-menu-column">
                <h3>관련 정책</h3>
                <p>문의 전에 관련 정책을 확인하시면 빠르게 궁금증을 해결할 수 있습니다. 각 서비스의 약관 및 규정을 숙지하시기 바랍니다.</p>
            </div>
        </div>
    </div>
</header>

<div class="overlay"></div>

<script src="../../resources/js/commonHeader.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<!-- --------------------------- 공통 헤더 영역 End --------------------------- -->