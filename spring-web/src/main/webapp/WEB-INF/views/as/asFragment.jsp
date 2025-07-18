<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Fragment 페이징 영역 -->
<ul id="pageNumArea" class="pagination custom-pagination justify-content-center">
    <!-- 이전 버튼 -->
    <c:if test="${pageInfo.hasPrev}">
        <li class="page-item">
            <a href="#" onclick="onPageClick(${pageInfo.startPage - 1}); return false;" class="page-link">&laquo;</a>
        </li>
    </c:if>
    
    <!-- 번호 버튼-->
    <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="i">
        <li class="page-item <c:if test="${pageInfo.currentPage == i}">active</c:if>">
            <a href="#" onclick="onPageClick(${i}); return false;" class="page-link">${i}</a>
        </li>
    </c:forEach>
    
    <!-- 다음 버튼 -->
    <c:if test="${pageInfo.hasNext}">
        <li class="page-item">
            <a href="#" onclick="onPageClick(${pageInfo.endPage + 1}); return false;" class="page-link">&raquo;</a>
        </li>
    </c:if>
</ul>