<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@page import="org.terracotta.reference.exam.mvc.controller.exam.ListExamsController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Exam Results</title>
	<script type="text/javascript" src="<c:url value="/js/paging.js" />"></script>
</head>

<body>
<div class="col1">
<c:set var="examResults" value="${pageData.data}"></c:set>
<c:if test="${empty examResults}">
	<c:choose>
		<c:when test="${userName == currentUser}">
			<h2>You haven't taken any exams yet</h2>
		</c:when>
		<c:otherwise>
			<h2><c:out value="${userName}" /> hasn't taken any exams yet</h2>
		</c:otherwise>
	</c:choose>
</c:if>

<c:if test="${not empty examResults}">
	<c:choose>
		<c:when test="${userName == currentUser}">
			<h2>Your exam results</h2>
		</c:when>
		<c:otherwise>
			<h2>List of exam results for <c:out value="${userName}" /></h2>
		</c:otherwise>
	</c:choose>
	<form name="pageForm">
		<jsp:include page="../paging/include.jsp"></jsp:include>
		<input type="hidden" name="userName" value="${userName}" />
	</form>
	<div class="centerBox lighter">
	<table width=100%>
		<tr>
			<td align="center"> Sl. No. </td>
			<td align="center"> Exam </td>
			<td align="center"> Result </td>
			<td align="center"> Score </td>
			<td align="center"> Start Time </td>
			<td align="center"> End Time </td>
		</tr>
		<c:forEach items="${examResults}" var="examResult" varStatus="status">
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
				<td align="center">${status.count + pageData.showingFrom - 1}</td>
				<td align="center">
					${examResult.exam.name}
				</td>
				<td align="center">
					<c:if test="${examResult.passed}"><span class="passed">PASSED</span></c:if>
					<c:if test="${! examResult.passed}"><span class="failed">FAILED</span></c:if>
				</td>
				<td align="center">
					${examResult.score}
				</td>
				<td align="center">
					${examResult.startTime}
				</td>
				<td align="center">
					${examResult.endTime}
				</td>
			</tr>
		</c:forEach>
	</table>
	</div>
</c:if>

<p>
	<a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">Back to Index</a> &nbsp;&nbsp;
	<c:if test="${userName == currentUser}">
		<a href="<l:ctrUrl class="<%=ListExamsController.class%>" />" id="ListExams">Take Exam</a>
	</c:if>
</p>
</div>
</body>
</html>