<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@page import="org.terracotta.reference.exam.mvc.controller.exam.OngoingExamsController"%>
<%@page import="org.terracotta.reference.exam.domain.ExamSession"%>
<%@page import="org.terracotta.reference.exam.mvc.model.PageData"%>
<html>
<head>
	<title>Ongoing Exams</title>
	<script type="text/javascript" src="<c:url value="/js/paging.js" />"></script>
</head>

<body>

<%!
private void outputNiceRemainingTime(JspWriter out, ExamSession examSession) throws Exception {
	int remainingTimeInSeconds = (int)examSession.getRemainingTimeInSeconds();
	long hour = Math.max(remainingTimeInSeconds/(60*60), 0);
	int secsLeft = remainingTimeInSeconds % (60*60);
	int mins = Math.max(secsLeft/60, 0);
	secsLeft = secsLeft % 60;
	int secs = secsLeft;
	String time = "";
	if (hour > 0) time += hour + " Hrs ";
	if (mins > 0) time += mins + " Mins ";
	if (secs > 0) time += secs + " Secs";
	//examSessions hang on till 2 seconds after the exam times out.
	if (remainingTimeInSeconds <= 1) time = "1 Sec";
	out.println(time);
}
%>

<div class="col1">
<c:set var="examSessions" value="${pageData.data}"></c:set>
<c:if test="${empty examSessions}">
<h2>No exams are ongoing.</h2>
</c:if>

<c:if test="${not empty examSessions}">
<h2>List of ongoing exams</h2>
<form name="pageForm">
<jsp:include page="../paging/include.jsp"></jsp:include>
</form>
<div class="centerBox lighter">
<table cellspacing="5" width=100%>
	<tr>
		<th> Sl. No.</th>
		<th> Exam </th>
		<th> User </th>
		<th> Start Time </th>
		<th> Remaining Time </th>
		<th> Answered Questions </th>
	</tr>
	<c:forEach items="${examSessions}" var="examSessionVar" varStatus="status">
		<c:set var="examSession" value="${examSessionVar}" scope="page"></c:set>
		<!--  choose TR class, alternate colors -->
		<tr
			<c:choose> 
				<c:when test="${status.count %2 == 0 }">
					class="lighter"
				</c:when>
				<c:otherwise>
					class="darker"
				</c:otherwise>
			</c:choose>
		>			
			<td>${status.count + pageData.showingFrom - 1}</td>
			<td>
				${examSession.examName}
			</td>
			<td>
				${examSession.userName}
			</td>
			<td>
				${examSession.startTime}
			</td>
			<td>
				<span id="remainingTime">
					<%
						ExamSession examSession = (ExamSession)pageContext.getAttribute("examSession");
						outputNiceRemainingTime(out, examSession);
					%>
				</span> 
			</td>
			<td>
				${examSession.answeredQuestionsCount}
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</c:if>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>

</div>
</body>
</html>