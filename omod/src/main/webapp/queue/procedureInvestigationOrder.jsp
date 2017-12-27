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
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Test order queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<%@ include file="../includes/js_css.jsp"%>

<script type="text/javascript">
jQuery(document).ready(function(){
$('.serquncalc').keyup(function() {
    var result = 0;
    $('#total').attr('value', function() {
        $('.serpricalc').each(function() {
            if ($(this).val() !== '') {
                result += parseInt($(this).val());
            }
        });
        
        var total=result;
        var waiverPercentage=jQuery("#waiverPercentage").val();
        var totalAmountPay=total-(total*waiverPercentage)/100;
        var tap=Math.round(totalAmountPay);
        jQuery("#totalAmountPayable").val(tap);
        
        var totalAmountToPay=tap;
        var amountGiven=jQuery("#amountGiven").val();
        var amountReturned=amountGiven-totalAmountToPay;
        jQuery("#amountReturned").val(amountReturned);
        
        return result;
    });
});
var sos=${serviceOrderSize};
if(sos==0){
jQuery("#savebill").hide(); 
  }
  
    var result = 0;
    $('#total').attr('value', function() {
        $('.serpricalc').each(function() {
            if ($(this).val() !== '') {
                result += parseInt($(this).val());
            }
        });
         var tap=Math.round(result);
         jQuery("#totalAmountPayable").val(tap);
        return result;
    });
 

});
</script>

<script type="text/javascript">
function updatePrice(incon){
var con=incon.toString();
var serqunid=con.concat("servicequantity"); 
var serpriid=con.concat("serviceprice");
var unipriid=con.concat("unitprice");  
//alert(document.getElementById(serqunid).value);
serqun=jQuery("#"+serqunid).val();
unpri=jQuery("#"+unipriid).val();
jQuery("#"+serpriid).val(serqun*unpri);
}
</script>

<script type="text/javascript">
function disable(incon){
var icon=incon.toString();

if(jQuery("#"+icon+"selectservice").attr('checked')) {
  jQuery("#"+icon+"servicequantity").removeAttr("disabled");
  jQuery("#"+icon+"serviceprice").removeAttr("disabled");
  if(jQuery("#credit").attr('checked')) {
  jQuery("#"+icon+"paybill").attr("disabled", "disabled"); 
  }
  else{
  jQuery("#"+icon+"paybill").attr('checked', true);
  jQuery("#"+icon+"paybill").removeAttr("disabled");
  }
  
  var totalValue = $("#total").val();
  var toBeAdded = $("#"+icon+"serviceprice").val();
  var added =  parseInt(totalValue,10) +   parseInt(toBeAdded,10);
  $('#total').val(added);
  
  var total=added;
  var waiverPercentage=jQuery("#waiverPercentage").val();
  var totalAmountPay=total-(total*waiverPercentage)/100;
  var tap=Math.round(totalAmountPay);
  jQuery("#totalAmountPayable").val(tap);
  var amountGiven=jQuery("#amountGiven").val();
  var amountReturned=amountGiven-tap;
  jQuery("#amountReturned").val(amountReturned);
} 
else{
 jQuery("#"+icon+"servicequantity").attr("disabled", "disabled"); 
 jQuery("#"+icon+"paybill").attr("disabled", "disabled"); 
 jQuery("#"+icon+"serviceprice").attr("disabled", "disabled"); 
 var totalValue = $("#total").val();
 var toBeMinus = $("#"+icon+"serviceprice").val();
 var left = totalValue - toBeMinus;
 var total=0;
 if(left>0){
 $('#total').val(left);
 total=left;
 }
 else{
  $('#total').val(0);
  total=0;
 }
 
  var waiverPercentage=jQuery("#waiverPercentage").val();
  var totalAmountPay=total-(total*waiverPercentage)/100;
  var tap=Math.round(totalAmountPay);
  jQuery("#totalAmountPayable").val(tap);
  var amountGiven=jQuery("#amountGiven").val();
  var amountReturned=amountGiven-tap;
  jQuery("#amountReturned").val(amountReturned);
}

}



function payCheckBox(incon){

var icon=incon.toString();
if(jQuery("#"+icon+"paybill").attr('checked')) {
  jQuery("#"+icon+"serviceprice").removeAttr("disabled");
  var totalValue = $("#total").val();
  var toBeAdded = $("#"+icon+"serviceprice").val();
  var added =  parseInt(totalValue,10) +   parseInt(toBeAdded,10);
  $('#total').val(added);
  
  var total=jQuery("#total").val();
  var waiverPercentage=jQuery("#waiverPercentage").val();
  var totalAmountPay=total-(total*waiverPercentage)/100;
  var tap=Math.round(totalAmountPay);
  jQuery("#totalAmountPayable").val(tap);
  var amountGiven=jQuery("#amountGiven").val();
  var amountReturned=amountGiven-tap;
  jQuery("#amountReturned").val(amountReturned);
   
} 
else{
  var totalValue = $("#total").val();
  var toBeMinus = $("#"+icon+"serviceprice").val();
  var left = totalValue - toBeMinus;
  $('#total').val(left);
  jQuery("#"+icon+"serviceprice").attr("disabled", "disabled");
 
  var total=jQuery("#total").val();
  var waiverPercentage=jQuery("#waiverPercentage").val();
  var totalAmountPay=total-(total*waiverPercentage)/100;
  var tap=Math.round(totalAmountPay);
  jQuery("#totalAmountPayable").val(tap);
  var amountGiven=jQuery("#amountGiven").val();
  var amountReturned=amountGiven-tap;
  jQuery("#amountReturned").val(amountReturned); 
}


}


function creditCheckBox(){
if(jQuery("#credit").attr('checked')) {
  for (var i=1; i<=${serviceOrderSize}; i++){
  jQuery("#"+i+"paybill").attr('checked', false);
  jQuery("#"+i+"paybill").attr("disabled", "disabled");
  }
  jQuery("#waiverPercentage").attr("disabled", "disabled");
  jQuery("#totalAmountPayable").attr("disabled", "disabled");
  jQuery("#amountGiven").attr("disabled", "disabled");
  jQuery("#amountReturned").attr("disabled", "disabled");
}
else{
  for (var i=1; i<=${serviceOrderSize}; i++){
  if(jQuery("#"+i+"selectservice").attr('checked')) {
  jQuery("#"+i+"paybill").attr('checked', true);
  jQuery("#"+i+"paybill").removeAttr("disabled");
  }
  else{
  jQuery("#"+i+"paybill").attr('checked', false);
  jQuery("#"+i+"paybill").attr("disabled", "disabled");
  }
  
  }
  jQuery("#waiverPercentage").removeAttr("disabled");
  jQuery("#totalAmountPayable").removeAttr("disabled");
  jQuery("#amountGiven").removeAttr("disabled");
  jQuery("#amountReturned").removeAttr("disabled");
}

}
</script>

<script type="text/javascript">
function validate(serviceOrderSize){
for (var i=1; i<=serviceOrderSize; i++)
  {
  var con=i.toString();
if(jQuery("#"+con+"selectservice").attr('checked')) {
var serqunid=con.concat("servicequantity"); 
serqun=jQuery("#"+serqunid).val();
if (serqun==null || serqun=="")
  {
  alert("Please enter quantity");
  return false;
  }

if (serqun!=null || quantity!=""){
   if(isNaN(serqun)){
   alert("Please enter quantity in correct format");
   return false;
	}
   }  
  }
 }

if(jQuery("#credit").attr('checked')==false) {
if(jQuery("#waiverPercentage").val() ==""){
	alert("Please enter Discount Percentage");
	return false;
}

if(jQuery("#waiverPercentage").val() < 0 ){
	alert("Please enter correct Discount Percentage");
	return false;
}


if(jQuery("#amountGiven").val() ==""){
	alert("Please enter Amount Given");
	return false;
}

if(jQuery("#amountGiven").val() < 0 || !StringUtils.isDigit(jQuery("#amountGiven").val())){
	alert("Please enter correct Amount Given");
	return false;
}

if(jQuery("#amountReturned").val() ==""){
	alert("Please enter Amount Returned");
	return false;
}

if(jQuery("#amountReturned").val() < 0 || !StringUtils.isDigit(jQuery("#amountReturned").val())){
	alert("Please enter correct Amount Returned");
	return false;
}

}
}


function totalAmountToPay(){
var total=jQuery("#total").val();
var waiverPercentage=jQuery("#waiverPercentage").val();
var totalAmountPay=total-(total*waiverPercentage)/100;
var tap=Math.round(totalAmountPay);
jQuery("#totalAmountPayable").val(tap);
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-tap;
jQuery("#amountReturned").val(amountReturned);
}

function amountReturnedToPatient(){
var totalAmountToPay=jQuery("#totalAmountPayable").val();
var amountGiven=jQuery("#amountGiven").val();
var amountReturned=amountGiven-totalAmountToPay;
jQuery("#amountReturned").val(amountReturned);
}

</script>

<div style="max-height: 50px; max-width: 1800px;">
	<b class="boxHeader">List of Procedures and Investigations</b>
</div>
<br />

<div id="patientDetails">
	<!--
<div id="patientDetails" style="margin: 10px auto; width: 981px;">
-->
	<table>
		<tr>
			<td>Date/ Time:</td>
			<td>${date}</td>
		</tr>
		<tr>
			<td>Patient ID:</td>
			<td>${patientSearch.identifier}</td>
		</tr>
		<tr>
			<td>Name:</td>
			<td>${patientSearch.givenName}&nbsp;${patientSearch.familyName}
				</td>
		</tr>
		<tr>
			<td>Gender:</td>
			<td>${gender}</td>
		</tr>
		
		<tr>
			<td>Age:</td>
			<td>${age}</td>
		</tr>
		
		<tr>
			<td>Patient Category:</td>
			<td>${category} - ${subCategory.name} - ${childCategory.name}</td>
		</tr>
		</table>
</div>

<form id="orderBillingForm"
	action="procedureinvestigationorder.form?patientId=${patientId}&encounterId=${encounterId}&indCount=${serviceOrderSize}&billType=mixed"
	method="POST"
	onsubmit="javascript:return validate(${serviceOrderSize});">
	<table id="myTable" class="tablesorter" class="thickbox">
		<thead>
			<tr>
				<th style="text-align: center;">S.No</th>
				<th style="text-align: center;">Service</th>
				<th style="text-align: center;">Select</th>
				<th style="text-align: center;">Quantity</th>
				<!-- 
				<th style="text-align: center;">Reschedule</th>
				 -->
				<th style="text-align: center;">Pay</th>
				<th style="text-align: right;">Unit Price</th>
				<th style="text-align: right;">Q*Unit Price</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="sol" items="${serviceOrderList}" varStatus="index">
				<c:choose>
					<c:when test="${index.count mod 2 == 0}">
						<c:set var="klass" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="klass" value="even" />
					</c:otherwise>
				</c:choose>
				<tr class="${klass}" id="">
					<td align="center">${index.count}</td>
					<td align="center"><input type="text"
						id="${index.count}service" name="${index.count}service"
						value="${sol.name}" readOnly="true"></td>
					<td align="center"><input type="checkbox"
						id="${index.count}selectservice"
						name="${index.count}selectservice" checked="checked"
						value="billed" onclick="disable(${index.count});">
					</td>
					<td align="center"><input type="text"
						id="${index.count}servicequantity"
						name="${index.count}servicequantity" size="7" value=1
						onkeyup="updatePrice(${index.count});" class="serquncalc" /></td>
					<!-- 
					<td align="center"><input type="text"
						id="${index.count}reschedule" name="${index.count}reschedule"
						size="12" readOnly="true">
						 -->
					</td>
					<td align="center"><input type="checkbox"
						id="${index.count}paybill" name="${index.count}paybill"
						checked="checked" value="pay" onclick="payCheckBox(${index.count});">
					</td>
					<td align="right"><input type="text"
						id="${index.count}unitprice" name="${index.count}unitprice"
						size="7" value="${sol.price}" readOnly="true"></td>
					<td align="right"><input type="text"
						id="${index.count}serviceprice" name="${index.count}serviceprice"
						size="7" value="${sol.price}" readOnly="true" class="serpricalc"></td>
				</tr>
			</c:forEach>
		</tbody>
		<tr>
			<td colspan="6" align="right">Total</td>
			<td align="right"><input type="text" id="total" name="total"
				size="7" value="0" readOnly="true" /></td>
		</tr>
		<tr>
			<td colspan="6" align="right">Discount %</td>
			<td align="right"><input type="text" id="waiverPercentage" name="waiverPercentage"
				size="7" value="0" onkeyup="totalAmountToPay();"/></td>
		</tr>
		<tr>
			<td colspan="6" align="right">Total amount payable</td>
			<td align="right"><input type="text" id="totalAmountPayable" name="totalAmountPayable"
				size="7" readOnly="true"/></td>
		</tr>
<!-- 
		<tr>
			<td colspan="6" align="right">Payment Mode:</td>
			<td align="right"><select id="paymentMode" name="paymentMode">
					<option value="Cash">Cash</option>
					<option value="Card">Card</option>
				</select>
			</td>
		</tr>
 -->
        <tr>
			<td colspan="6" align="right">Comment</td>
			<td align="right"><input type="text" id="waiverComment" name="waiverComment" size="7"/></td>
		</tr>
		
		<tr>
			<td colspan="6" align="right">Amount Given</td>
			<td align="right"><input type="text" id="amountGiven" name="amountGiven" size="7" onkeyup="amountReturnedToPatient();"></td>
		</tr>	
		
		<tr>
			<td colspan="6" align="right">Amount Returned to Patient</td>
			<td align="right"><input type="text" id="amountReturned" name="amountReturned" size="7" readOnly="true"/></td>
		</tr>			
	</table>
	
	<tr>
		<td>Credit <input type="checkbox" id="credit" name="credit"
			value="Credit" onclick="creditCheckBox();">
		</td>
		<td><input type="submit" id="savebill" name="savebill"
			value="Save Bill">
		</td>
		<td><input type="button"
			onclick="javascript:window.location.href='billingqueue.form?'"
			value="Cancel">
		</td>
	</tr>
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>