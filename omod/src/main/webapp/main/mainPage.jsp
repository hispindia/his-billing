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
<%@ include file="../includes/js_css.jsp"%>

<openmrs:require privilege="View Bills" otherwise="/login.htm"
	redirect="/module/billing/main.form" />
<spring:message var="pageTitle" code="billing.patient.find" scope="page" />
<openmrs:globalProperty key="hospitalcore.hospitalName"
	defaultValue="ddu" var="hospitalName" />
<br />
<p>
	<b><a href="searchDriver.form"><spring:message
				code="billing.ambulance" />
	</a>
	</b>&nbsp; | &nbsp; <b><a href="searchCompany.form"><spring:message
				code="billing.tender" />
	</a>
	</b>&nbsp; | &nbsp; <b><a href="miscellaneousServiceBill.list"><spring:message
				code="billing.miscellaneousService" />
	</a>
	</b>
	 <!-- ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue -->
	&nbsp; | &nbsp; <b><a href="billingqueue.form"><spring:message
				code="billing.billingqueue" />
	</a>
	</b>
</p>
<br />
<openmrs:require privilege="View Patients" otherwise="/login.htm"
	redirect="/index.htm" />

<script type="text/javascript">

	jQuery(document).ready(function(){
		jQuery("#searchbox").showPatientSearchBox({
			searchBoxView: "${hospitalName}/default",
			resultView: "/module/billing/patientsearch/${hospitalName}/main",
			rowPerPage: 15,
			beforeNewSearch: function(){
				jQuery("#patientSearchResultSection").hide();
			},
			success: function(data){
				jQuery("#patientSearchResultSection").show();
			}
		});
		
		/**
		* June 6th 2012: Thai Chuong - Supported #247
		*/
		jQuery("#billId", "#billSearch").keyup(function(event){				
			if(event.keyCode == 13){	
				jQuery("#billSearch").ajaxSubmit();
			}
		});
	});
</script>






<b class="boxHeader"><spring:message code="Patient.find" />
</b>
<div class="box" id="searchbox"></div>
<br />

<!-- June 6th 2012: Thai Chuong Supported new requirement #247 -->
<form id="billSearch" action="${pageContext.request.contextPath}/module/hospitalcore/findBill.htm" method="GET">
	<b class="boxHeader"><spring:message code="billing.Patient.find.byBillId" />
	</b>
	<div class="box" id="searchboxBillId">
		<table cellspacing="10">
			<tbody>
				<tr>
					<td>Bill Id </td>
					<td>
						<input id="billId" name="billId" style="width:300px;">
					</td>
					<td id="searchLoaderBillId"></td>
				</tr>
			</tbody>
		</table>
	</div>
	<br />
</form>
<!-- End #247 -->

<div id="patientSearchResultSection" style="display: none;">
	<div class="boxHeader">Found Patients</div>
	<div class="box" id="patientSearchResult"></div>
</div>
<%
/**
* Use for search bill by BillId
* Author: Thai Chuong June 10th 2012
* Supported issue #247 & #254
* Search bill by Bill Id
*/
if (request.getParameter("Found") == null) {
} else {
    out.println("<font color='red'><b>"+request.getParameter("Found")+"</b>!</font>");
}
%>
<%@ include file="/WEB-INF/template/footer.jsp" %>