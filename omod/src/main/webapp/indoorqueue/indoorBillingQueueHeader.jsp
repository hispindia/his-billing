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
 *  author: Abhishek-Ankur
 *  date: 15-Dec-2013
 *  Requirement: Indoor Patient Billing
--%>
<openmrs:require privilege="Indoor billing queue" otherwise="/login.htm" redirect="/module/billing/main.form" />
<div>
	<ul id="menu">
	<br />
		<p>
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
	 <!-- ghanshyam -->
	&nbsp; | &nbsp; <b><a href="billingqueue.form"><spring:message
				code="billing.billingqueue" />
	</a>
	</b>
	<!-- Abhishek-Ankur 15-Dec-2013 New Requirement # Indoor Patient Billing Queue -->
	&nbsp; | &nbsp; <b><a href="indoorbillingqueue.form"><spring:message
				code="billing.indoorbillingqueue" />
	</a>
	</b>
</p>
<br />
	</ul>
</div>