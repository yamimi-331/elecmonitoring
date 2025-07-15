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
     .button-box button[id="submitBtn"]{
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
     
	.report-detail input {
		box-sizing: border-box;
		padding: 5px;
		font-size: 16px;
	}
	.report-detail select {
		width: 25%;
		padding: 5px;
		font-size: 15px;
	}
	.report-detail textarea {
		width: 100%;
		height: 150px;
		resize: vertical;
		box-sizing: border-box;
		font-size: 18px;
		padding: 10px;
	}
  </style>
</head>
<body>
	<div class="wrapper">
		<%@ include file="/WEB-INF/views/common/header.jsp"%>
		<main class="main">
			<h2>게시글</h2>
			<div class="container">
				<form>
					<input type="hidden" value="report_cd">
					<input type="hidden" value="staff_cd">
					<input type="hidden" value="uddate_dt">
					<table class="report-detail">
						<colgroup>
							<col style="width:15%">
							<col style="width:85%">
						</colgroup>
						<tr>
							<th><label for="title">제목</label></th>
							<td><input type="text" name="title" id="title" placeholder="제목" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="staff_nm">작성자</label></th>
							<td><input type="text" name="staff_nm" id="staff_nm" value="김직원" readonly></td>
						</tr>
						<tr>
							<th><label for="report_dt">신고일</label></th>
							<td><input type="text" name="report_dt" id="report_dt" placeholder="신고일" autocomplete=off></td>
						</tr>
						<tr>
							<th><label for="local">지역</label></th>
							<td>
								<select name="local" id="local">
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
							</td>
						</tr>
						<tr>
							<th><label for="type">유형</label></th>
							<td>
								<select name="type" id="type">
									<option>전기 화재</option>
									<option>전기 감전</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="content">내용</label></th>
							<td><textarea></textarea></td>
						</tr>
					</table>
					<div class="button-box">
						<button type="submit" id="submitBtn">등록하기</button>
						<button type="button" onclick="location.href='/report'">돌아가기</button>
					</div>
				</form>
			</div>
		</main>
	</div>

	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>