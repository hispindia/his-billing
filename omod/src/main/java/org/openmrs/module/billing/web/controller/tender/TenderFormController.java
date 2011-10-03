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
package org.openmrs.module.billing.web.controller.tender;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Tender;
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
@RequestMapping("/module/billing/tender.form")
public class TenderFormController {
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("tender") Tender tender, @RequestParam(value="tenderId",required=false) Integer id, Model model) {
		if( id != null ){
			tender = Context.getService(BillingService.class).getTenderById(id);
			model.addAttribute(tender);
		}
		return "/module/billing/tender/form";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(Context.getDateFormat(), true));
		binder.registerCustomEditor(java.lang.Boolean.class, new CustomBooleanEditor(BillingConstants.TRUE,
		        BillingConstants.FALSE, true));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Tender tender, BindingResult bindingResult, HttpServletRequest request) {
		
		new TenderValidator().validate(tender, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/billing/tender/form";
		}
		BillingService billingService = Context.getService(BillingService.class);
		if( tender.getRetired()) {
			tender.setRetiredDate(new Date());
		}
		billingService.saveTender(tender);
		return "redirect:/module/billing/tender.list";
	}
	
}
