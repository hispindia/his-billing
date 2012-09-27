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
<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<openmrs:require privilege="View Bills" otherwise="/login.htm"
	redirect="/module/billing/main.form" />

<p>
	<b><a href="driver.form">Add new driver</a>
	</b>
</p>

<h2>
	<spring:message code="billing.driver.search" />
</h2>
<br />

<form class="box" id="searchForm" method="post">
	<table>
		<tr>
			<td><spring:message code="billing.name" />
			</td>
			<td><input type="text" id="searchText" name="searchText"
				value="${searchText}" />
			</td>
			<td><input type="submit" value="Search" />
			</td>
			<td><input type="button" onclick="listAll()" value="List all" />
			</td>
		</tr>
	</table>
</form>

<br />  <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --update title-->
<span class="boxHeader">List of Drivers</span>
<table class="box">
	<tr>
		<th><b><spring:message code="general.name" />
		</b>
		</th>
		<th><b><spring:message code="general.description" />
		</b>
		</th>
	</tr>
	<c:choose>
		<c:when test="${not empty drivers }">
			<c:forEach items="${drivers}" var="driver" varStatus="rowStatus">
				<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" }'>
					<td><a href="ambulanceBill.list?driverId=${driver.driverId}">${driver.name
							}</a>
					</td>
					<td>${driver.description}</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td colspan="2"><spring:message code="billing.noresult" />
				</td>
			</tr>
		</c:otherwise>

	</c:choose>
</table>
<script>
function listAll(){
	window.location.href = "searchDriver.form";
}
</script>