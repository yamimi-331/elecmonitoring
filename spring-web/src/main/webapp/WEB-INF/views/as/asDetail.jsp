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
	min-height: 60vh;
}
.active{
	font-weight: bold;
}
</style>
<script>
  let currentSort = 'reportDate';
  let currentPage = 1;

  function switchTab(sortType, buttonEl) {
    currentSort = sortType;
    currentPage = 1;

    // 탭 버튼 스타일 조정
    $('.tab-btn').removeClass('active');
    $(buttonEl).addClass('active');

    loadData(currentSort, currentPage);
  }

  function loadData(sortType, page = 1) {
    $.ajax({
      url: '/as/detail/list',
      type: 'GET',
      data: { sort: sortType, page: page },
      success: function(data) {
    	  $('#listArea').html($(data).filter('#aslistArea').html());
    	  $('#paginationArea').html($(data).filter('#pageNumArea').html()); 
     	
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
					<button class="tab-btn active" onclick="switchTab('reportDate', this)">신고일자순</button>
					<button class="tab-btn" onclick="switchTab('reservationDate', this)">예약일자순</button>
				</div>
				<div class="table-container" id="listArea">
				
				</div>
				
				<!-- 페이징 -->
				<div class="pagination custom-pagination" id="paginationArea">
				
				</div>
			</div>
		</main>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>