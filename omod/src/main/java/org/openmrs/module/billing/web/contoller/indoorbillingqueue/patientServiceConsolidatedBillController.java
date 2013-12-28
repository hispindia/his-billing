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
 *  author: Abhishek-Ankur
 *  date: 18-Dec-2013
 *  Requirement: Indoor Patient Billing
 */

package org.openmrs.module.billing.web.contoller.indoorbillingqueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("PatientServiceConsolidatedBillController")
public class patientServiceConsolidatedBillController {

	@RequestMapping(value = "/module/billing/patientServiceConsolidatedBill.form", method = RequestMethod.GET)
	public String viewBill(
			@RequestParam("patientId") Integer patientId, 
			@RequestParam("admissionDate") String admissionDate,
			@RequestParam("admittedWard") String admittedWard,
			@RequestParam("treatingDoctor") String treatingDoctor,
			Model model){
		
		BillingService billingService = (BillingService) Context.getService(BillingService.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(admissionDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Patient patient = Context.getPatientService().getPatient(patientId);
		List<PatientServiceBill> listPatientServiceBill = billingService.getPatientServiceByPatientId(patient, date);
		if (!listPatientServiceBill.isEmpty()) {
			model.addAttribute("listBill", listPatientServiceBill);
			model.addAttribute("patient", patient);
			model.addAttribute("admissionDate", date);
			model.addAttribute("admittedWard", admittedWard);
			model.addAttribute("treatingDoctor", treatingDoctor);
			return "module/billing/indoorqueue/patientServiceConsolidatedBill";
		} else {
			JOptionPane optionPane = new JOptionPane("No Bills Found for the Patient " + patient.getGivenName() + " " + patient.getMiddleName() + " " + patient.getFamilyName(),
                    JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Error!");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			return "redirect:/module/billing/indoorbillingqueue.form";
		}
	}
}
