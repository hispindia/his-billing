<%--
 *  Copyright 2014 Society for Health Information Systems Programmes, India (HISP India)
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
<%@ include file="../includes/js_css.jsp"%>
<openmrs:require privilege="View Bills" otherwise="/login.htm" />

<script type="text/javascript">
	jQuery(document).ready(
			function() {
			
			if(${requestForDischargeStatus}==1){
			jQuery("#waiverAmt").show();
			}
			else{
			jQuery("#waiverAmt").hide();
			}
			
});
</script>
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
		margin-top: 40px;
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
<%@ include file="../queue/billingQueueHeader.jsp"%>
<table id="myTablee" class="tablesorter" class="thickbox">
	<thead>
		<tr align="center">
			<th>S.No</th>
			<th>Service</th>
			<th>Unit Price</th>
			<th>Amount</th>
		</tr>
	</thead>
	<tbody>
		<c:set var="index" value="1"/>  
		<c:forEach var="bill" items="${billList}" varStatus="statusOuter">
		<c:forEach var="item" items="${bill.billItems}" varStatus="statusInner">
			<c:choose>
				<c:when test="${index mod 2 == 0}">
					<c:set var="klass" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="klass" value="even" />
				</c:otherwise>
			</c:choose>
			<tr class="${klass}">
				<td>${index}</td>
				<td>${item.name}</td>
				<td>${item.unitPrice}</td>
				<td>${item.amount}</td>
				<c:set var="index" value="${index+1}"/>  
			</tr>
		</c:forEach>
		</c:forEach>
	</tbody>
</table>
<table>
<form method="post" id="billListForIndoorPatient">
<div id="waiverAmt">
<b>Waiver Amount:</b><input type="text" id="waiverAmount" name="waiverAmount">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Payment Mode:</b>
			<select id="paymentMode" name="paymentMode">
					<option value="Cash">Cash</option>
					<option value="Card">Card</option>
				</select>
			

</div>
<div style="text-align:center"> 
<c:choose>
<c:when test="${requestForDischargeStatus== 1}"> 
<input type="submit" id="billSubmitForIndoorPatient" name="billSubmitForIndoorPatient" value="Submit">
</c:when>
</c:choose>
</div>
</form>
</table>
<%@ include file="/WEB-INF/template/footer.jsp"%>
</div>