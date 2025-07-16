<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>A/S 진행 현황</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
<style>
.container{
	margin: 0 20px;
}
</style>
<script>
/* function loadData(sortType) {
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
  } */
  let currentSort = 'reportDate';
  let currentPage = 1;

  function switchTab(sortType) {
    currentSort = sortType;
    currentPage = 1;

    $('.tab-btn').removeClass('active');
    if (sortType === 'reportDate') {
      $('.tab-btn').eq(0).addClass('active');
    } else {
      $('.tab-btn').eq(1).addClass('active');
    }

    loadData(currentSort, currentPage);
  }

  function loadData(sortType, page = 1) {
    $.ajax({
      url: '/as/detail/list',
      type: 'GET',
      data: { sort: sortType, page: page },
      success: function(data) {
        // 기대: 서버에서 리스트 fragment와 페이징 fragment를 각각 나눠 반환하거나 둘 다 묶어서 반환
        $('#listArea').html($(data).find('#listArea').html());
        $('#paginationArea').html($(data).find('#paginationArea').html());
      },
      error: function(err) {
        console.error(err);
      }
    });
  }

  function onPageClick(page) {
    currentPage = page;
    loadData(currentSort, currentPage);
  }

  $(document).ready(function() {
    loadData(currentSort, currentPage); // 초기 탭 로딩
  });

</script>

</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
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
				<!-- 페이징 -->
				<div class="pagination" id="paginationArea">
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
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>