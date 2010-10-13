<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Change your password</title>
</head>
<body>
<div class="col1">
<h1>Please provide the following information to change your password</h1>

<div class="centerBox lighter padded">
<form:form modelAttribute="changePasswordData">
	<div class="details">
		<div class="detailsSubSection">
			<div class="label">
				Current password: <form:errors path="currentPassword" cssStyle="color: red" />
			</div>
			<div class="input">
				<input type="password" name="currentPassword" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				New password: <form:errors path="newPassword" cssStyle="color: red" />
			</div>
			<div class="input">
				<input type="password" name="newPassword" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				New password (repeat): <form:errors path="newPassword2" cssStyle="color: red" />
			</div>
			<div class="input">
				<input type="password" name="newPassword2" size="30" maxlength="80" />
			</div>
		</div>
		<div class="buttons">
			<input type="submit" value="Change your password" />
		</div>
	</div>
</form:form>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>