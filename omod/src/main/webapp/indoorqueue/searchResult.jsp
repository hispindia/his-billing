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
 *  date: 17-Dec-201
 *  Requirement: Indoor Patient Billing
--%>
<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="Indoor billing queue" otherwise="/login.htm" redirect="index.htm" />

<hr noshade size=1 color="#1aac9b"><br/>
<table cellpadding="5" cellspacing="0" width="100%" id="queueList">
	<tr align="center" >
		<th>#</th>
		<th><spring:message code="billing.admissionDate"/></th>
		<th><spring:message code="billing.patient.patientId"/></th>
		<th><spring:message code="billing.patient.patientName"/></th>
		<th><spring:message code="billing.patient.age"/></th>
		<th><spring:message code="billing.patient.gender"/></th>
		<th><spring:message code="billing.patient.admissionWard"/></th>
		<th><spring:message code="billing.patient.bedNumber"/></th>
		<th><spring:message code="billing.patient.admissionBy"/></th>
		<th><spring:message code="billing.patient.action"/></th>
	</tr>
	<tr> </tr>
	
	<c:choose>
	<c:when test="${not empty listPatientAdmitted}">
	<c:forEach items="${listPatientAdmitted}" var="queue" varStatus="varStatus">
		<tr  align="center" class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } ' >
			<td><c:out value="${(( pagingUtil.currentPage - 1  ) * pagingUtil.pageSize ) + varStatus.count }"/></td>	
			<td><openmrs:formatDate date="${queue.admissionDate}" type="textbox"/></td>
			<td>${queue.patientIdentifier}</td>
			<td>${queue.patientName}</td>
			<td>${queue.age}</td>
			<td>${queue.gender}</td>
			<td width="50">${queue.admittedWard.name}</td>
			<td>${queue.bed}</td>
			<c:set var="person" value="${queue.ipdAdmittedUser.person }"/>
			<td width="50">${person.givenName} ${person.middleName } ${person.familyName }</td>
			<td>
				<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="Add Bill" 
					onclick="window.location.href='addPatientServiceBillForBD.form?patientId=${queue.patient.patientId}&billType=paid'"/>
		    	<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" value="View Bill" onclick="ADMITTED.transfer('${queue.id}');"/>
		    	<!-- <div id="printArea${queue.id}" style="display:none; margin: 10px auto; width: 981px; font-size: 1.5em;font-family:'Dot Matrix Normal',Arial,Helvetica,sans-serif;">
		    	
			    	<table width="100%" >
						<tr><td><spring:message code="billing.patient.patientName"/>:&nbsp;<strong>${queue.patientName }</strong></td></tr>
						<tr><td><spring:message code="billing.patient.patientId"/>:&nbsp;<strong>${queue.patientIdentifier}</strong></td></tr>
						<tr><td><spring:message code="billing.patient.age"/>:&nbsp;<strong>${queue.age }</strong></td></tr>
						<tr><td><spring:message code="billing.patient.gender"/>:&nbsp;<strong>${queue.gender }</strong></td></tr>
						<tr><td>${mapRelationType[queue.id]}:&nbsp;${mapRelationName[queue.id]}</td></tr>
						<tr><td colspan="4"><spring:message code="billing.patient.homeAddress"/>: ${address }</td></tr>
						<tr></tr>
						<tr><td ><spring:message code="billing.patient.admittedWard"/>:<strong> ${queue.admittedWard.name}</strong></td></tr>
						<tr><td ><spring:message code="billing.patient.treatingDoctor"/>:<strong> ${queue.ipdAdmittedUser.givenName}</strong></td></tr>
						<tr><td ><spring:message code="billing.patient.bedNumber"/>: <strong>${queue.bed }</strong></td></tr>
						<tr><td ><spring:message code="billing.patient.date/time"/>: <strong>${dateTime }</strong></td></tr>
					</table>
				</div> -->
			</td>
		</tr>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<tr align="center" >
			<td colspan="6">No patient found</td>
		</tr>
	</c:otherwise>
	</c:choose>
</table>