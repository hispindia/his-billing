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
 *  
--%>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="Indoor billing queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<%@ include file="../includes/js_css.jsp"%>
<%@ include file="../queue/billingQueueHeader.jsp"%>
<script type="text/javascript">
function addBill(patientId,encounterId){
var paid="paid";
window.location.href = openmrsContextPath + "/module/billing/addPatientServiceBillForBD.form?patientId="+patientId+"&encounterId="+encounterId+"&billType="+paid;
}

function viewBill(patientId,encounterId,admissionLogId,requestForDischargeStatus){
window.location.href = openmrsContextPath + "/module/billing/patientServiceBillForBD.list?patientId="+patientId+"&encounterId="+encounterId+"&admissionLogId="+admissionLogId+"&requestForDischargeStatus="+requestForDischargeStatus;
}
</script>
<h2>Indoor Patient List</h2>	
<br />

<div class="boxHeader">
	<strong>Indoor Patient List</strong>
</div>
<table cellpadding="5" cellspacing="0" width="100%" id="queueList">
<tr align="center" >
	<th>#</th>
	<th>Admission Advised On</th>
	<th>Patient ID</th>
	<th>Name</th>
	<th>Age</th>
	<th>Gender</th>
	<th>Admission Ward</th>
	<th>Admission By</th>
	<th>Action</th>
</tr>
<c:set var="index" value="1"/> 
<c:choose>
<c:when test="${not empty listIndoorPatient2}">
<c:forEach items="${listIndoorPatient2}" var="pAdmissionLog" varStatus="varStatus">
	<tr  align="center" class='${index % 2 == 0 ? "oddRow" : "evenRow" } ' >
		<td><c:out value="${index}"/></td>
		<td><openmrs:formatDate date="${pAdmissionLog.admissionDate }" type="textbox"/></td>	
		<td>${pAdmissionLog.patientIdentifier}</td>
		<td>${fn:replace(pAdmissionLog.patientName ,',',' ')}</td> 
		<td>
			${pAdmissionLog.age }
		</td>
		<td>${pAdmissionLog.gender}</td>
		<td>${pAdmissionLog.admissionWard.name}</td>
		
		<c:set var="person" value="${pAdmissionLog.opdAmittedUser.person }"/>
		<td>${person.givenName}${person.familyName }  ${fn:replace(person.middleName ,',',' ')} </td>
		<td><input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  value="Add Bill" onclick="addBill('${pAdmissionLog.patient.id}','${pAdmissionLog.ipdEncounter.id}');"/>
		<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  value="View Bill" onclick="viewBill('${pAdmissionLog.patient.id}','${pAdmissionLog.ipdEncounter.id}','${pAdmissionLog.id}','${pAdmissionLog.requestForDischargeStatus}');"/></td>
		<c:set var="index" value="${index+1}"/>	
</tr>
</c:forEach>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>
<c:choose>
<c:when test="${not empty listIndoorPatient1}">
<c:forEach items="${listIndoorPatient1}" var="pAdmission" varStatus="varStatus">
	<tr  align="center" class='${index % 2 == 0 ? "oddRow" : "evenRow" } ' >
		<td><c:out value="${index}"/></td>
		<td><openmrs:formatDate date="${pAdmission.admissionDate }" type="textbox"/></td>	
		<td>${pAdmission.patientIdentifier}</td>
		<td>${fn:replace(pAdmission.patientName ,',',' ')}</td>
		<td>
			${pAdmission.age }
		</td>
		<td>${pAdmission.gender}</td>
		<td>${pAdmission.admissionWard.name}</td>
		
		<c:set var="person" value="${pAdmission.opdAmittedUser.person }"/>
		<td>${person.givenName} ${person.familyName }  ${fn:replace(person.middleName ,',',' ')}</td>
		<td><input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  value="Add Bill" onclick="addBill('${pAdmission.patient.id}','${pAdmission.ipdEncounter.id}');"/>
		<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all"  value="View Bill" onclick="viewBill('${pAdmission.patient.id}','${pAdmission.ipdEncounter.id}','${pAdmission.id}',0);"/></td>
		<c:set var="index" value="${index+1}"/> 
</tr>
</c:forEach>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>	
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>