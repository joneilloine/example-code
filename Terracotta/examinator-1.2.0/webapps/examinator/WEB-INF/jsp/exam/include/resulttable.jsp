<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<tr>
	<td>Result</td>
	<td>
		<c:choose>
			<c:when test="${examResult.passed}">
				<span class="passed">PASSED</span>
			</c:when>
			<c:otherwise>
				<span class="failed">FAILED</span>
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr>
	<td>Score</td>
	<td>${examResult.score}</td>
</tr>
<tr>
	<td>Start Time</td>
	<td>${examResult.startTime}</td>
</tr>
<tr>
	<td>End Time</td>
	<td>${examResult.endTime}</td>
</tr>
<tr>
	<td>Correct answers</td>
	<td>${examResult.correctAnswers}</td>
</tr>
<tr>
	<td>Wrong answers</td>
	<td>${examResult.wrongAnswers}</td>
</tr>
<tr>
	<td>Unattempted Answers</td>
	<td>${examResult.unattemptedAnswers}</td>
</tr>