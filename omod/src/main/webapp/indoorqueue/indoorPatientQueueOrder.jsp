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
 *  date: 16-Dec-2013
 *  Requirement: Indoor Patient Billing
--%>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="Indoor billing queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<%@ include file="../includes/js_css.jsp"%>
<%@ include file="../indoorqueue/indoorBillingQueueHeader.jsp"%>

<h2>Indoor Patient Queue for Test Order</h2>	
<br />

<script type="text/javascript">
	//get context path in order to build controller url
	function getContextPath(){		
		pn = location.pathname;
		len = pn.indexOf("/", 1);				
		cp = pn.substring(0, len);
		return cp;
	}
	
	$(function() {
		$("select#ipdWard").multiselect({noneSelectedText: "Select ipd ward"});
		$("select#doctor").multiselect({noneSelectedText: "Select treating doctor"});
		$('#fromDate').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
		$('#toDate').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
	});
	
	// get queue
	function getBillingQueue(){
		var ipdWardString = jQuery("#ipdWardString").val();
		var searchPatient = jQuery("#searchPatient").val();
		var doctorString = jQuery("#doctorString").val();
		var fromDate = jQuery("#fromDate").val();
		var toDate = jQuery("#toDate").val();
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/billing/patientsearchindoorbillingqueue.form",
			data : ({
				ipdWardString			: ipdWardString,
				searchPatient			: searchPatient,
				doctorString			: doctorString,
				fromDate				: fromDate,
				toDate					: toDate
			}),
			success : function(data) {
				jQuery("#billingqueue").html(data);	
			},
			
		});
	}
</script>

<div class="boxHeader">
	<strong>Patient Queue for Test Order</strong>
</div>
<div class="box">
	<table >
		<tr valign="top">
			<td><spring:message code="billing.patient.search"/></td>
			<td>
				<input type="text" name="searchPatient" id="searchPatient" value="${searchPatient }"/>
			</td>
			<td><spring:message code="billing.ipdWard.name"/></td>
			<td>
				<select id="ipdWard"  name="ipdWard" multiple="multiple" style="width: 150px;" size="10">
					<option value=""></option>
					<c:if test="${not empty listIpd }">
			  			<c:forEach items="${listIpd}" var="ipd" >
			          			<option title="${ipd.answerConcept.name}"   value="${ipd.answerConcept.id}">
			          			${ipd.answerConcept.name}
			          			</option> 
			          			<c:if test="${not empty ipdWard}">
			          				<c:forEach items="${ipdWard}" var="x" >
			          				    <c:if test="${x ==  ipd.answerConcept.id}">
			          				    	selected
			          				    </c:if>
			          				</c:forEach>
			          			</c:if>
			       		</c:forEach>
		       		</c:if>
	  			</select> 
	  		</td>
	  		<td><spring:message code="billing.doctor.name"/></td>	
	  		<td>
				<select id="doctor"  name="doctor" multiple="multiple" style="width: 150px;" size="10">
					<option value=""></option>
					<c:if test="${not empty listDoctor }">
			  			<c:forEach items="${listDoctor}" var="doct" >
			          			<option title="${doct.givenName}"   value="${doct.id}">
			          			${doct.givenName}
			          			</option> 
			          			<c:if test="${not empty doctor}">
			          				<c:forEach items="${doctor}" var="x" >
			          				    <c:if test="${x ==  doct.id}">
			          				    	selected
			          				    </c:if>
			          				</c:forEach>
			          			</c:if>
			       		</c:forEach>
		       		</c:if>
	  			</select> 
	  		</td>	
			<td><spring:message code="billing.fromDate"/></td>
			<td><input type="text" id="fromDate" class="date-pick left" readonly="readonly" style="width: 80px;" name="fromDate" value="${fromDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
			<td><spring:message code="billing.toDate"/></td>
			<td><input type="text" id="toDate" class="date-pick left" readonly="readonly" style="width: 80px;" name="toDate" value="${toDate}" title="Double Click to Clear" ondblclick="this.value='';"/></td>
			<td ><input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all" value="Search" style="font-size: 10px" onclick="getBillingQueue();"/></td>
			
			<input type="hidden" id="ipdWardString" name="ipdWardString" value="${ipdWardString }"/>
			<input type="hidden" id="doctorString" name="doctorString" value="${doctorString }"/>
	    </tr>
</table>
<div id="billingqueue">
</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>