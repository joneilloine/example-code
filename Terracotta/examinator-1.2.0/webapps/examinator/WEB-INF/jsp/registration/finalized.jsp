<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController, 
	org.terracotta.reference.exam.mvc.controller.user.*" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Registration - Finalized</title>
</head>
<body>
<div class="col1">
<h1>Congratulations, your registration was successfully finalized!</h1>
Click <a href="<l:ctrUrl class="<%=LoginController.class%>" />" id="Login">here</a> to Login

<p>
<a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a>
</p>
</div>
</body>
</html>