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
		background-color: #f2f2f2;
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
  </style>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>전기 재해 신고 목록</h2>
			<div class="container">

			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>