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
<openmrs:globalProperty var="userLocation" key="hospital.location_user" defaultValue="false"/>
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

<c:forEach items="${errors}" var="error">
	<span class="error"><spring:message
			code="${error.defaultMessage}" text="${error.defaultMessage}" /> </span>
</c:forEach>
<c:if test="${not empty bill}">
	<div id="billContainer" style="margin: 10px auto; width: 981px;">
		<table align="center">
			<tr>
				<td>Bill ID:</td>
				<td>${bill.receipt.id}</td>
			</tr>
			<tr>
				<td>Date/ Time:</td>
				<td><openmrs:formatDate date="${bill.createdDate}"
						type="textbox" /></td>
			</tr>
			
			<tr>
				<td>Name:</td>
				<td>${patient.givenName}&nbsp;${patient.familyName}&nbsp;${fn:replace(patient.middleName,',',' ')}&nbsp;&nbsp;
					</td>
			</tr>
			<tr>
				<td>Patient ID:</td>
				<td>${patient.patientIdentifier.identifier}</td>
			</tr>
			<c:if test="${bill.voided==true }">
				<tr>
					<td>Bill Description:</td>
					<td>${bill.description}</td>
				</tr>
			</c:if>
			<tr>
				<td>Age:</td>
				<td>${age}</td>
			</tr>
			<tr>
				<td>Gender:</td>
				<td>${gender}</td>
			</tr>
			<tr>
				<td>Patient Category:</td>
				<td>${category}</td>
			</tr>
			<tr>
				<td>File Number:</td>
				<td>${fileNumber}</td>
			</tr>
			
		</table>
		<table width="100%" border="1">
			<tr>
				<th align="center">Service Name</th>
				<th align="center">Price (KSh)</th>
				<th align="center">Quantity</th>
				<th align="center">Amount</th>
			</tr>
			<c:forEach items="${bill.billItems}" var="item" varStatus="status">
				<c:if test="${bill.voidedDate==null}">
				<c:choose>
				<c:when test="${item.name != 'INPATIENT DEPOSIT'}">
					<tr>
						<td>${item.name}</td>
						<td align="right">${item.unitPrice}</td>
						<td align="right">${item.quantity}</td>
						<td class="printfont" height="20" align="right" style=""><c:choose>
								<c:when test="${not empty item.actualAmount}">
									<c:choose>
										<c:when test="${item.actualAmount eq item.amount}">
                                        <c:choose>
                                        <c:when test="${item.voidedDate != null}">
									<span style="text-decoration: line-through;">${item.amount}</span>
                                    </c:when>
                                    <c:otherwise>
                                    ${item.amount}</c:otherwise>
                                    </c:choose>
								</c:when>
										<c:otherwise>
											<span style="text-decoration: line-through;">${item.amount}</span>
											<b>${item.actualAmount}</b>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
							${item.amount}
						</c:otherwise>

							</c:choose></td>
					</tr>
				</c:when>
				</c:choose>
				</c:if>
			</c:forEach>

		<c:set var="initialtotal" value="0"/>  
		<c:forEach items="${bill.billItems}" var="item" varStatus="status">
			<c:if test="${bill.voidedDate==null}">
			<c:choose>
				<c:when test="${item.name == 'INPATIENT DEPOSIT'}">
					<c:set var="initialtotal" value="${item.amount + initialtotal}"/>  
				</c:when>
			</c:choose>
			</c:if>
		</c:forEach>
			<tr>
				<td colspan="3" align='right'><b>Total 
				</td>
				<td align="right"><c:choose>
						<c:when test="${not empty bill.actualAmount}">
							<c:choose>
								<c:when test="${bill.actualAmount eq bill.amount}">
									<c:choose>
										<c:when test="${bill.voided==true }">
											<span style="text-decoration: line-through;"> <b>${bill.amount}</b>
											</span>
										</c:when>
										<c:otherwise>
											<b>${bill.amount-initialtotal}</b>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<span style="text-decoration: line-through;">${bill.amount-initialtotal}</span>
									<c:choose>
										<c:when test="${bill.voided==true }">
											<span style="text-decoration: line-through;"> <b>${bill.actualAmount-initialtotal}</b>
											</span>
										</c:when>
										<c:otherwise>
											<b>${bill.actualAmount-initialtotal}</b>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							${bill.amount-initialtotal}
						</c:otherwise>
					</c:choose></td>
			</tr>
			<c:forEach items="${bill.billItems}" var="item" varStatus="status">
				<c:if test="${bill.voidedDate==null}">
				<c:choose>
				<c:when test="${item.name == 'INPATIENT DEPOSIT'}">
					<tr>
						<td colspan="3" align='right'>Amount Paid as Advance</td>
						<td class="printfont" height="20" align="right" style=""><c:choose>
								<c:when test="${not empty item.actualAmount}">
									<c:choose>
										<c:when test="${item.actualAmount eq item.amount}">
									${item.amount}
								</c:when>
										<c:otherwise>
											<span style="text-decoration: line-through;">${item.amount}</span>
											<b>${item.actualAmount}</b>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
							${item.amount}
						</c:otherwise>

							</c:choose></td>
					</tr>
				</c:when>
				</c:choose>
				</c:if>
			</c:forEach>
			<tr>
				<td colspan="3" align='right'><b>Total Amount Paid as Advance</td>
				<td align="right"><b>${initialtotal}</b></td>
			</tr>
			
			<tr>
				<td colspan="3" align='right'><b>Waiver Amount(If any)</td>
				<td align="right"><b>${bill.waiverAmount}</b></td>
			</tr>
			<c:if test="${bill.rebateAmount > 0}">
            <tr>
				<td colspan="3" align='right'><b>Rebate Amount</td>
				<td align="right"><b>${bill.rebateAmount}</b></td>
			</tr>
            </c:if>
            
			<tr>
				<td colspan="3" align='right'><b>Net Amount</td>
				<td align="right">
                <b>${bill.actualAmount - billItem.actualAmount - bill.waiverAmount -2*initialtotal}</b></td>
			</tr>

			
			
		</table>
		<br>
		<form method="POST" id="billForm">
			<center>
				<input type="button" value='<spring:message code="billing.print" />'
					onClick="printDiv2();" />
			</center>
		</form>
	</div>

	<!-- PRINT DIV -->

	<div id="printDiv" class="hidden"
		style="width: 1280px; font-size: 0.8em">
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
		<input type="hidden" id="contextPath"
			value="${pageContext.request.contextPath}" /> <img
			class="donotprint"
			src="${pageContext.request.contextPath}/moduleResources/billing/HEADEROPDSLIP.jpg"
			width="981" height="212"></img>
		<center><img width="100" height="100" align="center" title="OpenMRS" alt="OpenMRS" src="${pageContext.request.contextPath}/moduleResources/billing/kenya_logo.bmp"><center>	
		<table class="spacer" style="margin-left: 60px;">
			<tr><h3><center><u><b>${userLocation} </b></u></center></h3></tr>
			<tr><h5><center>CASH RECEIPT</center></h5></tr>
			<tr>
				<td>Bill ID:</td>
				<td>${bill.receipt.id}</td>
			</tr>
			<tr>
				<td>Date/ Time:</td>
				<td align="left">${bill.createdDate}</td>
			</tr>
			<tr>
				<td>Name:</td>
				<td colspan="3">${patient.givenName}&nbsp;${patient.familyName}&nbsp;${fn:replace(patient.middleName ,',',' ')}&nbsp;&nbsp;
					</td>
			</tr>
			<tr>
				<td>Patient ID:</td>
				<td colspan="3">${patient.patientIdentifier}</td>
			</tr>

			<c:if test="${bill.voided==true }">
				<tr>
					<td>Bill Description:</td>
					<td>${bill.description}</td>
				</tr>
			</c:if>
			<tr>
				<td>Age:</td>
				<td>${age}</td>
			</tr>
			<tr>
				<td>Gender:</td>
				<td>${gender}</td>
			</tr>
			<tr>
				<td>Patient Category:</td>
				<td>${category}</td>
			</tr>
			<tr>
				<td>File Number:</td>
				<td>${fileNumber}</td>
			</tr>
		</table>
		<table class="printfont"
			style="margin-left: 60px; margin-top: 10px; font-family: 'Dot Matrix Normal', Arial, Helvetica, sans-serif; font-style: normal;"
			width="80%">
			<thead>
				<th class="printfont"><center>S.No</center></th>
				<th class="printfont"><center>Service Name</center></th>
				<th class="printfont"><center>Price (KSh)</center></th>
				<th class="printfont"><center>Quantity</center></th>
				<th class="printfont"><center>Amount</center></th>
			</thead>
			<c:forEach items="${bill.billItems}" var="item" varStatus="varStatus">
				<c:if test="${item.voidedDate==null}">
				<c:choose>
					<c:when test="${item.name != 'INPATIENT DEPOSIT'}">

					<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }' >
						<td><center><c:out value="${varStatus.count }"/></center></td>
						<td class="printfont" height="20">${item.name}</td>
						<td class="printfont" height="20"><center>${item.unitPrice}</center></td>
						<td class="printfont" height="20"><center>${item.quantity}</center></td>
						<td class="printfont" height="20"><center>${item.actualAmount}</center></td>
					</tr>
					</c:when>
				</c:choose>	
				</c:if>
			</c:forEach>
			
		<c:set var="initialtotal" value="0"/>  
		<c:forEach items="${bill.billItems}" var="item" varStatus="status">
			<c:if test="${item.voidedDate==null}">
			<c:choose>
				<c:when test="${item.name == 'INPATIENT DEPOSIT'}">
					<c:set var="initialtotal" value="${item.amount + initialtotal}"/>  
				</c:when>
			</c:choose>
			</c:if>
		</c:forEach>
			
			<tr>
				<td>&nbsp;</td>
				<td align="right" colspan="3"><b>Total</b></td>
				<c:choose>
					<c:when test="${bill.voided}">
						<td align="right"><span
							style="text-decoration: line-through;">${bill.actualAmount-initialtotal}</span>
						</td>

					</c:when>
					<c:otherwise>
						<td align="right">${bill.actualAmount-initialtotal}</td>
					</c:otherwise>
				</c:choose>
			</tr>
			
			<tr>
				<td>&nbsp;</td>
				<td colspan="3" align='right'><b>Amount Paid as Advance</td>
				<td align="right"><b>${initialtotal}</b></td>
			</tr>
			
			<tr>
				<td>&nbsp;</td>
				<td colspan="3" align='right'><b>Waiver Amount(If any)</td>
				<td align="right"><b>${bill.waiverAmount}</b></td>
			</tr>
			
            <c:if test="${bill.rebateAmount > 0}">
            <tr>
				<td>&nbsp;</td>
				<td colspan="3" align='right'><b>Rebate Amount</td>
				<td align="right"><b>${bill.rebateAmount}</b></td>
			</tr>
            </c:if>
            
            
            
            
			<tr>
				<td>&nbsp;</td>
				<td colspan="3" align='right'><b>Net Amount</td>
				<td align="right"><b>${bill.actualAmount - billItem.actualAmount - bill.waiverAmount -2*initialtotal}</b></td>
			</tr>
            
            
			
		</table>
		<br>
		<table class="spacer" style="margin-left: 60px;">
	<!-- 	<tr>
			<td>PAYMENT MODE :</td>
			<td><b>${paymentMode}</b></td>
		</tr> -->
		<tr>
			<td>CASHIER :</td>
			<td><b>${cashier}</b></td>
		</tr>
		</table>
		
		<br /> <br /> <br /> <br /> <br />  <span
			class="printfont" style="margin-left: 200px;">Signature of
			Billing Clerk/ Stamp</span>
	</div>


</c:if>

<!-- END PRINT DIV -->


<script>
	function printDivNoJQuery() {
		var divToPrint = document.getElementById('printDiv');
		var newWin = window
				.open('', '',
						'letf=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
		newWin.document.write(divToPrint.innerHTML);
		newWin.print();
		newWin.close();
		//setTimeout(function(){window.location.href = $("#contextPath").val()+"/getBill.list"}, 1000);	
	}
	function printDiv2() {
		var printer = window.open('', '', 'width=300,height=300');
		printer.document.open("text/html");
		printer.document.write(document.getElementById('printDiv').innerHTML);
		printer.document.close();
		printer.window.close();
		printer.print();
		jQuery("#billForm").submit();
		//alert("Printing ...");
	}
</script>

<input type="hidden" id="total" value="${bill.actualAmount - bill.waiverAmount}">

<script>
	function printDiv() {
		jQuery("div#printDiv").printArea({
			mode : "iframe"
		});
		jQuery("#billForm").submit();

		//setTimeout(function(){window.location.href = $("#contextPath").val()+"/module/billing/getBill.list"}, 1000);
	}
	jQuery(document).ready(function() {
		jQuery("#totalValue2").html(toWords(jQuery("#total").val()));
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
</div>