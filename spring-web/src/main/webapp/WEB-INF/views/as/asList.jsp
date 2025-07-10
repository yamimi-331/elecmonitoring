<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
            <input type="hidden" name="as_cd" value="${item.as.as_cd}">
            <button type="submit">상세보기</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </c:otherwise>
</c:choose>
