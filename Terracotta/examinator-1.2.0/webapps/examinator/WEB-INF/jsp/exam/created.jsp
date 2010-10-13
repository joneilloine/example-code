<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ page import="org.terracotta.reference.exam.mvc.controller.exam.ExamEditController" %>
<%@ page import="org.terracotta.reference.exam.mvc.controller.exam.ExamDeleteController" %>
<%@page import="org.terracotta.reference.exam.mvc.controller.exam.ListExamsController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<html>
<head>
	<title>Exam creation success</title>
</head>
<body>
<div class="col1">
<h1>Exam ${exam.name} created successfully!</h1>
<div class="centerBox lighter">
	<table width=100%>
		<jsp:include page="include/detailstable.jsp" />
	</table>
</div>
<p>Click <a href="<l:ctrUrl class="<%=ListExamsController.class%>" />" id="ListExams">here</a> to see all existing exams. </p>

<p><a href="<l:ctrUrl class="<%=ExamEditController.class%>" />?examID=${exam.id}" id="EditExam">Edit Exam</a>
| <a href="<l:ctrUrl class='<%=ExamDeleteController.class%>' />?examID=${exam.id}" id="deleteExam">Delete Exam</a>
</p>
<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>