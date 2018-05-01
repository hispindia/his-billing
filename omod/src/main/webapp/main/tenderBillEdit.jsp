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
<openmrs:require privilege="Edit Bill" otherwise="/login.htm"
	redirect="/module/billing/main.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/moduleResources/billing/scripts/common.js"></script>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/scripts/jquery/css/start/jquery-ui-1.8.2.custom.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/moduleResources/billing/styles/common.css" />

<style>
.detailDiv {
	z-index: 100;
	border: 1px solid black;
	display: none;
	position: absolute;
	background-color: #CFE7FF;
	padding: 3px;
}
</style>

<script>
	jQuery(document).ready(function(){
		jQuery(".udiv").hover(
				function(){
					jQuery(this).children("div.detailDiv:first").show();
				}
				,function(){
					jQuery(this).children("div.detailDiv:first").hide();
				});
	});
</script>

<c:if test="${not empty listTender }">
	<div id="tabs">
		<ul>
			<li><a href="#fragment" style="color: white"><span>List
						Tenders</span>
			</a>
			</li>
		</ul>
		<div id="fragment">
			<c:forEach items="${listTender}" var="tender">
				<div id="tender_${tender.tenderId}" class="udiv boxNormal"
					onclick="addToBill('${tender.tenderId}', '${tender.price}', '${tender.name}')">
					${tender.number} - ${tender.name}
					<div id="detail_${tender.tenderId}" class="detailDiv">
						<ul style="list-style: none; padding: 0; margin: 0;">
							<li><b>Description:</b> ${tender.description }</li>
							<li><b>Price:</b> ${tender.price }</li>
							<li><b>Opening date:</b> <openmrs:formatDate
									date="${tender.openingDate }" type="textbox" />
							</li>
							<li><b>Closing date:</b> <openmrs:formatDate
									date="${tender.closingDate }" type="textbox" />
							</li>
						</ul>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</c:if>

<script type="text/javascript">
	function addToBill(tenderId, price, name) {
		price = parseFloat(price);
		var colorSelected = "rgb(170, 255, 170)";
		if(document.getElementById("billItem_"+tenderId) != null){
			deleteInput(tenderId, price, name);
		}
		else
		{
			var deleteString = 'deleteInput(\"'+tenderId+'\")';

			var checkprice = '\"'+tenderId+'\", '+price+', this.value';

	       	var htmlText =  " "
	       	+"<input id='tenders' name='tenderIds' type='checkbox' value='"+tenderId+"' style='display:none; visibility:hidden;' checked='true'> "
	       	+"<input id='"+tenderId+"_name'  type='text' size='25' value='"+name+"'  readonly='readonly'/>"
	    	+"<input type='text' name='"+tenderId+"_qty' id='"+tenderId+"_qty' class='qtyField'  onblur='updatePrice("+checkprice+");' value='1' size='3'/>&nbsp;"
	    	+"<input type='text' id='"+tenderId+"_amount'  value='"+price+"' size='5'  readonly='readonly'/>"
	      	+"<a style='color:red' href='#' onclick='"+deleteString+"' >&nbsp;[X]</a>";
	       	
	        var newElement = document.createElement('div');
	        newElement.setAttribute("id", "billItem_"+tenderId);
		        
		 	newElement.innerHTML = htmlText;

	        var fieldsArea = document.getElementById('extra');
			fieldsArea.appendChild(newElement);
			var totalprice = parseFloat(document.getElementById('totalprice').value);
			document.getElementById('totalprice').value = totalprice  + price;
			document.getElementById("tender_"+tenderId).style.backgroundColor=colorSelected; 
			var serName = jQuery("#"+tenderId+"_name").val();
			serName = serName.replace("#","\'");
			jQuery("#"+tenderId+"_name").val(serName);
		}
	}

	function updateBill(tenderId,name, unitPrice,  amount, billId, billQty)
	{
		unitPrice = parseFloat(unitPrice);
		amount = parseFloat(amount);
		var colorSelected = "rgb(170, 255, 170)";
		if(document.getElementById("billItem_"+tenderId) != null){
			deleteInput(tenderId, price, name);
		}
		else
		{
			var deleteString = 'deleteInput(\"'+tenderId+'\", \"'+amount+'\", \"'+name+'\")';

			var checkprice = '\"'+tenderId+'\", '+unitPrice+', this.value';

	       	var htmlText =  " "
	       	+"<input id='tenders' name='tenderIds' type='checkbox' value='"+tenderId+"' style='display:none; visibility:hidden;' checked='true'> "
	       	+"<input id='"+tenderId+"_name'   type='text' size='25' value='"+name+"'  readonly='readonly'/>"
	    	+"<input name='"+tenderId+"_itemId'  size='25' value='"+billId+"'  type='hidden'/>"
	    	+"<input type='text' name='"+tenderId+"_qty' id='"+tenderId+"_qty'  onblur='updatePrice("+checkprice+");' value='"+billQty+"' size='3'/>&nbsp;"
	    	+"<input type='text' id='"+tenderId+"_amount'  value='"+amount+"' size='5'  readonly='readonly'/>"
	      	+"<a style='color:red' href='#' onclick='"+deleteString+"' >&nbsp;[X]</a>";
	       	
	        var newElement = document.createElement('div');
	        newElement.setAttribute("id", "billItem_"+tenderId);
		        
		 	newElement.innerHTML = htmlText;

	        var fieldsArea = document.getElementById('extra');
			fieldsArea.appendChild(newElement);
			var totalprice = parseFloat(document.getElementById('totalprice').value);
			document.getElementById('totalprice').value = totalprice  + amount;
			document.getElementById("tender_"+tenderId).style.backgroundColor=colorSelected; 
			var serName = jQuery("#"+tenderId+"_name").val();
			serName = serName.replace("#","\'");
			jQuery("#"+tenderId+"_name").val(serName);
		}
	}
	
	function deleteInput(tenderId) {
		var parentDiv = 'extra';
	 	if (document.getElementById("billItem_"+tenderId)) {     
	 		  var child = document.getElementById("billItem_"+tenderId);
		        var parent = document.getElementById(parentDiv);
		        var removevalue = parseFloat(document.getElementById(tenderId+'_amount').value);
		        parent.removeChild(child);
		        var totalprice = parseFloat(document.getElementById('totalprice').value);
		        document.getElementById('totalprice').value = totalprice  - removevalue;
		        document.getElementById("tender_"+tenderId).style.backgroundColor="#FCCFFF";
	   }
	   else {
	        alert("Element has already been removed or does not exist.");
	        return false;
	   }
	}

		
	function updatePrice(serviceId, servicePrice, qty){
		 var objRegExp  = /^ *[0-9]+ *$/;
			if( !objRegExp.test(qty) || qty==''){
				alert("Please enter valid quantity!!!");
				var ele = document.getElementById(serviceId+'_qty');
				setTimeout(function(){ele.focus()}, 10);
			}else{		
		var initvalue = parseFloat(document.getElementById(serviceId+'_amount').value);
		document.getElementById(serviceId+'_amount').value = servicePrice * qty;
		var diff = parseFloat((servicePrice * qty) - initvalue);
		var total = parseFloat(document.getElementById('totalprice').value);
		document.getElementById('totalprice').value = total + diff;
		}
	}
	function validateForm(){
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
			jQuery("#billForm").submit()
		}
		
	}
	function voidBill(){
		jQuery("#action").val("void");
		jQuery("#subm").attr("disabled", "disabled");
		jQuery("#voi").attr("disabled", "disabled");
		jQuery("#billForm").submit();
	}
</script>

<!-- Right side div for bill collection -->
<div id="billDiv">
	<form method="POST" id="billForm" action="editTenderBill.form"
		onsubmit="return false">
		<input type="hidden" value="" name="action" id="action" />
		<div>
			<input type="button" onclick="validateForm()" id="subm" name="subm"
				value="<spring:message code='billing.bill.save'/>" /> <input
				type="button" id="voi" onclick="voidBill()"
				value="<spring:message code='billing.bill.void'/>" /> <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='tenderBill.list?companyId=${companyId}'" />
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

		<input type="hidden" name="companyId" value="${companyId}"> <input
			type="hidden" name="tenderBillId" value="${tenderBill.tenderBillId}">
		<input type="hidden" id="serviceCount" name="serviceCount" value="0">

		<div id="extra" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='25' value='Service Name' readonly='readonly' />&nbsp;
			<input type='text' size="3" value='Qty' readonly="readonly" />&nbsp;
			<input type='text' size='5' value='Amount' readonly="readonly" />&nbsp;</b>
			<hr />
		</div>
	</form>
</div>
<c:forEach items="${tenderBill.billItems}" var="bill">
	<c:set var="search" value="\'" />
	<c:set var="replace" value="#" />
	<c:set var="serviceName"
		value="${fn:replace(bill.name,search,replace)}" />
	<script type="text/javascript">
		updateBill(${bill.tender.tenderId},'${serviceName}', ${bill.unitPrice},  ${bill.amount}, ${bill.tenderBillItemId}, ${bill.quantity});
	</script>
</c:forEach>

<%@ include file="/WEB-INF/template/footer.jsp"%>
