<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Registration</title>
</head>
<body>
<div class="col1">
<h1>Please provide your personal details</h1>

<div class="centerBox lighter padded">
<form:form modelAttribute="user">
	<div class="details">
		<div class="detailsSubSection">
			<div class="label">
				User Name:<form:errors path="userName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="userName" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Password:<form:errors path="password" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:password path="password" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Email:<form:errors path="email" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="email" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				First Name:<form:errors path="firstName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="firstName" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Last Name:<form:errors path="lastName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="lastName" size="30" maxlength="80" />
			</div>
		</div>
		<div class="buttons">
			<input type="submit" value="Register" />
		</div>
	</div>
</form:form>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>