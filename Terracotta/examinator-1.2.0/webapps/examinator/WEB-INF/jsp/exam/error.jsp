<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<html>
	<head>
		<title>Please try again</title>
	</head>
<body>
<div class="col1">
<c:choose>
	<c:when test="${errorIncludePage != null}">
		<jsp:include page="${errorIncludePage}" />
	</c:when>
	<c:otherwise>
		<h2>There were some errors in processing your request</h2>
		Please try again
		<p/>
		Exception: ${flowExecutionException}
		<br/>Cause: ${rootCauseException}
	</c:otherwise>
</c:choose>

<p><a href="${flowExecutionUrl}&amp;_eventId=return" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>