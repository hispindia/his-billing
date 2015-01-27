/**
 *  Copyright 2014 Society for Health Information Systems Programmes, India (HISP India)
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

package org.openmrs.module.billing.web.controller.indoorbillingqueue;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorForBDService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmitted;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
@RequestMapping("/module/billing/indoorPatientServiceBill.list")
public class IndoorPatientServiceBillController {

	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(
			Model model,
			@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "billId", required = false) Integer billId,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "currentPage", required = false) Integer currentPage,
			@RequestParam(value = "encounterId", required = false) Integer encounterId,
			@RequestParam(value = "admissionLogId", required = false) Integer admissionLogId,
			HttpServletRequest request) {

		BillingService billingService = Context
				.getService(BillingService.class);
		Patient patient = Context.getPatientService().getPatient(patientId);
		BillCalculatorForBDService calculator = new BillCalculatorForBDService();
		
		IpdService ipdService = Context.getService(IpdService.class);
		IpdPatientAdmissionLog ipdPatientAdmissionLog = ipdService.getIpdPatientAdmissionLog(admissionLogId);
		IpdPatientAdmitted ipdPatientAdmitted = ipdService.getAdmittedByAdmissionLogId(ipdPatientAdmissionLog);

		PersonAttribute fileNumber = patient.getAttribute(43);
		if(fileNumber!=null){
			model.addAttribute("fileNumber", fileNumber.getValue());					
		}
		
		if (patient != null) {
			int total = billingService
					.countListPatientServiceBillByPatient(patient);
			PagingUtil pagingUtil = new PagingUtil(
					RequestUtil.getCurrentLink(request), pageSize, currentPage,
					total, patientId);
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("age", patient.getAge());
			if(patient.getGender().equals("M")){
				model.addAttribute("gender", "Male");
			}
			if(patient.getGender().equals("F")){
				model.addAttribute("gender", "Female");
			}
			model.addAttribute("category", patient.getAttribute(14).getValue());

			model.addAttribute("listBill", billingService
					.listPatientServiceBillByPatient(pagingUtil.getStartPos(),
							pagingUtil.getPageSize(), patient));
		}
		User user = Context.getAuthenticatedUser();

		model.addAttribute("canEdit",
				user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED));
		if (billId != null) {
			PatientServiceBill bill = billingService
					.getPatientServiceBillById(billId);
			PatientServiceBillItem patientServiceBillItem = billingService.getPatientServiceBillItem(billId,"ADMISSION FILE CHARGES");
			if (bill.getFreeBill().equals(1)) {
				String billType = "free";
				bill.setFreeBill(calculator.isFreeBill(billType));
			} else if (bill.getFreeBill().equals(2)) {
				String billType = "mixed";
				bill.setFreeBill(2);
			} else {
				String billType = "paid";
				bill.setFreeBill(calculator.isFreeBill(billType));
			}
			model.addAttribute("paymentMode",bill.getPaymentMode());
			model.addAttribute("cashier",bill.getCreator().getGivenName());
			model.addAttribute("bill", bill);
			model.addAttribute("billItem", patientServiceBillItem);
		}
		return "/module/billing/indoorQueue/indoorPatientServiceBill";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(
			@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "billId", required = false) Integer billId,
			@RequestParam(value = "encounterId", required = false) Integer encounterId) {
		if (encounterId != null) {
			BillingService billingService = (BillingService) Context
					.getService(BillingService.class);
			PatientServiceBill patientServiceBill = billingService
					.getPatientServiceBillById(billId);
			if (patientServiceBill != null && !patientServiceBill.getPrinted()) {
				patientServiceBill.setPrinted(true);
				BillCalculatorForBDService calculator = new BillCalculatorForBDService();
				if (patientServiceBill.getFreeBill().equals(1)) {
					String billType = "free";
					patientServiceBill.setFreeBill(calculator
							.isFreeBill(billType));
				} else if (patientServiceBill.getFreeBill().equals(2)) {
					String billType = "mixed";
					patientServiceBill.setFreeBill(2);
				} else {
					String billType = "paid";
					patientServiceBill.setFreeBill(calculator
							.isFreeBill(billType));
				}

				billingService.savePatientServiceBill(patientServiceBill);
			}
		}

		return "redirect:/module/billing/ipdbillingqueue.form";
	}
}
