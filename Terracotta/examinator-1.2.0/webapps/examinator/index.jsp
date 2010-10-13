<%@ page import="org.terracotta.reference.exam.mvc.controller.WelcomeController,
	org.terracotta.reference.exam.util.UrlHelper" %>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<%-- Redirected because we can't set the welcome page to a virtual URL. --%>
<c:redirect url="<%=UrlHelper.getControllerRequestMapping(WelcomeController.class)%>" />