<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
<title>Exam Details</title>
<script type="text/javascript">
function doOnLoad(remainingTime) {
	initializeExamTimer(remainingTime);
}
</script>
<script type="text/javascript" src="<c:url value="/js/examTimer.js" />"></script>

</head>
<body onload="doOnLoad(${remainingTimeInSeconds})">


<div class="col1">
	<div align="right"><h2>Time Remaining: <span id="remainingTime" class="remainingTime">${remainingTimeInSeconds} seconds</span></h2></div>

	<form:form name="examForm">
	
	<table class="allQuestions" width=100%>
		
		<tr>
			<th class="allQuestions">Answered Questions ("${answeredQuestionsCount}"):</th>
			<th class="allQuestions">Unnswered Questions ("${unansweredQuestionsCount}"):</th>
			<th class="allQuestions">Questions Marked for Review ("${questionsMarkedForReviewCount}"):</th>
		</tr><tr>
			<td class="allQuestions">
				<ul>
					<c:forEach var="question" items="${answeredQuestions}">
						<li><a href="${flowExecutionUrl}&amp;_eventId=showQuestion&amp;questionId=${question.id}" id="${question.questionNumber}">Question ${question.questionNumber}</a></li>
					</c:forEach>
				</ul>
			</td>
			<td class="allQuestions">
				<ul>
					<c:forEach var="question" items="${unansweredQuestions}">
						<li><a href="${flowExecutionUrl}&amp;_eventId=showQuestion&amp;questionId=${question.id}" id="${question.questionNumber}">Question ${question.questionNumber}</a></li>
					</c:forEach>
				</ul>
			</td>
			<td class="allQuestions">
				<ul>
					<c:forEach var="question" items="${questionsMarkedForReview}">
						<li><a href="${flowExecutionUrl}&amp;_eventId=showQuestion&amp;questionId=${question.id}" id="${question.questionNumber}">Question ${question.questionNumber}</a></li>
					</c:forEach>
				</ul>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="center">
				<input type="submit" id="confirmFinishExam" name="_eventId_confirmFinishExam" value="Finish Exam" />
			</td>
		</tr>
	</table>
	</form:form>
</div>
<div class="col2">
	<h1>${examName}</h1>
</div>
</body>
</html>