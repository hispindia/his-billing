<%--
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
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
<openmrs:require privilege="Add Bill" otherwise="/login.htm"
	redirect="/module/billing/main.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/ui.core.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/ui.tabs.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/common.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/hospitalcore/scripts/string-utils.js"></script>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/css/start/ui.tabs.css" />
<script type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/css/start/jquery-ui-1.8.2.custom.css"></script>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/styles/common.css" />
<style>
span {
	color: white;
}
</style>
${tabs }
<script type="text/javascript">
jQuery(document).ready(function(){ 
    $('#container-1 ul').tabs();
    document.getElementById("spclwrd").style.display = "none";
    document.getElementById("disc").style.display = "none";
});
</script>


<script type="text/javascript">

function dis(){ 
   //if(document.getElementById("disc"))
	document.getElementById("disc").style.display = "block";
	 document.getElementById("spclwrd").style.display = "none";
	}
function spcl(){ 
//if(document.getElementById("disc"))
	document.getElementById("spclwrd").style.display = "block";
	 document.getElementById("disc").style.display = "none";
	}
	
	function addToBill( conceptId, serviceName, servicePrice, qty) {
		servicePrice = parseFloat(servicePrice);
		var colorSelected = "rgb(170, 255, 170)";
		if(document.getElementById("com_"+conceptId+"_div") != null){
			deleteInput(conceptId, serviceName, servicePrice, qty);
		}
		else
		{
			var deleteString = 'deleteInput(\"'+conceptId+'\", \"'+serviceName+'\", '+servicePrice+','+qty+')';

			var checkprice = '\"'+conceptId+'\", '+servicePrice+', this.value';

	       	var htmlText =  "<div id='com_"+conceptId+"_div'>"
	       	 +"<input name='cons' type='checkbox' value='"+conceptId+"' style='display:none; visibility:hidden;' checked='true'> "
	       	 +"<input id='"+conceptId+"_name'  name='"+conceptId+"_name' type='text' size='25' value='"+serviceName+"'  readonly='readonly'/>&nbsp;"
	       	 +"<input name='"+conceptId+"_unitPrice' value='"+servicePrice+"' type='hidden'/>" 
	       	 +"<input type='text' name='"+conceptId+"_qty' id='"+conceptId+"_qty'  class='qtyField' onblur='updatePrice("+checkprice+");' value='1' size='3'/>&nbsp;"
	       	 +"<input type='text' id='"+conceptId+"_price' name='"+conceptId+"_price' value='"+servicePrice+"' size='5'  readonly='readonly'/>"
	      	 +"<a style='color:red' href='#' onclick='"+deleteString+"' >[X]</a>"					
	       	 +"</div>";
	       	
	        var newElement = document.createElement('div');
	        newElement.setAttribute("id", conceptId);
		        
		 	newElement.innerHTML = htmlText;
       
	        var fieldsArea = document.getElementById('extra');
			fieldsArea.appendChild(newElement);
			var totalprice = parseFloat(document.getElementById('totalprice').value);
			document.getElementById('totalprice').value = totalprice  + servicePrice;
			document.getElementById('box_'+conceptId).style.backgroundColor=colorSelected;
			var serName = jQuery("#"+conceptId+"_name").val();
			serName = serName.replace("#","\'");
			jQuery("#"+conceptId+"_name").val(serName);
			
			
			var total=jQuery("#totalprice").val();
            var waiverPercentage=jQuery("#waiverPercentage").val();
            var totalAmountPay=total-(total*waiverPercentage)/100;
            var tap=Math.round(totalAmountPay);
            jQuery("#totalAmountPayable").val(tap);

            var totalAmountToPay=tap;
            var amountGiven=jQuery("#amountGiven").val();
            var amountReturned=amountGiven-totalAmountToPay;
            jQuery("#amountReturned").val(amountReturned);
		}
	}
	
		function deleteInput( conceptId, serviceName, servicePrice,  qty) {
			var parentDiv = 'extra';
			servicePrice = parseInt(servicePrice);
		   if (conceptId == parentDiv) {
		        alert("The parent Element cannot be removed.");
		   }
		   else if (document.getElementById(conceptId)) {     
		        var child = document.getElementById(conceptId);
		        var parent = document.getElementById(parentDiv);
		        var removevalue = parseFloat(document.getElementById(conceptId+'_price').value);
		        parent.removeChild(child);
		        var totalprice = parseFloat(document.getElementById('totalprice').value);
		        document.getElementById('totalprice').value = totalprice  - removevalue;
		        document.getElementById('box_'+conceptId).style.backgroundColor="#FCCFFF";
		        
		        var total=jQuery("#totalprice").val();
                var waiverPercentage=jQuery("#waiverPercentage").val();
                var totalAmountPay=total-(total*waiverPercentage)/100;
                var tap=Math.round(totalAmountPay);
                jQuery("#totalAmountPayable").val(tap);

                var totalAmountToPay=tap;
                var amountGiven=jQuery("#amountGiven").val();
                var amountReturned=amountGiven-totalAmountToPay;
                jQuery("#amountReturned").val(amountReturned);
		   
		   
		   
		   }
		   else {
		        alert("Element has already been removed or does not exist.");
		        return false;
		   }
		}

		
		function updatePrice(conceptId, servicePrice, qty){
			var objRegExp  = /^ *[0-9]+ *$/;
			if( !objRegExp.test(qty) || qty=='' || qty <=0){
				alert("Please enter valid quantity!!!");
				var ele = document.getElementById(conceptId+'_qty');
				ele.focus();
			}else{		
				var initvalue = parseFloat(document.getElementById(conceptId+'_price').value);
				document.getElementById(conceptId+'_price').value = servicePrice * qty;
				var diff = parseFloat((servicePrice * qty) - initvalue);
				var total = parseFloat(document.getElementById('totalprice').value);
				document.getElementById('totalprice').value = total + diff;
			}
			var total=jQuery("#totalprice").val();
			  
            var waiverPercentage=jQuery("#waiverPercentage").val();
            var totalAmountPay=total-(total*waiverPercentage)/100;
            var tap=Math.round(totalAmountPay);
            jQuery("#totalAmountPayable").val(tap);

            var totalAmountToPay=tap;
            var amountGiven=jQuery("#amountGiven").val();
            var amountReturned=amountGiven-totalAmountToPay;
            jQuery("#amountReturned").val(amountReturned);
		}
		function submitBillForm(){
			$('#totalprice').focus();
			var ok = true;
			var objRegExp  = /^ *[0-9]+ *$/;
			jQuery(".qtyField").each(function(){
				var qty = jQuery(this).val();
				if( !objRegExp.test(qty) || qty=='' || qty <=0){
					ok = false;
				}
			});
			if( !ok ) {
				alert("Please enter valid quantity!!!");
			}else if(! jQuery("input[type='checkbox']","div#extra").length ) {
				alert("Please select item for billing");
			}else {
				if(jQuery("#waiverPercentage").val() ==""){
		            alert("Please enter Discount Percentage");
		            return false;
	                }

	                if(jQuery("#waiverPercentage").val() < 0 ){
		            alert("Please enter correct Discount Percentage");
		            return false;
	                }

	            	if(jQuery("#spclPercntage").val() ==""){
			            alert("Please enter Spclward Percentage");
			            return false;
		                } 
	                if(jQuery("#spclPercntage").val() < 0 ){
			            alert("Please enter correct Spclward Percentage");
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
				jQuery("#subm").attr("disabled", "disabled");
				jQuery("#billForm").submit();
			}
		}
		function totalAmountToPay(){
			var total=jQuery("#totalprice").val();
			var waiverPercentage=jQuery("#waiverPercentage").val();
			
			var totalAmountPay=total-(total*waiverPercentage)/100;
			
			var tap=Math.round(totalAmountPay);
			jQuery("#totalAmountPayable").val(tap);
			var amountGiven=jQuery("#amountGiven").val();
			var amountReturned=amountGiven-tap;
			jQuery("#amountReturned").val(amountReturned);
			}
		
		
		function totalAmountToPayspcl()
		{
			var total=jQuery("#totalprice").val();
			var spclPercentage=jQuery("#spclPercntage").val();
			
			var totalAmountPay= +total + +((total*spclPercentage)/100);
			
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

<!-- Right side div for bill collection -->
<div id="billDiv">
	<form method="POST" id="billForm" onsubmit="return false">
		<div>
			<input type="button" onclick="submitBillForm()" id="subm" name="subm"
				value="<spring:message code='billing.bill.save'/>" /> <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='patientServiceBill.list?patientId=${patientId}'" />
			<!-- 
		    <select name="enctype"  tabindex="20" >
                <c:forEach items="${encounterTypes}" var="enct">
                    <option value="${enct.encounterTypeId}">${enct.name}</option>
                </c:forEach>
            </select>
		 -->
			<input type="button" id="toogleBillBtn" value="-"
				onclick="toogleBill(this);" class="min" style="float: right" />
		</div>
		<div id="total"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='25' value='Total:' />&nbsp; <input
				type='text' size='3' value='' readonly="readonly" />&nbsp; <input
				type='text' id='totalprice' name='totalprice' size='5' value='0'
				readonly="readonly" />&nbsp; <b>
		</div>


        
		<div id="extra" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='25' value='Service Name' readonly='readonly' />&nbsp;
			<input type='text' size="3" value='Qty' readonly="readonly" />&nbsp;
			<input type='text' size='5' value='Price' readonly="readonly" />&nbsp;</b>
			<hr />
		</div>

   		<div id="waiverDiv"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<div>
			<tr>
			<td><input type="radio" value="Discount" name="Discount" onclick="dis();"/>Discount&nbsp;&nbsp;
			<input type="radio" value='SpecialCharges' name="Discount" onclick="spcl();" />SpecialCharges</td></tr>
			</div>
			<div id="disc">
			Discount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" id="waiverPercentage" name="waiverPercentage"
				size="11" class="cancelDraggable" value="0" onkeyup="totalAmountToPay();"/>%
		</div>
		<div id="spclwrd">
			
			SpecialWard Charges&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" id="spclPercntage" name="spclPercntage"
				size="11" class="cancelDraggable" value="0" onkeyup="totalAmountToPayspcl();"/>%
		</div>
		<div>
		Total amount payable&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="totalAmountPayable" name="totalAmountPayable"
				size="11" readOnly="true"/>
		</div>
		<div>
		Comment&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="waiverComment" name="waiverComment" size="11" class="cancelDraggable"/>
		</div>
		<div>
		Amount Given&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="amountGiven" name="amountGiven" size="11" class="cancelDraggable" onkeyup="amountReturnedToPatient();"/>
		</div>
		<div>
		Amount Returned to Patient&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="amountReturned" name="amountReturned" size="11" readOnly="true"/>
		</div>
		</div>
	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
