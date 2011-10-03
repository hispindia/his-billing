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

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@Controller
@RequestMapping("/module/billing/searchDriver.form")
public class SearchDriverController {

	@RequestMapping(method=RequestMethod.POST)	
	public String searchCompany(@RequestParam("searchText") String searchText, Model model){
		
		BillingService billingService = Context.getService(BillingService.class);
		
		model.addAttribute("drivers", billingService.searchDriver(searchText));
		
		model.addAttribute("searchText", searchText);
			
		return "/module/billing/main/searchDriver";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String listAll(Model model){
		BillingService billingService = Context.getService(BillingService.class);
		model.addAttribute("drivers", billingService.getAllActiveDriver());
		return "/module/billing/main/searchDriver";
	}
}
