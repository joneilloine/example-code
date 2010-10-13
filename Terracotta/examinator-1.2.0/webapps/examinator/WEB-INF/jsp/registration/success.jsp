<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Registration - Confirmation</title>
</head>
<body>
<div class="col1">
<h1>Welcome ${user.userName}, your registration was successful!</h1>
<p>An email has been sent to your email address at ${user.email} with a confirmation code.</p>
<p>Please use this code to finalize your registration. Until then, your account will be disabled.</p>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>