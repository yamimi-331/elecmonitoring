<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 리스트 영역 전체를 감싸는 div에 id 부여 -->
<div id="aslistArea" class="inner-container">
    <table class="normal-table">
    	<colgroup>
			<col style="width: 15%">
			<col style="width: 40%">
			<col style="width: 15%">
			<col style="width: 15%">
			<col style="width: 15%">
		</colgroup>
        <thead>
            <tr>
                <th>번호</th>
                <th>신고 내역</th>
                <th>예약 일시</th>
                <th>진행 상태</th>
                <th>상세 정보</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty userList}">
                    <tr>
                        <td colspan="5">예약 내역이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${userList}">
                        <tr>
                            <td>${item.as.as_cd}</td>
                            <td>${item.as.as_title}</td>
                            <td>${item.as_date_str} ${item.as_time_str}</td>
                            <td>${item.as.as_status}</td>
                            <td>
                                <form action="/as/info" method="get" style="display:inline;">
                                    <input type="hidden" name="as_cd" value="${item.as.as_cd}" />
                                    <button type="submit">상세보기</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<div id="pageNumArea">
<!-- List -->
<ul  class="pagination custom-pagination justify-content-center">
    <!-- 이전 버튼 -->
  	<c:choose>
  		<c:when test="${pageInfo.currentPage > 1}">
	  		<li class="page-item">
	            <a href="#" onclick="onPageClick(${pageInfo.currentPage - 1}); return false;" class="page-link">&laquo;</a>
	        </li>
  		</c:when>
  		<c:otherwise>
	  		<li class="page-item disabled">
	            <a href="#" class="page-link">&laquo;</a>
	        </li>
  		</c:otherwise>
  	</c:choose>
  
    <!-- 번호 버튼-->
    <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="i">
        <li class="page-item <c:if test="${pageInfo.currentPage == i}">active</c:if>">
            <a href="#" onclick="onPageClick(${i}); return false;" class="page-link">${i}</a>
        </li>
    </c:forEach>
    
    <!-- 다음 버튼 -->
    <c:choose>
  		<c:when test="${pageInfo.currentPage < pageInfo.endPage}">
	  		<li class="page-item">
	            <a href="#" onclick="onPageClick(${pageInfo.currentPage + 1}); return false;" class="page-link">&raquo;</a>
	        </li>
  		</c:when>
  		<c:otherwise>
	  		<li class="page-item disabled">
	            <a href="#" class="page-link">&raquo;</a>
	        </li>
  		</c:otherwise>
  	</c:choose>
</ul>
</div>
