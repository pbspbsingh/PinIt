<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContext.servletContext.contextPath}/favicon.ico" type="image/x-icon">
<title>Create data for "App pin it"</title>
<style type="text/css">
table {
	border-collapse: collapse;
}
td, th {
	border: 1px solid #ccc;
	border-collapse: collapse;
}
</style>
</head>
<h4>
	<a href="${pageContext.servletContext.contextPath}/create">Home</a> &gt; Apps
</h4>
<h4 style="color:red">${ error }</h4>
<h5>List of existing apps:</h5>
<form method="post" action="#">
	<table width="100%">
		<tr>
			<td>Icon</td>
			<td>Package</td>
			<td>Title</td>
			<td>Company</td>
			<td>Category</td>
		</tr>
		<c:if test="${not empty apps}">
			<c:forEach var="app" items="${apps}">
				<tr>
					<td><img src="data:image/png;base64,${ app.iconData }" style="height:50px; width:50px"/></td>
					<td>${ app.appPackage }</td>
					<td>${ app.appTitle }</td>
					<td>${ app.appCompany }</td>
					<td>${ app.category.category }</td>
				</tr>
			</c:forEach>
		</c:if>
		<tr>
			<td><input type="text" name="icon" required="required" /> </td>
			<td><input type="text" name="pkg" required="required" /></td>
			<td><input type="text" name="title" required="required" /></td>
			<td><input type="text" name="company" required="required" /></td>
			<td>
				<select name="category" required="required">
					<c:forEach var="cat" items="${ categories }">
						<option value="${ cat.appCategoryId }">${ cat.category }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Submit" />
</form>
<body>
</body>
</html>