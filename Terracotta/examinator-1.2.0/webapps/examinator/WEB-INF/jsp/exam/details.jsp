<%@ page import="org.terracotta.reference.exam.service.StandardAuthoritiesService,
	org.terracotta.reference.exam.mvc.controller.WelcomeController,
	org.terracotta.reference.exam.mvc.controller.exam.*"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<html>
<head>
<title>

	<c:choose>
		<c:when test="${isDeleteRequest}">
		Delete this Exam?
		</c:when>
		<c:when test="${isDeleteConfirmation}">
		Exam Deleted
		</c:when>
		<c:otherwise>Exam details</c:otherwise>
	</c:choose>

</title>
</head>
<body>
<div class="col1">
<h1>

	<c:choose>
		<c:when test="${isDeleteRequest}">
		Delete this Exam?
		</c:when>
		<c:when test="${isDeleteConfirmation}">
		Exam Deleted
		</c:when>
		<c:otherwise>Exam details</c:otherwise>
	</c:choose>

</h1>

<div class="centerBox lighter">
<table width=100%>
	<jsp:include page="include/detailstable.jsp" />
</table>
</div>
<table>
	<tr>

		<c:choose>
			<c:when test="${isDeleteRequest}">
				<td>

					<form method="post">
						<input type="submit" value="Delete Exam" />
					</form>

				</td>
			</c:when>
			<c:when test="${isDeleteConfirmation}">
			<!-- nothing here -->
			</c:when>
			<c:otherwise>
				<td>
					<p>
						<a href="<c:url value='/flow/takeexam.do' />?examId=${exam.id}" id="takeExam">Take Exam</a>
					</p>
				</td>
				<td>
					<sec:authorize ifAllGranted="<%=StandardAuthoritiesService.ADMINISTRATOR%>">
						<p>
							| <a href="<l:ctrUrl class='<%=ExamEditController.class%>' />?examID=${exam.id}" id="editExam">Edit Exam</a>
							| <a href="<l:ctrUrl class='<%=ExamDeleteController.class%>' />?examID=${exam.id}" id="deleteExam">Delete Exam</a>
						</p>
					</sec:authorize>
				</td>
			</c:otherwise>
		</c:choose>



	</tr>
</table>

<p>
	<a href="<l:ctrUrl class="<%=ListExamsController.class%>" />" id="BackToListOfExams">back to list exams</a> |
	<a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a>
</p>
</div>
</body>
</html>