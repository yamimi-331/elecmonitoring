<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">관리자 페이지</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp"%>
		<main class="main">
			먼저 검색영역 
			직원을 이름 아이디로 검색이가능함
			
			조회시 아래 테이블에 직원 리스트가 나옴
			
			해당 영역 클릭시(행)
			.selected 적용
			value도 변수에 저장
			가장 우측의 지역 변경 클릭시 모달팝업 등장
			
			모달팝업의 내용은 현재 선택한 직원/관리자의 정보가나오고 
			아래에 지역 select 영역이 있음 
			변경후 저장가능
			
			
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>