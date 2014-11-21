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
	<a href="${pageContext.servletContext.contextPath}/create">Home</a> &gt; User
</h4>
<h5>List of existing users:</h5>
<form method="post" action="#">
	<table width="100%">
		<tr>
			<td>UserId</td>
			<td>Username</td>
			<td>Email</td>
			<td>Password</td>
		</tr>
		<c:if test="${not empty users}">
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.userId}</td>
					<td>${user.userName}</td>
					<td>${ user.email }</td>
					<td>*******</td>
				</tr>
			</c:forEach>
		</c:if>
		<tr>
			<td>&nbsp;</td>
			<td><input type="text" name="username" required="required" /></td>
			<td><input type="text" name="email" required="required" /></td>
			<td><input type="text" name="password" required="required" /></td>
		</tr>
	</table>
	<input type="submit" value="Submit" />
</form>
<body>
</body>
</html>