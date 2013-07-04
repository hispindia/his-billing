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

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Test order queue" otherwise="/login.htm" redirect="/module/billing/main.form" />

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
	
	PATIENTSEARCHRESULT = {
		oldBackgroundColor: "",
		
		/** Click to view patient info */
		visit: function(patientId,date){	
		var dat = date.toString(); 
		//ghanshyam 25-02-2013 New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module	
			window.location.href = openmrsContextPath + "/module/billing/listoforder.form?patientId=" + patientId + "&date=" + dat;
		}
	};

	jQuery(document).ready(function(){
	
		// hover rows
		jQuery(".patientSearchRow").hover(
			function(event){					
				obj = event.target;
				while(obj.tagName!="TR"){
					obj = obj.parentNode;
				}
				PATIENTSEARCHRESULT.oldBackgroundColor = jQuery(obj).css("background-color");
				jQuery(obj).css("background-color", "#00FF99");									
			}, 
			function(event){
				obj = event.target;
				while(obj.tagName!="TR"){
					obj = obj.parentNode;
				}
				jQuery(obj).css("background-color", PATIENTSEARCHRESULT.oldBackgroundColor);				
			}
		);
		
	});
</script>

<c:choose>
	<c:when test="${not empty patientList}">
		<table style="width: 100%">
			<tr>
				<td><b>S.No</b></td>
				<td><b>Identifier</b></td>
				<td><b>Name</b></td>
				<td><b>Age</b></td>
				<td><b>Gender</b></td>
				<td><b>Birthdate</b></td>
				<!--  
				<td><b>Relative Name</b></td>
				<td><b>Phone number</b></td>
				-->
			</tr>
				<c:forEach items="${patientList}" var="patient" varStatus="varStatus">
				<tr
					class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" } patientSearchRow'
					onclick="PATIENTSEARCHRESULT.visit(${patient.patientId},'${date}');">
					<td>
					<c:choose>
					<c:when test="${pagingUtil.currentPage != 1}">
						${varStatus.count +
							(pagingUtil.currentPage-1)*pagingUtil.pageSize}
					</c:when>
					<c:otherwise>
						${varStatus.count}
					</c:otherwise>
					</c:choose>
					</td>
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
	No Result Found.
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
</div>