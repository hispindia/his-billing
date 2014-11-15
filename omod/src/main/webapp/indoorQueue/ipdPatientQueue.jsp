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
 *  author: sagar
 *  date: 11-nov-2014
--%>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="Test order queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<%@ include file="../includes/js_css.jsp"%>
<%@ include file="../indoorQueue/ipdBillingQueueHeader.jsp"%>
<h2>Indoor Patient Queue</h2>	
<br />

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

	currentPage = 1;
    jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true,showOn: "button",
                buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif",
                buttonImageOnly: true,});
    });
	
	// get queue
	function getBillingQueue(currentPage){
		this.currentPage = currentPage;
		var date = jQuery("#date").val();
		var searchKey = jQuery("#searchKey").val();
	
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/billing/patientsearchipdbillingqueue.form",
			data : ({
				date			: date,
				searchKey		: searchKey,
				currentPage		: currentPage
			}),
			success : function(data) {
				jQuery("#billingqueue").html(data);	
			},
			
		});
	}

	
	/**
	 * RESET SEARCH FORM
	 *    Set date text box to current date
	 *    Empty the patient name/identifier textbox
	 */
	function reset(){
		jQuery("#date").val("${currentDate}");
		jQuery("#searchKey").val("");
	}
</script> 

<div class="boxHeader">
	<strong>Get Queue</strong>
</div>
<div class="box">
	Date:
	<input id="date" value="${currentDate}" style="text-align:right;"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	Patient ID/Name:
	<input id="searchKey"/>
	<br/>
	<input type="button" value="Get patients" onClick="getBillingQueue(1);"/>
	<input type="button" value="Reset" onClick="reset();"/>
</div>

<div id="billingqueue">
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>