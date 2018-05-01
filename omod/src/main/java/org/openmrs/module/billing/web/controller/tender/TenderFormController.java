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
