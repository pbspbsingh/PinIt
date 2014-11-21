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
	<a href="${pageContext.servletContext.contextPath}/create">Home</a> &gt; Feeds
</h4>
<h4 style="color:red">${ error }</h4>
<h5>List of existing feeds:</h5>
<form method="post" action="#" id="myForm">
	<table width="100%">
		<tr>
			<td>Icon</td>
			<td>App</td>
			<td>User</td>
			<td>Date</td>
			<td>Comments</td>
			<td>Like</td>
			<td>Pin</td>
			<td>Rating</td>
		</tr>
		<c:if test="${not empty feeds}">
			<c:forEach var="timeline" items="${ feeds }">
				<tr>
					<td><img src="data:image/png;base64,${ timeline.app.iconData }" style="height:50px; width:50px"/></td>
					<td>${ timeline.app.appTitle }</td>
					<td>${ timeline.user.userName }</td>
					<td>${ timeline.time }</td>
					<td>${ timeline.comments }</td>
					<td>${ timeline.likes }</td>
					<td>${ timeline.pinned }</td>
					<td>${ timeline.rating }</td>
				</tr>
			</c:forEach>
		</c:if>
		<tr>
			<td>&nbsp;</td>
			<td>
				<select name="app" required="required">
					<c:forEach var="app" items="${ allApps }">
						<option value="${ app.appId }">${ app.appTitle }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select name="user" required="required">
					<c:forEach var="user" items="${ allUsers }">
						<option value="${ user.userId }">${ user.userName }</option>
					</c:forEach>
				</select>
			</td>
			<td><input type="text" name="nothing" required="required" value="now()" readonly="readonly" /></td>
			<td><textarea rows="2" cols="22" name="comment" required="required"></textarea> </td>
			<td><input type="checkbox" name="like" id="like" /> Like</td>
			<td><input type="checkbox" name="pin" id="pin" /> Pin It</td>
			<td>
				<select name="rating">
					<option value="0">0</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Submit" />
</form>
<script type="text/javascript">
(function(){
	document.getElementById('myForm').onsubmit = function(event){
		var isLiked = document.getElementById('like').checked;
		var isPinned = document.getElementById('pin').checked;
		if(!isLiked && !isPinned) {
			alert('You should at least Pin or like the app!');
			event.preventDefault();
		}
	}
})();
</script>
<body>
</body>
</html>