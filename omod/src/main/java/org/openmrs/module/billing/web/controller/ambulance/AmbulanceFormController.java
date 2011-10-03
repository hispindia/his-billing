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
package org.openmrs.module.billing.web.controller.ambulance;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Ambulance;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
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
@RequestMapping("/module/billing/ambulance.form")
public class AmbulanceFormController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.lang.Boolean.class, new CustomBooleanEditor(BillingConstants.TRUE,
		        BillingConstants.FALSE, true));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String firstView(@ModelAttribute("ambulance") Ambulance ambulance, @RequestParam(value="ambulanceId",required=false) Integer id, Model model) {
		if( id != null ){
			ambulance = Context.getService(BillingService.class).getAmbulanceById(id);
			model.addAttribute(ambulance);
		}
		return "/module/billing/ambulance/form";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Ambulance ambulance, BindingResult bindingResult, HttpServletRequest request) {
		
		new AmbulanceValidator().validate(ambulance, bindingResult);
		if (bindingResult.hasErrors()) {
			return "/module/billing/ambulance/form";
		}
		BillingService billingService = Context.getService(BillingService.class);
		if( ambulance.getRetired() ){
			ambulance.setRetiredDate(new Date());
		}else{
			ambulance.setCreatedDate(new Date());
		}
		billingService.saveAmbulance(ambulance);
		return "redirect:/module/billing/ambulance.list";
	}
	
}
