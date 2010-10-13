<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController,
	org.terracotta.reference.exam.mvc.controller.exam.ExamResultsController,
	org.terracotta.reference.exam.mvc.controller.user.EditUserController,
	org.terracotta.reference.exam.mvc.controller.user.ListUsersController" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<html>
	<head>
		<title>Users</title>
		<script type="text/javascript" src="<c:url value="/js/paging.js" />"></script>
	</head>
<body>
<div class="col1">
<h1>These are the available users</h1>
<form name="pageForm">
<jsp:include page="../paging/include.jsp"></jsp:include>
</form>
<c:set var="users" value="${pageData.data}"></c:set>
<div class="centerBox lighter">
<display:table name="${users}" id="user" class="wide">
	<display:column title="Sl. No.">
		${user_rowNum + pageData.showingFrom - 1}
	</display:column>
	<display:column title="User name">
		<a href="<l:ctrUrl class="<%=EditUserController.class%>" />?userId=${user.id}" id="${user.userName}">${user.userName}</a>
	</display:column>
	<display:column title="Email" property="email" />
	<display:column title="First name" property="firstName" />
	<display:column title="Last name" property="lastName" />
	<display:column title="Actions">
		<a href="<l:ctrUrl class="<%=ExamResultsController.class%>" />?userName=${user.userName}" id="Results">results</a>
	</display:column>
</display:table>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>