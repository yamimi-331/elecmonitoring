<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 페이징 영역 -->
<c:if test="${pageInfo.hasPrev}">
	<a href="#"
		onclick="onPageClick(${pageInfo.startPage - 1}); return false;">◀
		이전</a>
</c:if>

<c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}"
	var="i">
	<a href="#" onclick="onPageClick(${i}); return false;"
		class="${pageInfo.currentPage == i ? 'active' : ''}"> ${i} </a>
</c:forEach>

<c:if test="${pageInfo.hasNext}">
	<a href="#"
		onclick="onPageClick(${pageInfo.endPage + 1}); return false;">다음 ▶</a>
</c:if>