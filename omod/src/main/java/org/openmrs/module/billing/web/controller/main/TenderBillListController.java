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
import org.openmrs.module.hospitalcore.model.Company;
import org.openmrs.module.hospitalcore.model.TenderBill;
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
@RequestMapping("/module/billing/tenderBill.list")
public class TenderBillListController {

	@RequestMapping(method=RequestMethod.POST)
	public String printBill(@RequestParam("tenderBillId") Integer tenderBillId,
	                        @RequestParam("companyId") Integer companyId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
    	TenderBill tenderBill = billingService.getTenderBillById(tenderBillId);
    	if( tenderBill != null && !tenderBill.getPrinted()){
    		tenderBill.setPrinted(true);
    		billingService.saveTenderBill(tenderBill);
    	}
    	return "redirect:/module/billing/tenderBill.list?companyId="+companyId;
	}
	@RequestMapping(method=RequestMethod.GET)
	public String listBill(@RequestParam("companyId") Integer companyId,
	                       @RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                       @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                       @RequestParam(value="tenderBillId",required=false) Integer tenderBillId,
	                         Model model, HttpServletRequest request){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Company company = billingService.getCompanyById(companyId);
		if( company != null ){
			int total = billingService.countListTenderBillByCompany(company);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
			model.addAttribute("tenderBills", billingService.listTenderBillByCompany(pagingUtil.getStartPos(), pagingUtil.getPageSize(), company) );
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("company", company);
		}
		if( tenderBillId != null ){
			TenderBill tenderBill = billingService.getTenderBillById(tenderBillId);
			model.addAttribute("tenderBill", tenderBill);
		}
		model.addAttribute("companyId", companyId);
		User user = Context.getAuthenticatedUser();
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );
		return "/module/billing/main/tenderBillList";
	}
}
