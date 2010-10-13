<%@ page
	import="org.terracotta.reference.exam.mvc.controller.WelcomeController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
<title>

<c:choose>
	<c:when test="${exam == null}">
		Exam Creation
	</c:when>
	<c:otherwise>
		Edit Exam: ${exam.name}
	</c:otherwise>
</c:choose>

</title>


</head>
<body>
<div class="col1">
<c:choose>
	<c:when test="${exam == null}">
		<h1>Create a new Exam with JSON</h1>
	</c:when>
	<c:otherwise>
		<h1>View/Edit Exam JSON: ${exam.name}</h1>
	</c:otherwise>
</c:choose>


<form method="post">
<span>put exam JSON here:</span>
<textarea name="jsonExam" id="jsonExam" rows="30" cols="80">
<c:if test="${examJSON != null}">${examJSON}</c:if>
</textarea> 
<input type="submit" value="and click this" />
</form>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />">back
to index</a></p>
</div>
</body>
</html>