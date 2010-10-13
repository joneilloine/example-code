<%@ page
	import="org.terracotta.reference.exam.mvc.controller.WelcomeController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
<link href="<c:url value="/css/createEditExam.css" />" rel="stylesheet" type="text/css" />
<title>

<c:choose>
	<c:when test="${requestScope.exam == null}">
		Exam Creation
	</c:when>
	<c:otherwise>
		Edit Exam: ${requestScope.exam.name}
	</c:otherwise>
</c:choose>

</title>

<script type="text/javascript" src="<c:url value="/js/jquery-1.2.6.js" />" > </script>
<script type="text/javascript"> jQuery.noConflict(); </script>
<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js" />" > </script>
<script type="text/javascript" src="<c:url value="/js/createEditExam.js" />" > </script>

</head>
<body>
<div class="col1">
<div class="centerBox lighter padded">
<c:choose>
	<c:when test="${requestScope.exam == null}">
		<h1>Create a new Exam</h1>
	</c:when>
	<c:otherwise>
		<h1>Edit Exam: ${requestScope.exam.name}</h1>
	</c:otherwise>
</c:choose>

<c:if test="${requestScope.examValidation != null}">
	<p style="color: red" />Invalid Exam data.  Please correct the following:
	<ul>
		<c:forEach var="error" items="${requestScope.examValidation.errors}">
			<li>${error}</li>
		</c:forEach>
	</ul>
</c:if>

<div class="section TopSection">
<form method="post">
<input type="hidden" name="jsonExam" id="jsonExam" /> 
<input type="hidden" name="examID" id="id" class="id"
	<c:if test="${requestScope.exam != null}">
		value="${requestScope.exam.id}"
	</c:if> 
/>


<div class="details">
	<div class="detailsSubSection">
		<div class="label">
			Exam Name:
		</div>
		<div class="input">
			<input type="text" name="name" id="name" class="name"
					size="30" maxlength="80" 
				<c:if test="${requestScope.exam != null}">
					value="${requestScope.exam.name}"
				</c:if> 
			/>
		</div>
	</div>
	<div class="detailsSubSection">
		<div class="label">
			Exam Description:
		</div>
		<div class="input">
			<input type="text" name="description" id="description"
						class="description" size="30" maxlength="80" 
				<c:if test="${requestScope.exam != null}">
					value="${requestScope.exam.description}"
				</c:if> 
			/>		
		</div>
	</div>
	<div class="detailsSubSection">
		<div class="label">
			Time Limit (minutes):
		</div>
		<div class="input">
			<input type="text" name="timeLimitInMins"
					id="timeLimitInMins" size="30" maxlength="80" 
				<c:if test="${requestScope.exam != null}">
					value="${requestScope.exam.timeLimitInMinutes}"
				</c:if> 
			/>		
		</div>
	</div>
	<div class="detailsSubSection">
		<div class="label">
			Passing score:
		</div>
		<div class="input">
			<input type="text" name="passingScore" id="passingScore"
						size="30" maxlength="80" 
				<c:if test="${requestScope.exam != null}">
					value="${requestScope.exam.passingScore}"
				</c:if> 
			/>		
		</div>
	</div>
</div>

<div class="section NestedSection"><c:choose>
	<c:when test="${requestScope.exam == null}">
			&nbsp;
		</c:when>
	<c:otherwise>

		${requestScope.examBody}

	</c:otherwise>
</c:choose></div>

<div><input type="submit" value="Save" /></div>
</form>
</div>

</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back
to index</a></p>
</div>
</body>
</html>