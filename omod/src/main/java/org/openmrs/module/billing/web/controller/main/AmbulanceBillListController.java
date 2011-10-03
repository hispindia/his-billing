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
import org.openmrs.module.hospitalcore.model.AmbulanceBill;
import org.openmrs.module.hospitalcore.model.Driver;
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
@RequestMapping("/module/billing/ambulanceBill.list")
public class AmbulanceBillListController {

	@RequestMapping(method=RequestMethod.POST)
	public String printBill(@RequestParam("ambulanceBillId") Integer ambulanceBillId,
	                        @RequestParam("driverId") Integer driverId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
    	AmbulanceBill ambulanceBill = billingService.getAmbulanceBillById(ambulanceBillId);
    	if( ambulanceBill != null && !ambulanceBill.getPrinted()){
    		ambulanceBill.setPrinted(true);
    		billingService.saveAmbulanceBill(ambulanceBill);
    	}
    	return "redirect:/module/billing/ambulanceBill.list?driverId="+driverId;
	}
	@RequestMapping(method=RequestMethod.GET)
	public String listBill(@RequestParam("driverId") Integer driverId,
	                       @RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                       @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                       @RequestParam(value="ambulanceBillId",required=false) Integer ambulanceBillId,
	                         Model model, HttpServletRequest request){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Driver driver = billingService.getDriverById(driverId);
		if( driver != null ){
			int total = billingService.countListAmbulanceBillByDriver(driver);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
			model.addAttribute("ambulanceBills", billingService.listAmbulanceBillByDriver(pagingUtil.getStartPos(), pagingUtil.getPageSize(), driver) );
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("driver", driver);
		}
		if( ambulanceBillId != null ){
			AmbulanceBill ambulanceBill = billingService.getAmbulanceBillById(ambulanceBillId);
			model.addAttribute("ambulanceBill", ambulanceBill);
		}
		model.addAttribute("driverId", driverId);
		User user = Context.getAuthenticatedUser();
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );
		return "/module/billing/main/ambulanceBillList";
	}
}
