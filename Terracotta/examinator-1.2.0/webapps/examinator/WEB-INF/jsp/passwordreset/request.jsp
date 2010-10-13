<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
	<head>
		<title>Reset password</title>
	</head>
<body>
<div class="col1">
<h1>Reset your password</h1>

<p>Please fill in your email address.</p>
<p>You will receive an email that contains the instructions of how to reset your password.</p>

<c:if test="${userNotFound}">
	<p style="color: red">No user was found with this email address.</p>
</c:if>

<div class="centerBox lighter padded">
<form method="post">
	<div class="details">
		<div class="detailsSubSection">
			<div class="label">Email:</div>
			<div class="input">
				<input type="text" name="email" size="30" maxlength="80" />
			</div>
		</div>
		<div class="buttons">
			<input type="submit" value="Request password reset" />
		</div>
	</div>
</form>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>