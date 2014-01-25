<%--
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author: Abhishek-Ankur
 *  date: 1-Jan-2013
 *  Requirement: Indoor Patient Billing
--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp"%>
<openmrs:require privilege="View Bills" otherwise="/login.htm" />

<style type="text/css">
.hidden {
	display: none;
}
</style>

<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 70px;
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
	.printfont {
		font-family: "Dot Matrix Normal", Arial, Helvetica, sans-serif;
		font-style: normal;
		font-size: 14px;
	}
}
</style>

<script type="text/javascript">
	
</script>

<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/styles/paging.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/paging.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery.PrintArea.js"></script>
	
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message
			code="${error.defaultMessage}" text="${error.defaultMessage}" /> </span>
</c:forEach>
<c:if test="${not empty listBill}">
	<div id="billContainer" style="margin: 10px auto; width: 1000px;">
		<table>
			<tr>
				<td><b>Patient's Identifier:&nbsp;&nbsp;</b></td>
				<td>${patient.patientIdentifier.identifier}</td>
			</tr>
			<tr>
				<td><b>Patient's Name:&nbsp;&nbsp;</b></td>
				<c:set var="person" value="${patient.personName}"/>
				<td>${person.givenName}&nbsp;&nbsp;${person.middleName}&nbsp;&nbsp;
					${personfamilyName}</td>
			</tr>
			<tr>
				<td><b>Admitted Ward:&nbsp;&nbsp;</b></td>
				<td>${admittedWard}</td>
			</tr>
			<tr>
				<td><b>Treating Doctor:&nbsp;&nbsp;</b></td>
				<td>${treatingDoctor}</td>
			<tr>
				<td><b>Date of Admission:&nbsp;&nbsp;</b></td>
				<td><openmrs:formatDate date="${admissionDate}"
						type="textbox" /></td>
			</tr>
		</table>
	</div>
	
	<table id="myTable" class="tablesorter" align="center">
	<thead>
		<tr> 
			<th>S.No</th>
			<th>Service</th>
			<th>Item Bill Date</th>
			<th>Pay</th>
			<th>Unit Price</th>	
		</tr><tr></tr>
	</thead>
	<tbody>
		<c:forEach var="listBillItems" items="${listBill}" varStatus="outerIndex">
			<c:set var="paid" value="${serviceBill[outerIndex.index].printed}"/>
			<c:forEach var="billItems" items="${listBillItems}" varStatus="index">
				<c:choose >
				<c:when test="${paid}"> 
					<tr>
						<td style ="background-color:#00FF00;">${index.count}</td>
						<td style ="background-color:#00FF00;">${billItems.name}</td>
						<td style ="background-color:#00FF00;"><openmrs:formatDate date="${billItems.createdDate}"
							type="textbox" /></td>
						<td style ="background-color:#00FF00;"><input type="checkbox" id="paid" checked></td>
						<td style ="background-color:#00FF00;">${billItems.unitPrice}</td>
					</tr>
				</c:when>	
				<c:otherwise>
					<tr>
						<td>${index.count}</td>
						<td>${billItems.name}</td>
						<td><openmrs:formatDate date="${billItems.createdDate}"
							type="textbox" /></td>
						<td><input type="checkbox" id="paid" checked></td>
						<td>${billItems.unitPrice}</td>
					</tr>
				</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:forEach>
	</tbody>
	</table>
</c:if>