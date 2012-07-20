/**
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
 **/

package org.openmrs.module.billing.web.controller.main;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.MiscellaneousService;
import org.openmrs.module.hospitalcore.model.MiscellaneousServiceBill;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/billing/addMiscellaneousServiceBill.form")
public class MiscellaneousServiceBillAddController {
	Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(Model model,
			@RequestParam("serviceId") Integer miscellaneousServiceId, 
			@RequestParam("name") String name,HttpServletRequest request, Object command, BindingResult binding ){
		
		BillingService billingService = (BillingService) Context.getService(BillingService.class);
		
		//07/07/2012:kesavulu: New Requirement #306 Add field quantity in Miscellaneous Services Bill

		MiscellaneousService miscellaneousService = null;
		int quantity = 0;
		Money itemAmount;
		Money totalAmount = new Money(BigDecimal.ZERO);
		
		miscellaneousService = billingService.getMiscellaneousServiceById(miscellaneousServiceId);
		quantity =Integer.parseInt(request.getParameter(miscellaneousServiceId+"_qty"));
		
		itemAmount = new Money(new BigDecimal(request.getParameter(miscellaneousServiceId+"_price")));
		itemAmount = itemAmount.times(quantity);
		totalAmount = totalAmount.plus(itemAmount);	
	
		MiscellaneousServiceBill miscellaneousServiceBill = new MiscellaneousServiceBill();
		miscellaneousServiceBill.setCreatedDate(new Date());
		miscellaneousServiceBill.setCreator(Context.getAuthenticatedUser().getUserId());
		miscellaneousServiceBill.setLiableName(name);		
		
		miscellaneousServiceBill.setAmount(totalAmount.getAmount());		
		miscellaneousServiceBill.setService(miscellaneousService);
		miscellaneousServiceBill.setQuantity(quantity);
		miscellaneousServiceBill.setReceipt(billingService.createReceipt());		
		miscellaneousServiceBill = billingService.saveMiscellaneousServiceBill(miscellaneousServiceBill);

		return "redirect:/module/billing/miscellaneousServiceBill.list?serviceId="+miscellaneousServiceId+"&billId="+miscellaneousServiceBill.getId();
	}	

	@RequestMapping(method=RequestMethod.GET)
	public String displayForm(@ModelAttribute("command") Object command,  HttpServletRequest request, Model model){

		BillingService billingService = (BillingService) Context.getService(BillingService.class);
		
		List<MiscellaneousService> listMiscellaneousService = billingService.getAllMiscellaneousService();
		
		if( listMiscellaneousService == null || listMiscellaneousService.size() == 0  )
		{
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "No MiscellaneousService found.");
		}else {
			model.addAttribute("listMiscellaneousService", listMiscellaneousService);
		}
		return "module/billing/main/miscellaneousServiceBillAdd";
	}

	
}
