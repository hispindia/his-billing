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
$("#waiverCommentDiv").hide();
$('.serquncalc').keyup(function() {
    var result = 0;
    $('#total').attr('value', function() {
        $('.serpricalc').each(function() {
            if ($(this).val() !== '') {
                result += parseInt($(this).val());
            }
        });
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
        return result;
    });
 

});


function loadWaiverCommentDiv()
{
	$("#waiverCommentDiv").show();
}
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
  jQuery("#"+icon+"paybill").removeAttr("disabled");
  
  var totalValue = $("#total").val();
  var toBeAdded = $("#"+icon+"serviceprice").val();
  var added =  parseInt(totalValue,10) +   parseInt(toBeAdded,10);
  $('#total').val(added);
} 
else{
 jQuery("#"+icon+"servicequantity").attr("disabled", "disabled"); 
 jQuery("#"+icon+"paybill").attr("disabled", "disabled"); 
 jQuery("#"+icon+"serviceprice").attr("disabled", "disabled"); 
 var totalValue = $("#total").val();
 var toBeMinus = $("#"+icon+"serviceprice").val();
 var left = totalValue - toBeMinus;
 $('#total').val(left);
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
   
} 
else{
 var totalValue = $("#total").val();
 var toBeMinus = $("#"+icon+"serviceprice").val();
 var left = totalValue - toBeMinus;
 $('#total').val(left);
 jQuery("#"+icon+"serviceprice").attr("disabled", "disabled"); 
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

if( Number(jQuery("#total").val()) < Number(jQuery("#waiverAmount").val())){
	alert("Please enter correct Waiver Amount");
	return false;
}
if(isNaN(jQuery("#waiverAmount").val()) || jQuery("#waiverAmount").val() < 0 ){
	alert("Please enter correct Waiver Amount");
	return false;
}
if(jQuery("#waiverAmount").val()>0 && jQuery("#waiverComment").val() ==""){
	alert("Please enter Waiver Number");
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
			<td>${patientSearch.givenName}&nbsp;${patientSearch.familyName}&nbsp;${fn:replace(patientSearch.middleName,',',' ')}&nbsp;&nbsp;
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
			<td>Payment Category:</td>
			<td>${category}</td>
		</tr>
		
		
		<tr>
			<td>File Number:</td>
			<td>${fileNumber}</td>
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
			<td colspan="6" align="right">Waiver Amount</td>
			<td align="right"><input type="text" id="waiverAmount" name="waiverAmount"
				size="7" onblur="loadWaiverCommentDiv();" /></td>
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
	</table>
	<div align="right" id="waiverCommentDiv">
	Waiver Number/Comment <input type="text" id="waiverComment" name="waiverComment"  size="20" />
	</div>
	<tr>
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