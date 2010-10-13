<%@ page import="org.terracotta.reference.exam.service.StandardAuthoritiesService,
	org.terracotta.reference.exam.mvc.controller.registration.*,
	org.terracotta.reference.exam.mvc.controller.user.*,
	org.terracotta.reference.exam.mvc.controller.exam.*,
	org.terracotta.reference.exam.mvc.controller.passwordreset.*" %>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>
<html>
<head>
	<title>Welcome</title>
	<sec:authorize ifNotGranted="<%=StandardAuthoritiesService.STUDENT_AND_ADMINISTRATOR%>">
		<link href="<c:url value="/css/welcome.css" />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value="/js/jquery-1.2.6.js" />" > </script>
		<script type="text/javascript" src="<c:url value="/js/jquery.corner.js" />" > </script>
		<script type="text/javascript" src="<c:url value="/js/welcome.js" />" > </script>
	</sec:authorize>

</head>
<body>

<!--  not logged in - "landing" page -->
<sec:authorize ifNotGranted="<%=StandardAuthoritiesService.STUDENT_AND_ADMINISTRATOR%>">
	<div class="col1">
	
		<% String docURL = "http://www.terracotta.org/web/display/orgsite/Web+App+Reference+Implementation";%>
		
		<div class='clickable' style='cursor:pointer;width:auto' onclick='document.location.href="<%=docURL%>"' 
	 	onkeypress='document.location.href="<%=docURL%>"'>
			<h1>Welcome to the Examinator</h1>
			<div class="welcomeText">The Terracotta Web App Reference Implementation (aka, The Examinator), provides a thorough example of how to use
			Terracotta for High Availability and Scalability with a best of breed Stack.</div>
			<div class="welcomeText"><a href="http://www.terracotta.org/web/display/orgsite/Web+App+Reference+Implementation">Click this button to read documentation</a>
			about the Examinator and how you can use it to Jump Start your usage of Terracotta.</div>
		</div>
		
		<div class='clickable' style='cursor:pointer;width:auto' onclick='document.location.href="<l:ctrUrl class="<%=LoginController.class%>"/>"'
	 	onkeypress='document.location.href="<l:ctrUrl class="<%=LoginController.class%>"/>"' >

			<h1>Login to the Examinator</h1>
			<div class="welcomeText">If you've already created an Account in the Examinator, <a href="<l:ctrUrl class="<%=LoginController.class%>" />" id="Login">click here to login.</a></div>
			<div class="welcomeText">Otherwise, <a href="<l:ctrUrl class="<%=SignupController.class%>" />" >use the register button</a> below.</div>
		</div>
		
		<div class='clickable' style='cursor:pointer;width:auto' onclick='document.location.href="<l:ctrUrl class="<%=SignupController.class%>"/>"' 
	 	onkeypress='document.location.href="<l:ctrUrl class="<%=SignupController.class%>"/>"' >

			<h1>Register to Use the Examinator</h1>
			<div class="welcomeText">If you would like to try out a live demo of the Examinator, 
				<a href="<l:ctrUrl class="<%=SignupController.class%>" />" id="Register" >create an account here.</a></div>
			<div class="welcomeText">Registering is easy.  You just need to 
				<a href="<l:ctrUrl class="<%=SignupController.class%>" />" >create an account</a>
				name and give us a valid e-mail address so that we may verify your account.</div>
		</div>
		
		<div class='clickable' style='cursor:pointer;width:auto' onclick='document.location.href="<l:ctrUrl class="<%=RequestResetController.class%>"/>"' 
	 	onkeypress='document.location.href="<l:ctrUrl class="<%=RequestResetController.class%>"/>"' >

			<h1>Reset Password</h1>
			<div class="welcomeText">If you have forgotten your password, or would otherwise like to reset it, select
				<a href="<l:ctrUrl class="<%=RequestResetController.class%>" />" id="ResetPassword">Reset password.</a>
				A new password will be e-mailed to you.
				</div>
		</div>
	</div>
</sec:authorize>


<!--  user logged in -->
<sec:authorize ifAnyGranted="<%=StandardAuthoritiesService.STUDENT_AND_ADMINISTRATOR%>">
	<div class="col1">
	<h1>Commands</h1>
	<ul>
		<li><a href="<l:ctrUrl class="<%=RequestResetController.class%>" />" id="ResetPassword">Reset password</a></li>
		
		<!--  admin logged in -->
		<sec:authorize ifAllGranted="<%=StandardAuthoritiesService.ADMINISTRATOR%>">
			<li><a href="<l:ctrUrl class="<%=OngoingExamsController.class%>" />" id="OngoingExams">Ongoing exams</a></li>
			<li><a href="<l:ctrUrl class="<%=ListUsersController.class%>" />" id="ListUsers">List users</a></li>
			<li><a href="<l:ctrUrl class="<%=ListExamsController.class%>" />" id="ListExams">List exams</a></li>
			<li><a href="<l:ctrUrl class="<%=ExamCreationController.class%>" />" id="CreateExam">Create exam</a></li>
		</sec:authorize>
		
		<li><a href="<l:ctrUrl class="<%=ListExamsController.class%>" />" id="TakeExam">Take exam</a></li>
		<li><a href="<l:ctrUrl class="<%=ExamResultsController.class%>" />" id="ShowPreviousExamResults">Show Previous Exam Results</a></li>
	</ul>
	</div>
</sec:authorize>

</body>
</html>