<%--
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Billing module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
--%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Drivers" otherwise="/login.htm"
	redirect="/module/billing/main.form" />

<spring:message var="pageTitle" code="billing.driver.manage"
	scope="page" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/styles/paging.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/paging.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<h2>
	<spring:message code="billing.driver.manage" />
</h2>
<p>
	<b><a href="searchDriver.form"><spring:message
				code="billing.ambulance" />
	</a>
	</b>
</p>
<br />
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message
			code="${error.defaultMessage}" text="${error.defaultMessage}" />
	</span><
</c:forEach>
<input type="button" value="<spring:message code='billing.driver.add'/>"
	onclick="javascript:window.location.href='driver.form'" />

<br />
<br />
<c:choose>
	<c:when test="${not empty drivers}">
		<form method="post" onsubmit="return false" id="form">
			<input type="button" onclick="checkValue()"
				value="<spring:message code='billing.driver.deleteselected'/>" /> <span
				class="boxHeader"><spring:message code="billing.driver.list" />
			</span>
			<div class="box">
				<table cellpadding="5" cellspacing="0">
					<tr>
						<th>#</th>
						<th><spring:message code="general.name" />
						</th>
						<th><spring:message code="billing.phone" />
						</th>
						<th><spring:message code="billing.address" />
						</th>
						<th><spring:message code="billing.createddate" />
						</th>
						<th></th>
					</tr>
					<c:forEach items="${drivers}" var="driver" varStatus="varStatus">
						<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } '>
							<td><c:out
									value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }" />
							</td>
							<td><a
								href="javascript:window.location.href='driver.form?driverId=${driver.driverId}'">${driver.name
									}</a></td>
							<td>${driver.phone }</td>
							<td>${driver.address}</td>
							<td><openmrs:formatDate date="${driver.createdDate}"
									type="textbox" />
							</td>
							<td><input type="checkbox" name="ids"
								value="${driver.driverId}" />
							</td>
						</tr>
					</c:forEach>
					</form>
					<tr class="paging-container">
						<td colspan="7"><%@ include file="../paging.jsp"%></td>
					</tr>
				</table>
			</div>
			<script>
function checkValue()
{
	var form = jQuery("#form");
	if( jQuery("input[type='checkbox']:checked",form).length > 0 ) 
		form.submit();
	else{
		alert("Please choose items for deleting");
		return false;
	}
}</script>
	</c:when>
	<c:otherwise>
No driver found.
</c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/template/footer.jsp"%>
