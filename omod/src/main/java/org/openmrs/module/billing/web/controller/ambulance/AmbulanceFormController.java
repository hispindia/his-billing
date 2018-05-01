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

package org.openmrs.module.billing.web.controller.ambulance;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
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
		binder.registerCustomEditor(java.lang.Boolean.class, new CustomBooleanEditor("true",
		        "false", true));
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
