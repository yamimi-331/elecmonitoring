<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 리스트 영역 전체를 감싸는 div에 id 부여 -->
<div id="aslistArea" class="inner-container">
    <table class="normal-table">
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
                                <form action="/as/edit" method="post" style="display:inline;">
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

<!-- 페이징 영역도 별도의 div로 감싸서 id 부여 -->
<div id="pageNumArea" class="pagination">
    <c:if test="${pageInfo.hasPrev}">
        <a href="#" onclick="onPageClick(${pageInfo.startPage - 1}); return false;">◀ 이전</a>
    </c:if>
    <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="i">
        <a href="#" onclick="onPageClick(${i}); return false;"
           class="${pageInfo.currentPage == i ? 'active' : ''}">${i}</a>
    </c:forEach>
    <c:if test="${pageInfo.hasNext}">
        <a href="#" onclick="onPageClick(${pageInfo.endPage + 1}); return false;">다음 ▶</a>
    </c:if>
</div>
