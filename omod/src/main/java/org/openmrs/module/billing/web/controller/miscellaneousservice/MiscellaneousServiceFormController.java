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
package org.openmrs.module.billing.web.controller.miscellaneousservice;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.MiscellaneousService;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
@RequestMapping("/module/billing/miscellaneousService.form")
public class MiscellaneousServiceFormController {
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("miscellaneousService") MiscellaneousService miscellaneousService, @RequestParam(value="id",required=false) Integer id, Model model) {
		if( id != null ){
			miscellaneousService = Context.getService(BillingService.class).getMiscellaneousServiceById(id);
			model.addAttribute(miscellaneousService);
		}
		return "/module/billing/miscellaneousService/form";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(Context.getDateFormat(), true));
		binder.registerCustomEditor(java.lang.Boolean.class, new CustomBooleanEditor(BillingConstants.TRUE,
		        BillingConstants.FALSE, true));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(MiscellaneousService miscellaneousService, BindingResult bindingResult, HttpServletRequest request) {
		new MiscellaneousServiceValidator().validate(miscellaneousService, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/billing/miscellaneousService/form";
		}
		BillingService billingService = Context.getService(BillingService.class);
		if( miscellaneousService.getRetired()) {
			miscellaneousService.setRetiredDate(new Date());
		}		
		miscellaneousService.setCreatedDate(new Date());
		billingService.saveMiscellaneousService(miscellaneousService);
		return "redirect:/module/billing/miscellaneousService.list";
	}
	
}
