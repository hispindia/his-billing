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
<openmrs:globalProperty var="userLocationPoBox" key="hospital.location_PoBox" defaultValue="false"/>
<openmrs:globalProperty var="userLocationTel" key="hospital.location_Telephone" defaultValue="false"/>
<openmrs:globalProperty var="normalDelivery" key="billing.invoice.normalDelivery" defaultValue="false"/>
<openmrs:globalProperty var="general" key="billing.invoice.general" defaultValue="false"/>
<openmrs:globalProperty var="Caesarian" key="billing.invoice.Caesarian" defaultValue="false"/>
<script type="text/javascript">

			var totalAmount="0";
			var change=false;
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
    
   <script type="text/javascript">
   function resetRadios(){
	       var ele = document.getElementsByName("NHIF");
   for(var i=0;i<ele.length;i++)
      ele[i].checked = false;
   }
	  </script>
    
    <script type="text/javascript">
	
	
	function categoryChange(){  
		if(!change) 
		totalAmount=document.getElementById('tot').innerHTML;
		change=true;
				
		document.getElementById("patientCategory").value='';	
		var patientCat=document.getElementById("categoryList").value;
		var myTable = document.getElementById('myTablee');
		var lengthTa=myTable.rows.length;
		document.getElementById("rebateAmount").value=0;	
		//Assuming that PatientCat value is global one	
		if(patientCat==${nhifCatId}){
		document.getElementById("NHIFRadio").style.display='block';		
		myTable.rows[lengthTa-1].cells[4].innerHTML = '0';
		myTable.rows[lengthTa-1].cells[3].innerHTML = 'Rebate Amount';
		document.getElementById("patientCategory").value='NHIF Patient';
		document.getElementById('tot').innerHTML=totalAmount;
		document.getElementById('tot2').innerHTML=totalAmount;
		
		}
		else{			
			document.getElementById("NHIFRadio").style.display='none';	
			myTable.rows[lengthTa-1].cells[4].innerHTML = ' ';
			myTable.rows[lengthTa-1].cells[3].innerHTML = ' ';
			document.getElementById('tot').innerHTML=totalAmount;
			document.getElementById('tot2').innerHTML=totalAmount;	
			
			if(patientCat==${ChildCatId}){
				document.getElementById("patientCategory").value='CHILD LESS THAN 5 YEARS';
				document.getElementById('tot').innerHTML=totalAmount;
				document.getElementById('tot2').innerHTML=totalAmount;
				
				}
				if(patientCat==${exemptedCatId}){
				document.getElementById("patientCategory").value='EXEMPTED PATIENT';
				document.getElementById('tot').innerHTML=0;
				document.getElementById('tot2').innerHTML=0;
				
				
				
				}
				if(patientCat==${generalCatId}){
				document.getElementById("patientCategory").value='GENERAL PATIENT';
				document.getElementById('tot').innerHTML=totalAmount;
				document.getElementById('tot2').innerHTML=totalAmount;
				
				}
				
					
		}
	}
    </script>
     <script type="text/javascript">
	function nhifChange(type){
		var myTable = document.getElementById('myTablee');
		var lengthTa=myTable.rows.length;
		if(type==0){			
			myTable.rows[lengthTa-1].cells[4].innerHTML = '${general*admittedDays}';
			document.getElementById("rebateAmount").value='${general*admittedDays}';
			}
			else if(type==1){
				myTable.rows[lengthTa-1].cells[4].innerHTML='${normalDelivery}';
				document.getElementById("rebateAmount").value='${normalDelivery}';
				}
				else if(type==2){
					myTable.rows[lengthTa-1].cells[4].innerHTML='${Caesarian}';
					document.getElementById("rebateAmount").value='${Caesarian}';
					}
		}
	</script>
    
    
<%@ include file="../queue/billingQueueHeader.jsp"%>
<body onLoad="resetRadios();categoryChange();">
	<div id="billContainer" style="margin: 10px auto; width: 100%;">
		<table>

		<tr>
				<td>Patient ID:</td>
				<td>${patient.patientIdentifier.identifier}</td>
				<td style="width: 20%;">&nbsp;</td>
				<td>Ward:</td>
				<td>${ward.name}</td>
			</tr>
			<tr>
				<td>Name:</td>
				<td align="left">${patient.givenName}&nbsp;${patient.familyName}&nbsp;${fn:replace(patient.middleName,',',' ')}&nbsp;&nbsp;</td>
				<td style="width: 20%;">&nbsp;</td>
				<td align="left">Bed No:</td>
				<td>${bed}</td>

			</tr>
			<tr>
				<td>Address:</td>
				<td align="left">${address}&nbsp;&nbsp;</td>
				<td style="width: 20%;">&nbsp;</td>
				<td align="left">Doctor:</td>
				<td>${doctor}</td>
			</tr>
			<tr>
				<td>Contact Number:</td>
				<td align="left">${phone}&nbsp;&nbsp;
					</td>
				<td style="width: 20%;">&nbsp;</td>
				<td align="left">Adm Date:</td>
				<td>${admissionDate}</td>
			</tr>
			<tr>
				<td>Payer:</td>
				<td align="left">&nbsp;&nbsp;</td>
				<td style="width: 20%;">&nbsp;</td>
				<td align="left">Discharge Date:</td>
				<c:choose>
				<c:when test="${requestForDischargeStatus== 1}"> 
					<td style="width: 35%;">${curDate}</td>
				</c:when>
				</c:choose>				
			</tr>
			<tr>
				<td>Scheme Name:</td>
				<td align="left">&nbsp;&nbsp;</td>
				<td style="width: 20%;">&nbsp;</td>
				<td align="left">File Number:</td>
				<td>${fileNumber}</td>
			
					
			</tr>
			<tr>
				<td>No.of Days:</td>
				<td align="left">${admittedDays}&nbsp;&nbsp;
					</td>
			<td style="width: 20%;">&nbsp;</td>
				<td align="left">Patient Category:</td>
				<td><select id="categoryList"  name="categoryList" onChange="categoryChange()">
					<option value="">                    
						<c:forEach items="${categoryList}" var="category" >
			          			<option value="${category.answerConcept.id}">
			          			${category.answerConcept.name}
			          			</option> 
			        	</c:forEach>
			        	</option>
		       	</select>                 
                <form id="NHIFRadio">
                <br />
                <input id="nhifgeneral" name="NHIF" type="radio" value="General" onClick="nhifChange(0);"/> General &nbsp;&nbsp;&nbsp;
                <input id="nhifnormal" name="NHIF" type="radio" value="Normal Delivery" onClick="nhifChange(1);"/> Normal Delivery &nbsp;&nbsp;&nbsp;
                <input id="nhifcasarian" name="NHIF" type="radio" value="Caesarian Section"  onclick="nhifChange(2);" /> Caesarian Section &nbsp;&nbsp;&nbsp;
		       	</form>
			</tr>

		</table>

<table id="myTablee" class="tablesorter" class="thickbox">
	<thead>
		<tr align="center">
			<th>S.No</th>
			<th>Date</th>
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
			<c:choose>
				<c:when test="${item.name != 'INPATIENT DEPOSIT'}">
			<tr class="${klass}">
				<td>${index}</td>
				<td><openmrs:formatDate date="${item.createdDate}" type="textbox"/></td>
				<td>${item.name}</td>
				<td>${item.unitPrice}</td>
				<td>${item.amount}</td>
				<c:set var="index" value="${index+1}"/>  
			</tr>
				</c:when>
			</c:choose>
		</c:forEach>
		</c:forEach>
		<td></td>
		<td></td>
		<td></td>
		<td align="right"><b>ADVANCE PAYMENT</b></td>
		<c:set var="initialtotal" value="0"/>  
		<td><c:forEach var="bill" items="${billList}" varStatus="statusOuter">
		<c:forEach var="item" items="${bill.billItems}" varStatus="statusInner">
		<c:choose>
				<c:when test="${item.name == 'INPATIENT DEPOSIT'}">
				<c:set var="initialtotal" value="${item.amount + initialtotal}"/>  
						</c:when>
			</c:choose>
			</c:forEach>
		</c:forEach>
		<b>${initialtotal}</b>
		<tr>
		<td></td>
		<td></td>
		<td></td>
		<td align="right"><b>Total</b></td>
		<c:set var="total" value="0"/>  
		<td id='tot'><c:forEach var="bill" items="${billList}" varStatus="statusOuter">
		<c:forEach var="item" items="${bill.billItems}" varStatus="statusInner">
				<c:set var="total" value="${item.amount + total}"/>  
			</c:forEach>
		</c:forEach>
		<b>${total -2*initialtotal}</b>
		</td>
		</tr>
		<tr>
        <td></td>
		<td></td>
		<td></td>
        
		<td align="right"><b>Rebate Amount</b></td>
        <td><b>0</b></td>
        </td>
        </tr>
	</tbody>
</table>
<table>
<form method="post" id="billListForIndoorPatient">
<div id="waiverAmt">
<b>Waiver Amount:</b><input type="text" id="waiverAmount" name="waiverAmount">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- <b>Payment Mode:</b>
			<select id="paymentMode" name="paymentMode">
					<option value="Cash">Cash</option>
					<option value="Card">Card</option>
				</select>  --><b>Comment if Waiver:</b><input id="comment" name="comment" type="text"/>
                <input name="adDays" type="hidden" value="${admittedDays}" />
                <input id="rebateAmount" name="rebateAmount" type="hidden"/>
                <input id="patientCategory" name="patientCategory" type="hidden" value="" />
			

</div>
<div style="text-align:center"> 
<c:choose>
<c:when test="${requestForDischargeStatus== 1}"> 
<input type="submit" id="billSubmitForIndoorPatient" name="billSubmitForIndoorPatient" value="Complete Payment">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</c:when>
</c:choose>
<input type="button" value="Print" onClick="printDiv2();" />
</div>
</form>
</table>

	<div id="printDiv" class="hidden"
		style="width: 1280px; font-size: 0.8em">

		<style>
@media print {
	.donotprint {
		display: none;
	}
	.spacer {
		margin-top: 50px;
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
<h5><center>${userLocation}</center><h5><center> ${userLocationPoBox}</center></h5>
<h5><center>TEL - ${userLocationTel}</center></h5>
<br><br>
<center><b>Interim Invoice</b></center>
<br><br>
		<table align='Center'>

		<tr>
				<td style="width: 10%;">Patient ID:</td>
				<td style="width: 35%;">${patient.patientIdentifier.identifier}</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Ward:</td>
				<td style="width: 35%;">${ward.name}</td>
			</tr>
			<tr>
				<td style="width: 10%;">Name:</td>
				<td style="width: 35%;">${patient.givenName}&nbsp;${patient.familyName}&nbsp;${fn:replace(patient.middleName,',',' ')}</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Bed No:</td>
				<td style="width: 35%;">${bed}</td>

			</tr>
			<tr>
				<td style="width: 10%;">Address:</td>
				<td style="width: 35%;">${address}</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Doctor:</td>
				<td style="width: 35%;">${doctor}</td>
			</tr>
			<tr>
				<td style="width: 10%;">TEl No.:</td>
				<td style="width: 35%;">${phone}</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Adm Date:</td>
				<td style="width: 35%;">${admissionDate}</td>
			</tr>
			<tr>
				<td style="width: 10%;">Payer:</td>
				<td style="width: 35%;">&nbsp;&nbsp;</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Discharge Date:</td>
				<c:choose>
				<c:when test="${requestForDischargeStatus== 1}"> 
					<td style="width: 35%;">${curDate}</td>
				</c:when>
				</c:choose>				
			</tr>
			<tr>
				<td style="width: 10%;">Scheme Name:</td>
				<td style="width: 35%;">&nbsp;&nbsp;</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">Patient Category:</td>
				<td style="width: 35%;">${category}</td>
					
			</tr>
			<tr>
				<td style="width: 10%;">No.of Days:</td>
				<td style="width: 35%;">${admittedDays}</td>
				<td style="width: 10%;">&nbsp;</td>
				<td style="width: 10%;">File Number:</td>
				<td style="width: 35%;">${fileNumber}</td>
			</tr>
			
		</table>


<table id="myTablee" class="tablesorter" class="thickbox" style="width:100%; margin-top:30px">
	<thead>
		<tr align="left">
			<th>S.No</th>
			<th>Date</th>
			<th>Service</th>
			<th>Unit Price</th>
			<th>Amount</th>
		</tr>
		<hr>
	</thead>
	<tbody>
		</tr>
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
			<c:choose>
				<c:when test="${item.name != 'INPATIENT DEPOSIT'}">
			<tr class="${klass}">
				<td>${index}</td>
				<td><openmrs:formatDate date="${item.createdDate}" type="textbox"/></td>
				<td>${item.name}</td>
				<td>${item.unitPrice}</td>
				<td>${item.amount}</td>
				<c:set var="index" value="${index+1}"/>  
			</tr>
			</c:when>
			</c:choose>
			
		</c:forEach>
		</c:forEach>
		</tr>
		<b><b>
		<tr>
		<td></td>
		<td></td>
		<td></td>
		<td align="right"><b>ADVANCE PAYMENT</b></td>
		<c:set var="initialtotal" value="0"/>  
		<td><c:forEach var="bill" items="${billList}" varStatus="statusOuter">
		<c:forEach var="item" items="${bill.billItems}" varStatus="statusInner">
		<c:choose>
				<c:when test="${item.name == 'INPATIENT DEPOSIT'}">
				<c:set var="initialtotal" value="${item.amount + initialtotal}"/>  
						</c:when>
			</c:choose>
			</c:forEach>
		</c:forEach>
		<b>${initialtotal}</b>
		<tr>
		<td></td>
		<td></td>
		<td></td>
		<td align="right"><b>Total</b></td>
		<c:set var="total" value="0"/>  
		<td id='tot2'><c:forEach var="bill" items="${billList}" varStatus="statusOuter">
		<c:forEach var="item" items="${bill.billItems}" varStatus="statusInner">
				<c:set var="total" value="${item.amount + total}"/>  
			</c:forEach>
		</c:forEach>
		<b>${total -2*initialtotal}</b>
		</td></tr>
        <tr>
		<td></td>
		<td></td>
		<td></td>
		<td align="right"><b>Rebate Amount</b></td>
		<td id='dAmount'>

		</td>
        </tr>
	</tbody>
</table>
<br>
<br>
<br>
<br>
<div style="position: fixed; bottom: 0; width:100%;">
<hr>
At the time of discharge you are billed provisionally for services computed at that time. A final
invoice will be sent to you three(3) days after discharge that may include services not billed at the
time of discharge. A cheque or cash payment for such charges willbe expected. NHIF rebate must
be claimed at the time of discharge. Claims after discharge must be made with NHIF Authorities
<hr>
</div>




</div>

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
		
		document.getElementById('dAmount').innerHTML = document.getElementById('rebateAmount').value;
		var printer = window.open('', '', 'width=300,height=300');
		var R = document.getElementById('rebateAmount').value;		
		printer.document.open("text/html");
		printer.document.write(document.getElementById('printDiv').innerHTML);
				
				printer.print();
		printer.document.close();
		printer.window.close();
		//alert("Printing ...");
	}
</script>


</div>