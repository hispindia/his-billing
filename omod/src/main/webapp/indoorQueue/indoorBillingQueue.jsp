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

<openmrs:require privilege="Indoor billing queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<%@ include file="../includes/js_css.jsp"%>

<script type="text/javascript">
// get context path in order to build controller url
	function getContextPath(){		
		pn = location.pathname;
		len = pn.indexOf("/", 1);				
		cp = pn.substring(0, len);
		return cp;
	}
</script>


<script type="text/javascript">
function addBill(patientId,encounterId){
var paid="paid";
var indoor="indoor";
window.location.href = openmrsContextPath + "/module/billing/addPatientServiceBillForBD.form?patientId="+patientId+"&encounterId="+encounterId+"&typeOfPatient="+indoor+"&billType="+paid;
}

function viewBill(patientId,encounterId,admissionLogId,requestForDischargeStatus){
var indoor="indoor";
window.location.href = openmrsContextPath + "/module/billing/patientServiceBillForBD.list?patientId="+patientId+"&encounterId="+encounterId+"&typeOfPatient="+indoor+"&admissionLogId="+admissionLogId+"&requestForDischargeStatus="+requestForDischargeStatus;
}
</script>

<br />

<div class="boxHeader">
	<strong>Indoor Patient List</strong>
</div>
<c:choose>
	<c:when test="${not empty listIndoorPatient2}">

<table cellpadding="5" cellspacing="0" width="100%" id="queueList">
<tr align="center" >
	<th>S.No</th>
	<th>Admission Date</th>
	<th>Patient ID</th>
	<th>Name</th>
	<th>Age</th>
	<th>Gender</th>
	<th>Admission Ward</th>
	<th>Admission By</th>
    <th>Abscond</th>
	<th>Action</th>
</tr>
<c:set var="index" value="1"/> 
<c:choose>
<c:when test="${not empty listIndoorPatient2}">
<c:forEach items="${listIndoorPatient2}" var="pAdmissionLog" varStatus="varStatus">
	<tr  align="center" class='${index % 2 == 0 ? "oddRow" : "evenRow" } ' >
		<td><c:choose>
					<c:when test="${pagingUtil.currentPage != 1}">
						${varStatus.count +
							(pagingUtil.currentPage-1)*pagingUtil.pageSize}
					</c:when>
					<c:otherwise>
						${varStatus.count}
					</c:otherwise>
					</c:choose></td>
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
        <td>
        <c:choose>
		    <c:when test="${pAdmissionLog.absconded == 1}"><font color="#FF0000"> Yes</Font>
            </c:when>
            <c:otherwise> No
            </c:otherwise>
            </c:choose>
         </td>
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
		<td><c:choose>
					<c:when test="${pagingUtil.currentPage != 1}">
						${varStatus.count +
							(pagingUtil.currentPage-1)*pagingUtil.pageSize}
					</c:when>
					<c:otherwise>
						${varStatus.count}
					</c:otherwise>
					</c:choose></td>
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
        <td>
        <c:choose>
		    <c:when test="${pAdmissionLog.absconded == 1}"><font color="#FF0000"> Yes</Font>
            </c:when>
            <c:otherwise> No
            </c:otherwise>
            </c:choose>
         </td>
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
	</c:when>
	<c:otherwise>
	No Patients Found.
	</c:otherwise>
</c:choose>

<div id='paging'>
	<a style="text-decoration: none" href='javascript:getBillingQueue(1);'>&laquo;&laquo;</a>
	<a style="text-decoration: none"
		href="javascript:getBillingQueue(${pagingUtil.prev});">&laquo;</a>
	${pagingUtil.currentPage} / <b>${pagingUtil.numberOfPages}</b> <a
		style="text-decoration: none"
		href="javascript:getBillingQueue(${pagingUtil.next});">&raquo;</a> <a
		style="text-decoration: none"
		href='javascript:getBillingQueue(${pagingUtil.numberOfPages});'>&raquo;&raquo;</a>
		<br/>
		
</div>

