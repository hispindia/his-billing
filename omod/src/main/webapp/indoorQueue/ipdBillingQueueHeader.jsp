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
<openmrs:require privilege="IPD Billing queue Header" otherwise="/login.htm" redirect="/module/billing/main.form" />
<div>
	<ul id="menu">
	<br />
		<p>
	</b>
	&nbsp; 	&nbsp; <b><a href="billingqueue.form"><spring:message
				code="billing.billingqueue" />
	</a>
	</b>&nbsp; | &nbsp; <b><a href="ipdbillingqueue.form"><spring:message
				code="billing.indoorbillingqueue" />
	</a>
	&nbsp; | &nbsp; 
		<b><a href="searchDriver.form"><spring:message
				code="billing.ambulance" />
	</a>
	</b>&nbsp; | &nbsp; <b><a href="searchCompany.form"><spring:message
				code="billing.tender" />
	</a>
	</b>&nbsp; | &nbsp; <b><a href="miscellaneousServiceBill.list"><spring:message
				code="billing.miscellaneousService" />
	</a>
	</b>
	&nbsp; | &nbsp; <b><a href="main.form"><spring:message
				code="billing.searchPatient" />
	</a>
	
	
	</b>
</p>
<br />
	</ul>
</div>