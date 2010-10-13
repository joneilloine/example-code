<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="l" uri="/WEB-INF/taglib/examinator.tld" %>
<%@ page import="org.terracotta.reference.exam.service.StandardAuthoritiesService,
	org.terracotta.reference.exam.mvc.controller.profile.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Examinator - <decorator:title /></title>
		<link href="<c:url value="/css/styles.css" />" rel="stylesheet" type="text/css" />
        <decorator:head />
    </head>
    
    <body onload="<decorator:getProperty property="body.onload" />">

	<div class="header_area">
		<sec:authorize ifAnyGranted="<%=StandardAuthoritiesService.STUDENT_AND_ADMINISTRATOR %>" >
			<div class="toplinks">
				<p>
					You are logged in as <strong><sec:authentication property="name" /></strong>  |  
					<a href="<c:url value="/logout.do" />" id="logout">Logout</a>  |  
					<a href="<l:ctrUrl class="<%=ChangePasswordController.class%>" />" id="changePassword">Change password</a>
				</p>
			</div>
		</sec:authorize>
		<div class="toplogo">
		<a href="<%=request.getContextPath()%>"><img name="logo_top_left" src="<%=request.getContextPath()%>/images/logo_top_left.png" id="logo_top_left" alt="" /></a>
		<a href="http://www.terracotta.org"><img name="logo_top_right" src="<%=request.getContextPath()%>/images/logo_top_right.png" id="logo_top_right" alt="" /></a>
		</div>
	</div>

	<div class="divider"><div class="divider_right"></div><div class="divider_left"></div></div>

	<div class="content_area">
		<div class="colmask leftmenu">
			<div class="colleft">
				<decorator:body />
			</div>
		</div>
	</div>

	<div class="divider"><div class="divider_right"></div><div class="divider_left"></div></div>

	<div class="footer"><a href="http://www.terracotta.org"><img name="bottom_logo" src="<%=request.getContextPath()%>/images/bottom_logo.png" width="122" height="48" alt="logo" /></a></div>
	
    </body>
</html>