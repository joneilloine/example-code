<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<!-- Do not forget to include js/paging.js in your jsp if you are including this file -->
<!-- Do not forget to add a form with name "pageForm" in which this jsp is included -->
<table>
	<tr>
		<td>
			Number of records per page <small>(Between 10 and 50)</small> <input name="pageSize" type="text" maxlength="2" size="2" value="${pageData.pageRequest.pageSize}" />
			<input type="hidden" name="start" value="${pageData.pageRequest.start}"/>
			<input type="hidden" id="cmd" name="cmd"/>
		</td>
	</tr>
	<tr>
		<td>
			<small>
				<c:if test="${pageData.hasFirst}"> <a href="javascript:submitPage('first')">First ${pageData.pageRequest.pageSize}</a></c:if>
				<c:if test="${!pageData.hasFirst}"> First ${pageData.pageRequest.pageSize}</c:if> |
				<c:if test="${pageData.hasPrevious}"> <a href="javascript:submitPage('prev')">Previous ${pageData.pageRequest.pageSize}</a></c:if>
				<c:if test="${!pageData.hasPrevious}">Previous ${pageData.pageRequest.pageSize}</c:if> |
					Showing 
					</small><b>${pageData.showingFrom}</b><small> 
						to
					</small><b>${pageData.showingTo}</b><small> 
						of
					</small><b>${pageData.total}</b><small> |
				<c:if test="${pageData.hasNext}"> <a href="javascript:submitPage('next')">Next ${pageData.pageRequest.pageSize}</a></c:if>
				<c:if test="${!pageData.hasNext}"> Next ${pageData.pageRequest.pageSize}</c:if> |
				<c:if test="${pageData.hasLast}"> <a href="javascript:submitPage('last')">Last ${pageData.pageRequest.pageSize}</a></c:if>
				<c:if test="${!pageData.hasLast}"> Last ${pageData.pageRequest.pageSize}</c:if>
			</small>
		</td>
	</tr>
</table>