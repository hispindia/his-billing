<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<openmrs:require privilege="View Bills" otherwise="/login.htm" redirect="/module/billing/main.form" />

<p><b><a href="driver.form">Add new driver</a></b></p>

<h2><spring:message code="billing.driver.search" /></h2>
<br/>

<form class="box" id="searchForm" method="post">
	<table >
		<tr>
			<td><spring:message code="billing.name"/></td>
			<td><input type="text" id="searchText" name="searchText" value="${searchText}" /></td>
			<td><input type="submit" value="Search"/></td>
			<td><input type="button" onclick="listAll()" value="List all"/></td>
		</tr>
	</table>
</form>

<br/>   
<span class="boxHeader">List Drivers</span>
<table class="box">
	<tr>
		<th><b><spring:message code="general.name"/></b></th>
		<th><b><spring:message code="general.description"/></b></th>
	</tr>
	<c:choose>
	<c:when test="${not empty drivers }">
	<c:forEach items="${drivers}" var="driver" varStatus="rowStatus">
		<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
			<td><a href="ambulanceBill.list?driverId=${driver.driverId}">${driver.name }</a></td>
			<td>${driver.description}</td>
		</tr>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<tr><td colspan="2"><spring:message code="billing.noresult"/></td></tr>
	</c:otherwise>
	
	</c:choose>
</table>
<script>
function listAll(){
	window.location.href = "searchDriver.form";
}
</script>