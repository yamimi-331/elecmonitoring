<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<style>
.container{
	margin: 0 20px;
}
</style>
<script>
function loadData(sortType) {
    $.ajax({
      url: '/as/detail/list',
      type: 'GET',
      data: { sort: sortType },
      success: function(data) {
        $('#listArea').html(data);
      },
      error: function(err) {
        console.error(err);
      }
    });
  }
</script>

</head>
<body>
	<div class="wrapper">
		<h2 class="header-title">A/S 신고</h2>
		<%@ include file="/WEB-INF/views/common/commonHeader.jsp" %>
		<main class="main">
			<h2>AS 신고 내역</h2>
			<div class="container">
				<div class="custom-table">
					<button onclick="loadData('reportDate')">신고일자순</button>
					<button  onclick="loadData('reservationDate')">예약일자순</button>
				</div>
				<table class="custom-table">
					<thead>
						<tr>
							<th>번호</th>
							<th>신고 내역</th>
							<th>예약 일시</th>
							<th>진행 상태</th>
							<th>상세 정보</th>
						</tr>
					</thead>
					<tbody id="listArea">
						<c:choose>
							<c:when test="${empty userList }">
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
					</tbody>
				</table>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>