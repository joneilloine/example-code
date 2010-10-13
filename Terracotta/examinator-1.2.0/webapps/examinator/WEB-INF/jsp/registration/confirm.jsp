<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Registration Confirmation</title>
</head>
<body>
<div class="col1">
<h1>Please provide the following information to finalize your registration</h1>

<c:if test="${invalid}">
	<p style="color: red">Invalid confirmation data.</p>
</c:if>

<div class="centerBox lighter padded">
<form method="post">

	<div class="details">
		<div class="detailsSubSection">
			<div class="label">
				Confirmation Code:
			</div>
			<div class="input">
				<input type="text" name="code" size="30" maxlength="80" />
			</div>
		</div>
		<div class="buttons">
			<input type="submit" value="Finalize registration" />
		</div>
	</div>
</form>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>