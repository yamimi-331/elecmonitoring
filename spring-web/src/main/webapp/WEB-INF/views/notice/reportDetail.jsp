<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
 <style>
 	.container{
 		margin: 10px 20px;
 	}
 	
	.report-detail {
		width: 100%;
		border-collapse: collapse;
		border: none;
		border-top: 2px solid #797979;
	}
	.report-detail thead {
		background-color: #f9f9f9;
		border-top: 2px solid black;
    }
    .report-detail td {
		border: 1px solid #ccc;
		padding: 10px 15px;
    }
    .report-detail th {
		background-color: #f2f2f2;
		border: 1px solid #ccc;
		font-weight: bold;
		padding: 10px 20px;
		color: #333;
		text-align: left;
	}
     /* 첫 열: 왼쪽 테두리 제거 */
    .report-detail td:first-child,
    .report-detail th:first-child {
		border-left: none;
    }
    /* 마지막 열: 오른쪽 테두리 제거 */
    .report-detail td:last-child,
    .report-detail th:last-child {
		border-right: none;
    }
    
     .button-box{
     	display: flex;
     	justify-content: center;
     	gap: 15px;
     	margin: 20px 0;
     }
     .button-box button[id="modifyBtn"]{
		background-color: #0070c0;
		color: white;
		font-size: 17px;
		padding: 8px 10px;
		border: none;
		border-radius: 5px;
		cursor: pointer;
     }
     .button-box button{
		background-color: #6c757d;
		color: white;
		font-size: 17px;
		padding: 8px 10px;
		border: none;
		border-radius: 5px;
		cursor: pointer;
     }
  </style>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글</h2>
			<div class="container">
				<table class="report-detail">
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>제목입니다</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>김직원</td>
					</tr>
					<tr>
						<th>신고일</th>
						<td>22-03-22 10:53</td>
					</tr>
					<tr>
						<th>지역</th>
						<td>울산광역시</td>
					</tr>
					<tr>
						<th>유형</th>
						<td>전기 화재</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>신고 내용입니다.</td>
					</tr>
				</table>
				<div class="button-box">
					<c:if test="${currentUserInfo.staff_role eq 'admin'}">
						<button type="button" id="modifyBtn">수정하기</button>
						<button type="button" id="deleteBtn">삭제하기</button>
					</c:if>
					<button onclick="location.href='/report'">돌아가기</button>
				</div>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>