<!-- /* *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
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
 */ -->

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Add Bill" otherwise="/login.htm"
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
<c:forEach items="${errors.allErrors}" var="error">
	<span class="error"><spring:message
			code="${error.defaultMessage}" text="${error.defaultMessage}" />
	</span><
</c:forEach>
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
<c:if test="${not empty listMiscellaneousService }">
	<div id="tabs">
		<ul>
			<li><a href="#fragment" style="color: white"><span>List
						Miscellaneous Services</span>
			</a>
			</li>
		</ul>
		<div id="fragment">
			<c:forEach items="${listMiscellaneousService}" var="service">
				<div id="service_${service.id}" class="udiv boxNormal"
					onclick="addToBill('${service.id}', '${service.name}', ${service.price} )">
					${service.name}</div>
			</c:forEach>
		</div>
	</div>
</c:if>

<script type="text/javascript">
	function addToBill(serviceId, name, price) {
		price = parseFloat(price);
		var colorSelected = "rgb(170, 255, 170)";
		if(document.getElementById("billItem") != null){
			deleteInput(serviceId, name);
		}else{
		var checkprice = '\"'+serviceId+'\", '+price+', this.value';
       	var htmlText = "" 
		+"<input  type='text'  size='16'  value='"+name+"'  readonly='readonly'  />&nbsp;"
		+"<input type='text' name='"+serviceId+"_qty' id='"+serviceId+"_qty' onblur='updatePrice("+checkprice+");' value='1' size='3'/>&nbsp;"
    	+"<input  type='text'  size='5' id='"+serviceId+"_amount' name='"+serviceId+"_amount' value='"+price+"' onblur='updatePrice("+checkprice+");'  />&nbsp;"
    	+"<input  type='text'  size='16' id='liableName' name='name'  />&nbsp;"
    	+"<input  type='hidden'  name='serviceId'  value='"+serviceId+"' />&nbsp;"
      	+"<a style='color:red' href='#' onclick='deleteInput("+serviceId+")' >&nbsp;[X]</a>";
       	
        var newElement = document.createElement('div');
        newElement.setAttribute("id", "billItem");
        document.getElementById('totalprice').value = price;
	 	newElement.innerHTML = htmlText;

        var fieldsArea = document.getElementById('extra');
		fieldsArea.appendChild(newElement);
		document.getElementById("service_"+serviceId).style.backgroundColor=colorSelected; 
		}
	}
	//07/07/2012:kesavulu: New Requirement #306 Add field quantity in Miscellaneous Services Bill
	function updateToBill(serviceId, name, price, amount, liableName, billId, billQty) {
		price = parseFloat(price);
		amount = parseFloat(amount);
		var colorSelected = "rgb(170, 255, 170)";
		if(document.getElementById("billItem") != null){
			deleteInput(serviceId, name);
		}else{
		var checkprice = '\"'+serviceId+'\", '+price+', this.value';
		
       	var htmlText =  "<input  type='text'  size='16'  value='"+name+"'  readonly='readonly'  />&nbsp;"
		+"<input type='text' name='"+serviceId+"_qty' id='"+serviceId+"_qty'  onblur='updatePrice("+checkprice+");' value='"+billQty+"' size='3'/>&nbsp;"
    	+"<input type='text' id='"+serviceId+"_amount' name='"+serviceId+"_amount' value='"+amount+"' size='5' onblur='updatePrice("+checkprice+");' />"
    	+"<input  type='text'  size='16' id='liableName' name='name'  value='"+liableName+"'/>&nbsp;"
    	+"<input name='serviceId'  size='25' value='"+serviceId+"'  type='hidden'/>"
		+"<input name='"+serviceId+"_itemId'  size='25' value='"+billId+"'  type='hidden'/>"
      	+"<a style='color:red' href='#' onclick='deleteInput("+serviceId+")' >&nbsp;[X]</a>";
       	
        var newElement = document.createElement('div');
        newElement.setAttribute("id", "billItem");
       
	 	newElement.innerHTML = htmlText;

        var fieldsArea = document.getElementById('extra');
		fieldsArea.appendChild(newElement);
		var totalprice = parseFloat(document.getElementById('totalprice').value);
			document.getElementById('totalprice').value = totalprice  + amount;
		document.getElementById("service_"+serviceId).style.backgroundColor=colorSelected; 
		}
	}
		function deleteInput( serviceId ) {
			var parentDiv = 'extra';
		 	if (document.getElementById("billItem")) {     
		        var child = document.getElementById("billItem");
		        var parent = document.getElementById(parentDiv);
		        parent.removeChild(child);
		        document.getElementById('totalprice').value = "0";
		        document.getElementById("service_"+serviceId).style.backgroundColor="#FCCFFF";
		   }
		   else {
		        alert("Element has already been removed or does not exist.");
		        return false;
		   }
		}
		function updatePrice(serviceId, servicePrice, qty){
			var objRegExp  = /^ *[0-9]+ *$/;
			servicePrice=document.getElementById(serviceId+'_amount').value;
			qty=document.getElementById(serviceId+'_qty').value;
			
			if( !objRegExp.test(qty) || qty=='' || qty <=0){
					alert("Please enter valid quantity!!!");
					var ele = document.getElementById(serviceId+'_qty');
					setTimeout(function(){ele.focus()}, 10);
				}else{		
			var initvalue = parseFloat(document.getElementById(serviceId+'_amount').value);
			//document.getElementById(serviceId+'_price').value = servicePrice * qty;
			var result=parseFloat((servicePrice * qty));
			//var diff = parseFloat((servicePrice * qty) - initvalue);
			//var total = parseFloat(document.getElementById('totalprice').value);
			document.getElementById('totalprice').value = result;
			}
		}
		
		function validateForm(){
			$('#totalprice').focus();
			var liableName = jQuery("#liableName").val();
			if( jQuery.trim(liableName) == 0  ) {
				alert("Please enter name");
			}else{
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
	<form method="POST" id="billForm"
		action="editMiscellaneousServiceBill.form" onsubmit="return false">
		<input type="hidden" value="" name="action" id="action" />
		<div>
			<input type="button" id="subm" onclick="validateForm()" name="subm"
				value="<spring:message code='billing.bill.save'/>" /> <input
				type="button" id="voi" onclick="voidBill()"
				value="<spring:message code='billing.bill.void'/>" /> <input
				type="button" value="<spring:message code='general.cancel'/>"
				onclick="javascript:window.location.href='miscellaneousServiceBill.list'" />
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

		<input type="hidden" name="billId" value="${bill.id}">

		<div id="extra" class="cancelDraggable"
			style="background: #f6f6f6; border: 1px #808080 solid; padding: 0.3em; margin: 0.3em 0em; width: 100%;">
			<input type='text' size='16' value='Service Name' readonly='readonly' />&nbsp;
			<input type='text' size="5" value='Qty' readonly="readonly" />&nbsp;
			<input type='text' size="5" value='Price' readonly="readonly" />&nbsp;
			<input type='text' size='16' value='Name' readonly="readonly" />&nbsp;</b>
			<hr />
		</div>	


	</form>
</div>
<c:if test="${not empty bill }">
	<script>
		updateToBill(${bill.service.id},'${bill.service.name}', ${bill.service.price}, ${bill.amount}, '${bill.liableName}', ${bill.id}, ${bill.quantity})
	</script>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>
			