<%@ page
	import="org.terracotta.reference.exam.mvc.controller.WelcomeController"%>
<%@page
	import="org.terracotta.reference.exam.mvc.controller.user.ListUsersController"%>
<%@ include file="/WEB-INF/jsp/includes.jsp"%>


<%@page import="org.terracotta.reference.exam.service.StandardAuthoritiesService"%><html>
<head>
<title>Edit user</title>

<sec:authorize ifAllGranted="<%=StandardAuthoritiesService.ADMINISTRATOR %>">
<script type="text/javascript">
<!--
function addRole() {
	var selected = getSelectedOptions(document.editForm.availableRoles.options);
	var i = 0;
	for (i = 0; i < selected.length; i++) {
		var opt = selected[i];
		document.editForm.availableRoles.remove(opt.index);
		document.editForm.roles.add(opt, null);
	}
}

function removeRole() {
	var selected = getSelectedOptions(document.editForm.roles.options);
	var i = 0;
	for (i = 0; i < selected.length; i++) {
		var opt = selected[i];
		document.editForm.roles.remove(opt.index);
		document.editForm.availableRoles.add(opt, null);
	}
}

function getSelectedOptions(options) {
	var selected = new Array();
	var i;
	for (i = 0; i < options.length; i++) {
		var option = options[i];
		//alert('text: ' + option.text + ' value: ' + option.value + ' selected: ' + option.selected);
		if (option.selected) {
			selected.push(option);
		}
	}
	return selected;
}

function submitForm(cmd) {
	document.editForm.submitButton.disable = true;
	selectAll(document.editForm.roles.options);
	document.editForm.cmd.value = cmd;
	document.editForm.submit();
}

function selectAll(options) {
	var i;
	for (i = 0; i < options.length; i++) {
		options[i].selected = true;
	}
}
-->
</script>
</sec:authorize>
</head>
<body>
<div class="col1">
<h1>Edit the user details</h1>
<div class="centerBox lighter padded">
<form:form modelAttribute="user" name="editForm">
	<input type="hidden" name="cmd" />
	<div class="details">
		<div class="detailsSubSection">
			<div class="label">
				User Name: <form:errors path="userName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="userName" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Email Address: <form:errors path="email" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="email" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				First Name: <form:errors path="firstName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="firstName" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="label">
				Last Name: <form:errors path="lastName" cssStyle="color: red" />
			</div>
			<div class="input">
				<form:input path="lastName" size="30" maxlength="80" />
			</div>
		</div>
		<div class="detailsSubSection">
			<div class="availableRoles">
				Available Roles:
				<select name="availableRoles" multiple="multiple"
					class="option">
					<c:forEach var="role" items="${userRoleList}">
						<option value="<c:out value="${role.roleName}" />">
							<fmt:message key="roles.${role.roleName}" />
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="rolesButtons">
				<sec:authorize ifAllGranted="<%=StandardAuthoritiesService.ADMINISTRATOR %>">
					<input type="button" name="addButton" value="Add &gt;&gt;"
						onclick="javascript:addRole();" />
					<input type="button" name="removeButton" value="&lt;&lt; Remove"
						onclick="javascript:removeRole()" />					
				</sec:authorize>
			</div>
			<div class="assignedRoles">
				Assigned Roles:
				<form:select path="roles" cssClass="option"
					multiple="multiple">	
					<c:forEach var="role" items="${user.roles}">
						<option value="<c:out value="${role.roleName}" />">
							<fmt:message key="roles.${role.roleName}" />
						</option>
					</c:forEach>				
				</form:select>
			</div>
		</div>
		<sec:authorize ifAllGranted="<%=StandardAuthoritiesService.ADMINISTRATOR %>">
			<div class="buttons">
				<input name="submitButton" type="submit" value="Modify" onclick="javascript:submitForm('modify');return false;" />
				<input name="submitButton" type="submit" value="Delete" onclick="javascript:submitForm('delete');return false;" />
			</div>
		</sec:authorize>
	</div>
</form:form>
</div>
<p><a href="<l:ctrUrl class="<%=ListUsersController.class%>" />" id="BackToListOfUsers">back
to list of users</a> | <a
	href="<l:ctrUrl class="<%=WelcomeController.class%>" />" id="BackToIndex">back to
index</a></p>
</div>
</body>
</html>