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
