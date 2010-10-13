<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Password reset</title>
</head>
<body>
<div class="col1">
<h1>Your password was successfully reset!</h1>

<p>Your new password and your user name have been sent to you through email.</p>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>