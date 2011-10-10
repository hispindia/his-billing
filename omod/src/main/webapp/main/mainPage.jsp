<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>

<openmrs:require privilege="View Bills" otherwise="/login.htm" redirect="/module/billing/main.form" />
<spring:message var="pageTitle" code="billing.patient.find" scope="page"/>
<openmrs:globalProperty key="hospitalcore.hospitalName" defaultValue="ddu" var="hospitalName"/>
<br/>
<p><b><a href="searchDriver.form"><spring:message code="billing.ambulance"/></a></b>&nbsp; | &nbsp;
<b><a href="searchCompany.form"><spring:message code="billing.tender"/></a></b>&nbsp; | &nbsp;
<b><a href="miscellaneousServiceBill.list"><spring:message code="billing.miscellaneousService"/></a></b></p>
<br/>
<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/index.htm" />	
			
<script type="text/javascript">

	jQuery(document).ready(function(){
		jQuery("#searchbox").showPatientSearchBox({
			searchBoxView: "${hospitalName}/default",
			resultView: "/module/billing/patientsearch/${hospitalName}/main",
			rowPerPage: 15,
			beforeNewSearch: function(){
				jQuery("#patientSearchResultSection").hide();
			},
			success: function(data){
				jQuery("#patientSearchResultSection").show();
			}
		});		
	});
</script>





			
<b class="boxHeader"><spring:message code="Patient.find"/></b>
<div class="box" id="searchbox"></div>
<br/>
<div id="patientSearchResultSection" style="display:none;">
	<div class="boxHeader">Found Patients</div>
	<div class="box" id="patientSearchResult"></div>
</div

<%@ include file="/WEB-INF/template/footer.jsp" %>