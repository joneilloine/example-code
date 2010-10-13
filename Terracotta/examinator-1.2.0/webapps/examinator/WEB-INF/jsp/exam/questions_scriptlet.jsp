<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="org.terracotta.reference.exam.domain.Question"%>
<html>
<head>
<title>Exam Details</title>
<style type="text/css">
.answered {background-color: #33CC99}
.unanswered {background-color: #FF66FF}
</style>
<script type="text/javascript">
function doOnLoad(remainingTime) {
	initializeExamTimer(remainingTime);
}
</script>
<script type="text/javascript" src="<c:url value="/js/examTimer.js" />"></script>

</head>
<%!
private void outputQuestionCell(JspWriter out, String flowExecutionUrl, Question q, List<Question> answeredQuestions, 
		Set<Question> questionMarkedForReview) throws Exception {
	String answeredClass = answeredQuestions.contains(q)? "answered": "unanswered";
	out.println("<td class='" + answeredClass + "'>");
	String qUrl = flowExecutionUrl + "&amp;_eventId=showQuestion&amp;questionId=" + q.getId();
	out.println("<a href='" + qUrl + "' id='"+q.getQuestionNumber()+"'>Question " + q.getQuestionNumber() + "</a>");
	if (questionMarkedForReview.contains(q)) out.println("<font color='red'>**</font>");
	out.println("</td>");
}
%>
<body onload="doOnLoad(${remainingTimeInSeconds})">
<p align="center">
Remaining Time: <span id="remainingTime" class="remainingTime">${remainingTimeInSeconds} seconds</span>
</p>
<form:form name="examForm">

<!-- All questions will be displayed in a table, ordering row-wise -->
<!-- minimum 2 columns and max of 10 -->
<%
	List<Question> questions = (List<Question>)request.getAttribute("questions");
	List<Question> answeredQuestions = (List<Question>)request.getAttribute("answeredQuestions");
	Set<Question> questionsMarkedForReview = (Set<Question>)request.getAttribute("questionsMarkedForReview");
	int answeredQuestionsCount = (Integer)request.getAttribute("answeredQuestionsCount");
	int unansweredQuestionsCount = (Integer)request.getAttribute("unansweredQuestionsCount");
	int questionsMarkedForReviewCount = (Integer)request.getAttribute("questionsMarkedForReviewCount");
	int numColumns = questions.size() / 10;
	if (numColumns > 10) numColumns = 10;
	if (numColumns < 2) numColumns = 2;
%>

<table align="center">
	<tr>
		<td align="center" colspan="<%=numColumns%>">
			<h2>${examName}</h2>
		</td>
	</tr>
	<tr>
<%
	String flowExecUrl = (String)request.getAttribute("flowExecutionUrl");
	int columnCounter = 0;
	for (Question question : questions) {
		if (columnCounter < numColumns) {
			outputQuestionCell(out, flowExecUrl, question, answeredQuestions, questionsMarkedForReview);
			columnCounter++;
		}
		else {
			out.println("</tr><tr>");
			outputQuestionCell(out, flowExecUrl, question, answeredQuestions, questionsMarkedForReview);
			columnCounter = 1;
		}
	}
%>
	</tr>
	<tr>
		<td align="center" colspan="<%=numColumns%>">
			<table>
				<tr><td colspan="2">Legend</td></tr>
				<tr>
					<td><font color="red">**</font>
					</td>
					<td><small>Questions marked for review (<%=questionsMarkedForReviewCount%>)</small>
					</td>
				</tr>
				<tr>
					<td class="answered">
					</td>
					<td><small>Answered Questions (<%=answeredQuestionsCount%>)</small>
					</td>
				</tr>
				<tr>
					<td class="unanswered">
					</td>
					<td><small>Unanswered Questions (<%=unansweredQuestionsCount%>)</small>
					</td>
				</tr>	
			</table>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="<%=numColumns%>"><br />
		</td>
	</tr>
	<tr>
		<td colspan="<%=numColumns%>" align="center">
			<input type="submit" id="confirmFinishExam" name="_eventId_confirmFinishExam" value="Finish Exam" />
		</td>
	</tr>
</table>
</form:form>
</body>
</html>