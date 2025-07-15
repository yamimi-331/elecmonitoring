<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전기 재해 신고 목록</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
 <style>
 	.container{
 		margin: 10px 20px;
 	}
	.report-table {
		width: 100%;
		border-collapse: collapse;
		border: none;
	}

	.report-table thead {
		background-color: #f9f9f9;
		border-top: 2px solid black;
    }

    .report-table td {
		border: 1px solid #ccc;
		padding: 8px 10px;
		text-align: center;
    }

    .report-table th {
		background-color: #f9f9f9;
		border-top: 2px solid black;
		border: 1px solid #ccc;
		font-weight: bold;
		padding: 8px 10px;
		color: #333;
	}
    
     /* 첫 열: 왼쪽 테두리 제거 */
    .report-table td:first-child,
    .report-table th:first-child {
		border-left: none;
    }

    /* 마지막 열: 오른쪽 테두리 제거 */
    .report-table td:last-child,
    .report-table th:last-child {
		border-right: none;
    }

	.report-table td a {
		color: black;
		text-decoration: none;
    }

    .report-table td a:hover {
		text-decoration: underline;
    }
    
    .search_addr{
		font-size: 15px;
		margin: 10px 0;
	}
	.search_addr button{
		background-color: #0070c0;
		color: white;
		padding: 2px 10px;
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
			<h2>전기 재해 신고 목록</h2>
			<div class="container">
				<div class="search_addr">
					<label for="report_addr">지역</label>
					<select name="local" id="report_addr">
					    <option value="전체" selected>전체</option>
					    <option value="서울">서울특별시</option>
					    <option value="부산">부산광역시</option>
					    <option value="대구">대구광역시</option>
					    <option value="인천">인천광역시</option>
					    <option value="광주">광주광역시</option>
					    <option value="대전">대전광역시</option>
					    <option value="울산">울산광역시</option>
					    <option value="세종">세종특별자치시</option>
					    <option value="경기">경기도</option>
					    <option value="강원">강원도</option>
					    <option value="충북">충청북도</option>
					    <option value="충남">충청남도</option>
					    <option value="전북">전라북도</option>
					    <option value="전남">전라남도</option>
					    <option value="경북">경상북도</option>
					    <option value="경남">경상남도</option>
					    <option value="제주">제주특별자치도</option>
					</select>
					<button type="button">조회</button>
				</div>
				<table class="report-table">
					<thead>
						<tr>
							<th>번호</th>
							<th>신고일</th>
							<th>신고 제목</th>
							<th>담당자</th>
							<th>지역</th>
							<th>수정일시</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td>22-03-22 10:53</td>
							<td><a href="#">제목</a></td>
							<td>김직원</td>
							<td>부산광역시</td>
							<td>-</td>
						</tr>
					</tbody>
				</table>
			</div>
		</main>
	</div>


	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>