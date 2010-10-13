<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
<title>Exam Details</title>

<script type="text/javascript">
function doOnLoad(remainingTime) {
	initializeExamTimer(remainingTime);
}
function submitAndShowQuestion(showQuestionId) {
	this.document.examForm.showQuestionId.value = showQuestionId;
	this.document._eventId.value = "showQuestion";
	this.document.examForm.submit();
	return true;
}
</script>

<script type="text/javascript" src="<c:url value="/js/examTimer.js" />"></script>

</head>
<body onload="doOnLoad(${remainingTimeInSeconds})">
<form:form modelAttribute="examQuestionForm" name="examForm">

<input type="hidden" name="currentQuestionId" value="${examQuestionForm.question.id}" />
<input type="hidden" name="showQuestionId" />

<div class="col1">
	<h1>${examName}</h1>
		
	<c:forEach var="section" items="${examQuestionForm.sections}">
			Section: <c:out value="${section.name}" /> <br />
			Description: <c:out value="${section.description}" /> <br />
			
	</c:forEach>

	<div class="centerBox lighter padded">
		<div class="questionNumber">Question ${examQuestionForm.question.questionNumber}:</div>
		<div class="questionText">${examQuestionForm.question.htmlText}</div>
		<div class="centerBox darker padded">
			<div>Choose an option from the following</div>
			<form:radiobuttons items="${examQuestionForm.orderedChoices}" path="userChoiceId" itemLabel="text" itemValue="id" delimiter="<br />"/>
		</div>
	</div>

	<form:checkbox path="markQuestionForReview" id="questionForReview"/>
	<label for="questionForReview"><small>Mark question for review</small></label>

	<div class="center padded">
		<c:choose>
			<c:when test="${examQuestionForm.hasPreviousQuestion}">
			<input type="submit" id="previous" name="_eventId_previous" value="Previous Question" />
			</c:when>
			<c:otherwise><input type="button" disabled="disabled" value="Previous Question" /></c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${examQuestionForm.hasNextQuestion}">
			<input type="submit" id="next" name="_eventId_next" value="Next Question" />
			</c:when>
			<c:otherwise><input type="button" disabled="disabled" value="Next Question" /></c:otherwise>
		</c:choose>
	</div>
	<div class="center padded">
		<input type="submit" id="confirmFinishExam" name="_eventId_confirmFinishExam" value="Finish Exam" />
	</div>

	<p class="browser_back">
		Please do not use your browser back button.
		<br/> 
		Doing so will take you to the list of exams page again (Your exam session will not be affected)
	</p>
</div>
<div class="col2">
	<p align="center">Remaining Time: <span id="remainingTime" class="remainingTime">${remainingTimeInSeconds} seconds</span></p>
	<p align="center"><input type="submit" id="showAllQuestions" name="_eventId_showAllQuestions" value="Show All Questions" /></p>

	<c:choose>
	<c:when test="${not empty questionsMarkedForReview}">
		<table>
			<tr>
				<td><u><b>Review Questions</b></u></td>
			</tr>	
			<c:set var="numColumns" value="1" />
			<c:set var="columnCounter" value="0" />
			<tr>
				<c:forEach var="question" items="${questionsMarkedForReview}">
					<c:choose>
						<c:when test="${columnCounter < numColumns}">
							<td>
								<input type="submit" name="_eventId_showQuestion" value="Question ${question.questionNumber}" 
									onclick="javascript:submitAndShowQuestion(${question.id});return false;" />
							</td>
							<c:set var="columnCounter" value="${columnCounter + 1}" />
						</c:when>
						<c:otherwise>
							<c:out value="</tr><tr>" escapeXml="false" />
							<td>
								<input type="submit" name="_eventId_showQuestion" value="Question ${question.questionNumber}" 
									onclick="javascript:submitAndShowQuestion(${question.id});return false;" />
							</td>
							<c:set var="columnCounter" value="1" />
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<!-- Show nothing when no question marked for review -->
	</c:otherwise>
	</c:choose>
</div>

</form:form>

</body>
</html>