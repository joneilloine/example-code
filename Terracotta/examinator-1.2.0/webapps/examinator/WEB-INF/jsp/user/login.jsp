<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController,
	org.terracotta.reference.exam.mvc.controller.registration.*,
	org.terracotta.reference.exam.mvc.controller.user.*,
	org.terracotta.reference.exam.mvc.controller.passwordreset.*" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Login</title>
</head>
<body>
<div class="col1">
<h1>Login with user name and password</h1>
<c:if test="${loginError}">
	<p style="color: red">
		Your login attempt was not successful, try again.<br />
		<c:if test="${errorMessage != null}">
			<br />
			<c:out value="${errorMessage}" />
		</c:if>
	</p>
</c:if>
<div class="centerBox lighter padded">
<form action="j_spring_security_check" method="post">

	<div class="details">
		<div class="detailsSubSection">
			<div class="label">
				User Name:<form:errors path="userName" cssStyle="color: red" />
			</div>
			<div class="input">
				<input type="text" name="j_username" value="${lastUser}" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Password:
			</div>
			<div class="input">
				<input type="password" name="j_password"/>
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Remember me on this computer:
			</div>
			<div class="input">
				<input type="checkbox" name="_spring_security_remember_me" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				<a href="<l:ctrUrl class="<%=RequestResetController.class%>" />" id="ForgotPassword">Forgot password?</a>
			</div>
		</div>
		<div class="buttons">
			<input name="submit" type="submit" value="Login"/>
			<input name="reset" type="reset" />
		</div>
	</div>


</form>
</div>

<p><a href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to index</a></p>
</div>
</body>
</html>