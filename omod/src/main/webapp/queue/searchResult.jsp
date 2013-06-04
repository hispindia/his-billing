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
 *  author: ghanshyam
 *  date: 3-june-2013
 *  issue no: #1632
--%>

<c:choose>
	<c:when test="${not empty patientListt}">
		<table style="width: 100%">
			<tr>
				<td><b>Identifier</b></td>
				<td><b>Name</b></td>
				<td><b>Age</b></td>
				<td><b>Gender</b></td>
				<td><b>Birthdate</b></td>
				<td><b>Relative Name</b></td>
				<td><b>Phone number</b></td>
			</tr>
				<c:forEach items="${patientListt}" var="patient" varStatus="varStatus">
				<tr
					class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } patientSearchRow'
					onclick="PATIENTSEARCHRESULTT.visit(${patient.patientId});">
					<td>${patient.identifier}</td>
					<td>${patient.givenName} ${patient.middleName}
						${patient.familyName}</td>
					<td><c:choose>
							<c:when test="${patient.age == 0}">&lt 1</c:when>
							<c:otherwise>${patient.age}</c:otherwise>
						</c:choose></td>
					<td><c:choose>
							<c:when test="${patient.gender eq 'M'}">
								<img src="${pageContext.request.contextPath}/images/male.gif" />
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/images/female.gif" />
							</c:otherwise>
						</c:choose></td>
					<td><openmrs:formatDate date="${patient.birthdate}" /></td>
					
				</tr>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
	No Patient founddddddddddddddddddddd.
	</c:otherwise>
</c:choose>