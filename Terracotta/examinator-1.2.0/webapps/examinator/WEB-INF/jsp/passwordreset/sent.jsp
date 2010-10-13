<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
	<head>
		<title>Reset password</title>
	</head>
<body>
<div class="col1">
<h1>Password reset instructions sent</h1>

<p>The instructions to reset the password were sent to <%=request.getParameter("email")%>.</p>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>