<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController"%>
<%@ page import="org.terracotta.reference.exam.mvc.controller.exam.ExamDetailsController"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<html>
	<head>
		<title>Exams</title>
		<script type="text/javascript" src="<c:url value="/js/paging.js" />"></script>
	</head>
<body>
<div class="col1">

<h1>These are the available exams</h1>
<form name="pageForm">
<jsp:include page="../paging/include.jsp"></jsp:include>
</form>
<c:set var="exams" value="${pageData.data}"></c:set>
<div class="centerBox lighter">
<display:table name="${exams}" id="exam" class="wide">
	<display:column title="Sl. No.">
		${exam_rowNum + pageData.showingFrom - 1}
	</display:column>
	<display:column title="Exam name">
		<a href="<l:ctrUrl class="<%=ExamDetailsController.class%>" />?examId=${exam.id}" id="${exam.name}">${exam.name}</a>
	</display:column>
	<display:column title="Description" property="description" />
	<display:column title="Exam Time Limit (in minutes)" property="timeLimitInMinutes" />
	<display:column title="Passing Score" property="passingScore" />
</display:table>
</div>
<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>