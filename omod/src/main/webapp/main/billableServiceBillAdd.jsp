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
});
</script>


<script type="text/javascript">
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
				jQuery("#subm").attr("disabled", "disabled");
				jQuery("#billForm").submit();
			}
		}

</script>

<!-- Right side div for bill collection -->
<div id="billDiv">
	<form method="POST" id="billForm" onsubmit="return false">
		<div>
			<input type="button" onclick="submitBillForm()" id="subm" name="subm"
				value="<spring:message code='billing.bill.save'/>" /> <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='patientServiceBillForBD.list?patientId=${patientId}'" />
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


	</form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
