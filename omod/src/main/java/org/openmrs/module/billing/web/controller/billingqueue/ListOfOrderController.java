/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author: ghanshyam
 *  date: 3-june-2013
 *  issue no: #1632
 **/

package org.openmrs.module.billing.web.controller.billingqueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ListOfOrderController")
@RequestMapping("/module/billing/listoforder.form")
public class ListOfOrderController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(Model model, @RequestParam("patientId") Integer patientId,
			@RequestParam(value = "date", required = false) String dateStr) {
		BillingService billingService = Context
				.getService(BillingService.class);
		PatientService patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(patientId);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		List<OpdTestOrder> listOfOrders = billingService.listOfOrder(patientId,date);
		// Kesavulu loka 25-06-2013, Add Patient Details on the page where Order ID is clicked
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = hospitalCoreService.getPatientByPatientId(patientId);
                
		model.addAttribute("age",patient.getAge());
		
		if(patient.getGender().equals("M"))
		{
			model.addAttribute("gender","Male");
		}
		if(patient.getGender().equals("F"))
		{
			model.addAttribute("gender","Female");
		}
		model.addAttribute("category",patient.getAttribute(14));
		model.addAttribute("fileNumber",patient.getAttribute(43));
		/*
		if(patient.getAttribute(14).getValue() == "Waiver"){
			model.addAttribute("exemption", patient.getAttribute(32));
		}
		else if(patient.getAttribute(14).getValue()!="General" && patient.getAttribute(14).getValue()!="Waiver"){
			model.addAttribute("exemption", patient.getAttribute(36));
		}
		else {
			model.addAttribute("exemption", " ");
		}
		*/
		model.addAttribute("patientSearch", patientSearch);
		model.addAttribute("listOfOrders", listOfOrders);
		//model.addAttribute("serviceOrderSize", serviceOrderList.size());
		model.addAttribute("patientId", patientId);
		model.addAttribute("date", dateStr);   
                
                
		return "/module/billing/queue/listOfOrder";
	}
}
