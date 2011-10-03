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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.AmbulanceBill;
import org.openmrs.module.hospitalcore.model.MiscellaneousServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.TenderBill;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@Controller
@RequestMapping("/module/billing/main.form")
public class MainController {

	private Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(method=RequestMethod.GET)
	public String main(Model model){
		
		String prefix = Context.getAdministrationService().getGlobalProperty("registration.identifier_prefix");
		model.addAttribute("idPrefix", prefix);
		
//		BillingService billingService = Context.getService(BillingService.class);
//		billingService.updateReceipt();	
		return "/module/billing/main/mainPage";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submit(Model model, @RequestParam("identifier") String identifier){
		
		String prefix = Context.getAdministrationService().getGlobalProperty("registration.identifier_prefix");
		if( identifier.contains("-") && !identifier.contains(prefix)){
			identifier = prefix+identifier;
    	}
		List<Patient> patientsList = Context.getPatientService().getPatients( identifier.trim() );
		model.addAttribute("patients", patientsList);
			
		return "/module/billing/main/mainPage";
	}
}
