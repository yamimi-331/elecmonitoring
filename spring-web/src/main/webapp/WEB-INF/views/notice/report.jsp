<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../../resources/css/common.css?after" />
 <style>

    table {
      width: 100%;
      border-collapse: collapse;
      border: 1px solid #ccc;
      font-size: 14px;
    }

    thead {
      background-color: #f9f9f9;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 8px 10px;
      text-align: center;
    }

    th {
      font-weight: bold;
      color: #333;
    }

    td a {
      color: #007BFF;
      text-decoration: none;
    }

    td a:hover {
      text-decoration: underline;
    }

    .status {
      color: #888;
    }
  </style>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<div class="container">
			
			 <table>
			    <thead>
			      <tr>
			        <th>번호</th>
			        <th>신고일</th>
			        <th>신고 내용</th>
			        <th>신고자</th>
			        <th>처리일</th>
			        <th>처리 여부</th>
			      </tr>
			    </thead>
			    <tbody>
			      <tr>
			        <td>1</td>
			        <td>22-03-22 10:53</td>
			        <td><span class="highlight">내용입니다.</span></td>
			        <td><a href="#">gabia08</a></td>
			        <td></td>
			        <td class="status">미처리</td>
			      </tr>
			    </tbody>
			  </table>
			</div>
		</main>
	</div>


	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>