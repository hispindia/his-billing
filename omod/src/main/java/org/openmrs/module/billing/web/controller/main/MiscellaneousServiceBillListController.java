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

import javax.servlet.http.HttpServletRequest;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.MiscellaneousService;
import org.openmrs.module.hospitalcore.model.MiscellaneousServiceBill;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@Controller
@RequestMapping("/module/billing/miscellaneousServiceBill.list")
public class MiscellaneousServiceBillListController {

	@RequestMapping(method=RequestMethod.POST)
	public String printBill(@RequestParam("billId") Integer miscellaneousServiceBillId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
    	MiscellaneousServiceBill miscellaneousServiceBill = billingService.getMiscellaneousServiceBillById(miscellaneousServiceBillId);
    	if( miscellaneousServiceBill != null && !miscellaneousServiceBill.getPrinted()){
    		miscellaneousServiceBill.setPrinted(true);
    		billingService.saveMiscellaneousServiceBill(miscellaneousServiceBill);
    	}
    	return "redirect:/module/billing/miscellaneousServiceBill.list";
	}
	@RequestMapping(method=RequestMethod.GET)
	public String listBill(@RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                       @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                       @RequestParam(value="billId",required=false) Integer miscellaneousServiceBillId,
	                       @RequestParam(value="serviceId", required=false) Integer serviceId,
	                         Model model, HttpServletRequest request){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
		MiscellaneousService service = billingService.getMiscellaneousServiceById(serviceId);
		int total = billingService.countListMiscellaneousServiceBill(service);
		PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
		model.addAttribute("listBills", billingService.listMiscellaneousServiceBill(pagingUtil.getStartPos(), pagingUtil.getPageSize(), service) );
		model.addAttribute("pagingUtil", pagingUtil);
		if( miscellaneousServiceBillId != null ){
			MiscellaneousServiceBill miscellaneousServiceBill = billingService.getMiscellaneousServiceBillById(miscellaneousServiceBillId);
			model.addAttribute("bill", miscellaneousServiceBill);
		}
		User user = Context.getAuthenticatedUser();
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );
		
		model.addAttribute("listServices", billingService.getAllMiscellaneousService());
		model.addAttribute("serviceId", serviceId);
		return "/module/billing/main/miscellaneousServiceBillList";
	}
}
