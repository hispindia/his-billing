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
