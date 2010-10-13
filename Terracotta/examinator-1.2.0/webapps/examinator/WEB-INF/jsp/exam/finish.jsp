<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
<title>Confirm Finish Exam</title>

<script type="text/javascript">
function doOnLoad(remainingTime) {
	initializeExamTimer(remainingTime);
}
</script>
<script type="text/javascript" src="<c:url value="/js/examTimer.js" />"></script>

</head>

<body onload="doOnLoad(${remainingTimeInSeconds})">
<div class="col1">
<form:form name="examForm">
	<h2>Are you sure you want to end your Exam?</h2>
	<p align="left">
	Remaining Time: <span id="remainingTime" class="remainingTime">${remainingTimeInSeconds} seconds</span>
	</p>
	You have
	<ul>
		<li><b>${answeredQuestionsCount}</b> out of ${totalQuestions} Questions Answered</li>
		<li><b>${unansweredQuestionsCount}</b> out of ${totalQuestions} Questions Not Attempted</li>
		<li><b>${questionsMarkedForReviewCount}</b> out of ${totalQuestions} Questions Marked for Review</li>
	</ul>
	<p/>
	<input type="submit" id="showAllQuestions" name="_eventId_showAllQuestions" value="Show All Questions" />
	<input type="submit" id="endExam" name="_eventId_endExam" value="Finish Exam" />
</form:form> 
</div>
</body>
</html>