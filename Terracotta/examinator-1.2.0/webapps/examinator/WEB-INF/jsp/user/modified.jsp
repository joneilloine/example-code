<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<%@page import="org.terracotta.reference.exam.mvc.controller.user.ListUsersController"%><html>
<head>
	<title>User modified</title>
</head>
<body>
<div class="col1">
<h1>The user modifications have been performed correctly</h1>

<p>
	<a href="<l:ctrUrl class="<%=ListUsersController.class%>" />" id="BackToListOfUsers">back to list of users</a> |
	<a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a>
</p>
</div>
</body>
</html>