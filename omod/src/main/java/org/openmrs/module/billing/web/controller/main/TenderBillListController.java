/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
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
