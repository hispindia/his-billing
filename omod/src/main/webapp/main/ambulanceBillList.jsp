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
<openmrs:require privilege="View Bills" otherwise="/login.htm"
	redirect="/module/billing/main.form" />
<style type="text/css">
.hidden {
	display: none;
}
</style>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/styles/paging.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/paging.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery.PrintArea.js"></script>
<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message
			code="${error.defaultMessage}" text="${error.defaultMessage}" />
	</span>
</c:forEach>
<div>
	<b><a href="addAmbulanceBill.form?driverId=${driverId}">Add
			Ambulance bill</a>
	</b>
</div>

<c:if test="${not empty ambulanceBill}">
	<div id="billContainer" style="margin: 10px auto; width: 981px;">
		<table>
			<tr>
				<td>Driver name:</td>
				<td>${driver.name }</td>
			</tr>
			<tr>
				<td>Date:</td>
				<td><openmrs:formatDate date="${ambulanceBill.createdDate}"
						type="textbox" />
				</td>
			</tr>
			<tr>
				<td>Bill ID:</td>
				<td>${ambulanceBill.receipt.id}</td>
			</tr>
		</table>
		<table width="100%" border="1">
			<tr>  <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --Adjust allignment in table-->
				<th align="center">Ambulance</th>
				<th align="center">Patient Name</th>
				<th align="center">Receipt Number</th>
				<th align="center">Number Of trip</th>
				<th align="center">Origin</th>
				<th align="center">Destination</th>
				<th align="center">Amount</th>
			</tr>
			<c:forEach items="${ambulanceBill.billItems}" var="bill">
				<tr>
					<td>${bill.name}</td>
					<td align="right">${bill.patientName}</td>
					<td align="right">${bill.receiptNumber}</td>
					<td align="right">${bill.numberOfTrip}</td>
					<td align="right">${bill.origin}</td>
					<td align="right">${bill.destination}</td>
					<td align="right">${bill.amount}</td>
				</tr>
			</c:forEach>
			<tr> <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --Adjust allignment in table-->
				<td colspan="6" align="right"><b>Total</td>
				<td align="right">${ambulanceBill.amount}</td>
			</tr>
		</table>
		<br>
		<form method="POST" id="billForm">
			<center>
				<c:if test="${ambulanceBill.voided==false }">
					<input type="button"
						value='<spring:message code="billing.print" />'
						onClick="printDiv();" />&nbsp;&nbsp;</c:if>
				<a href="#" onclick="javascript:jQuery('#billContainer').hide();">Hide</a>
			</center>
		</form>
	</div>

	<!-- PRINT DIV -->

	<div id="printDiv" class="hidden"
		style="margin: 10px auto; width: 981px; font-size: 1.5em; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif;">
		<img
			src="${pageContext.request.contextPath}/moduleResources/billing/HEADEROPDSLIP.jpg"
			width="981" height="170"></img>
		<table>
			<tr>
				<td>Driver name:</td>
				<td>${driver.name }</td>
			</tr>
			<tr>
				<td>Date:</td>
				<td><openmrs:formatDate date="${ambulanceBill.createdDate}"
						type="textbox" />
				</td>
			</tr>
			<tr>
				<td>Bill ID:</td>
				<td>${ambulanceBill.receipt.id}</td>
			</tr>
		</table>
		<table width="100%" border="1">
			<thead>
				<th align="center">Ambulance</th>
				<th align="center">Patient Name</th>
				<th align="center">Receipt Number</th>
				<th align="center">Number Of trip</th>
				<th align="center">Origin</th>
				<th align="center">Destination</th>
				<th align="center">Amount</th>
			</thead>
			<c:forEach items="${ambulanceBill.billItems}" var="bill">
				<tr>
					<td>${bill.name}</td>
					<td align="right">${bill.patientName}</td>
					<td align="right">${bill.receiptNumber}</td>
					<td align="right">${bill.numberOfTrip}</td>
					<td align="right">${bill.origin}</td>
					<td align="right">${bill.destination}</td>
					<td align="right">${bill.amount}</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="6">Total</td>
				<td align="right">${ambulanceBill.amount}</td>
			</tr>
		</table>
		<br> <span style="font-size: 1.5em">Total Amount:</span> <span
			id="totalValue" style="font-size: 1.5em"></span> <br />
		<br />
		<br />
		<br />
		<br />
		<br /> <span style="float: right; font-size: 1.5em">Signature
			of billing clerk/ Stamp</span>
	</div>

	<!-- END PRINT DIV -->
</c:if>


<!--List old bills -->
<div> 
	<c:if test="${not empty ambulanceBills}"> <!-- Sept 22,2012 -- Sagar Bele -- Issue 387 --update title-->
		<span class="boxHeader">List of Ambulance bills</span>
		<table class="box">
			<thead>
				<th>#</th>
				<th>Bill Name</th>
				<th>Action</th>
			</thead>
			<c:forEach items="${ambulanceBills}" var="ambulanceBill"
				varStatus="varStatus">
				<tr
					class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } <c:if test="${ambulanceBill.voided}">retired </c:if> '>
					<td><c:out
							value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }" />
					</td>
					<td><c:choose>
							<c:when
								test="${ ambulanceBill.voided == false && (  ambulanceBill.printed == false || ( ambulanceBill.printed == true && canEdit == true ))}">
								<a
									href="${pageContext.request.contextPath}/module/billing/editAmbulanceBill.form?ambulanceBillId=${ambulanceBill.ambulanceBillId}&driverId=${driver.driverId}">Bill
									ID <b>${ambulanceBill.receipt.id}</b>, <openmrs:formatDate
										date="${ambulanceBill.createdDate }" type="textbox" />
								</a>
							</c:when>
							<c:otherwise>
		             	Bill ID <b>${ambulanceBill.receipt.id}</b>, <openmrs:formatDate
									date="${ambulanceBill.createdDate}" />
							</c:otherwise>
						</c:choose></td>
					<td><input type="button" value="View"
						onclick="javascript:window.location.href='ambulanceBill.list?driverId=${driver.driverId}&ambulanceBillId=${ambulanceBill.ambulanceBillId}'" />
					</td>
				</tr>

			</c:forEach>
			<tr class="paging-container">
				<td colspan="3"><%@ include file="../paging.jsp"%></td>
			</tr>
		</table>
	</c:if>

	<input type="hidden" id="total" value="${ambulanceBill.amount}">

	<script>
function printDiv()
{
  	jQuery("div#printDiv").printArea({mode:"popup",popClose:true});
	jQuery("#billForm").submit();
}

jQuery(document).ready(function(){
	jQuery("#totalValue").html(toWords(jQuery("#total").val()));
});
</script>

	<%@ include file="/WEB-INF/template/footer.jsp"%>
</div>