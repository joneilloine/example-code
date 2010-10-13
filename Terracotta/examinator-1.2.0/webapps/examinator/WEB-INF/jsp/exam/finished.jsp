<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<c:set var="exam" scope="request" value="${examResult.exam}"/>
<html>
<head>
<title>Exam Details</title>
</head>
<body>
<div class="col1">
<div class="centerBox lighter padded">
<h1>Thank you for taking the exam.</h1>

<h2>The Exam</h2>
<table>
	<jsp:include page="include/detailstable.jsp" />
</table>

<h2>Exam Result</h2>
<table>
	<jsp:include page="include/resulttable.jsp" />
</table>
<p />
<a href="${flowExecutionUrl}&amp;_eventId=return" id="Home">back to index</a>
</div>
</div>
</body>
</html>