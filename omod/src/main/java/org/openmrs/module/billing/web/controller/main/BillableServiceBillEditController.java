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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorService;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
@RequestMapping("/module/billing/editPatientServiceBill.form")
public class BillableServiceBillEditController {

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(Model model, @RequestParam("billId") Integer billId,
			@RequestParam("patientId") Integer patientId) {

		BillingService billingService = Context
				.getService(BillingService.class);
		List<BillableService> services = billingService.getAllServices();
		Map<Integer, BillableService> mapServices = new HashMap<Integer, BillableService>();
		for (BillableService ser : services) {
			mapServices.put(ser.getConceptId(), ser);
		}
		Integer conceptId = Integer.valueOf(Context.getAdministrationService()
				.getGlobalProperty("billing.rootServiceConceptId"));
		Concept concept = Context.getConceptService().getConcept(conceptId);
		model.addAttribute("tabs",
				billingService.traversTab(concept, mapServices, 1));
		model.addAttribute("patientId", patientId);
		PatientServiceBill bill = billingService
				.getPatientServiceBillById(billId);

		model.addAttribute("bill", bill);
		return "/module/billing/main/billableServiceBillEdit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Model model, Object command,
			BindingResult bindingResult, HttpServletRequest request,
			@RequestParam("cons") Integer[] cons,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("billId") Integer billId,
			@RequestParam("action") String action,
			@RequestParam(value = "description", required = false) String description) {

		validate(cons, bindingResult, request);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "module/billing/main/patientServiceBillEdit";
		}
		BillingService billingService = Context
				.getService(BillingService.class);

		PatientServiceBill bill = billingService
				.getPatientServiceBillById(billId);

		// Get the BillCalculator to calculate the rate of bill item the patient
		// has to pay
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);
		BillCalculatorService calculator = new BillCalculatorService();
		
		if("" != description)
			bill.setDescription(description);

		if ("void".equalsIgnoreCase(action)) {
			bill.setVoided(true);
			bill.setVoidedDate(new Date());
			for (PatientServiceBillItem item : bill.getBillItems()) {
				item.setVoided(true);
				item.setVoidedDate(new Date());
			}
			billingService.savePatientServiceBill(bill);
			return "redirect:/module/billing/patientServiceBill.list?patientId="
					+ patientId;
		}

		// void old items and reset amount
		Map<Integer, PatientServiceBillItem> mapOldItems = new HashMap<Integer, PatientServiceBillItem>();
		for (PatientServiceBillItem item : bill.getBillItems()) {
			item.setVoided(true);
			item.setVoidedDate(new Date());
			mapOldItems.put(item.getPatientServiceBillItemId(), item);
		}
		bill.setAmount(BigDecimal.ZERO);
		bill.setPrinted(false);

		PatientServiceBillItem item;
		int quantity = 0;
		Money itemAmount;
		Money mUnitPrice;
		Money totalAmount = new Money(BigDecimal.ZERO);
		BigDecimal totalActualAmount = new BigDecimal(0);
		BigDecimal unitPrice;
		String name;
		BillableService service;

		for (int conceptId : cons) {

			unitPrice = NumberUtils.createBigDecimal(request
					.getParameter(conceptId + "_unitPrice"));
			quantity = NumberUtils.createInteger(request.getParameter(conceptId
					+ "_qty"));
			name = request.getParameter(conceptId + "_name");
			service = billingService.getServiceByConceptId(conceptId);

			mUnitPrice = new Money(unitPrice);
			itemAmount = mUnitPrice.times(quantity);
			totalAmount = totalAmount.plus(itemAmount);

			String sItemId = request.getParameter(conceptId + "_itemId");

			if (sItemId == null) {
				item = new PatientServiceBillItem();

				// Get the ratio for each bill item
				Map<String, Object> parameters = HospitalCoreUtils
						.buildParameters("patient", patient, "attributes",
								attributes, "billItem", item);
				BigDecimal rate = calculator.getRate(parameters);

				item.setAmount(itemAmount.getAmount());
				item.setActualAmount(item.getAmount().multiply(rate));
				totalActualAmount = totalActualAmount.add(item
						.getActualAmount());
				item.setCreatedDate(new Date());
				item.setName(name);
				item.setPatientServiceBill(bill);
				item.setQuantity(quantity);
				item.setService(service);
				item.setUnitPrice(unitPrice);
				bill.addBillItem(item);
			} else {

				item = mapOldItems.get(Integer.parseInt(sItemId));

				// Get the ratio for each bill item
				Map<String, Object> parameters = HospitalCoreUtils
						.buildParameters("patient", patient, "attributes",
								attributes, "billItem", item);
				BigDecimal rate = calculator.getRate(parameters);

				item.setVoided(false);
				item.setVoidedDate(null);
				item.setQuantity(quantity);
				item.setAmount(itemAmount.getAmount());
				item.setActualAmount(item.getAmount().multiply(rate));
				totalActualAmount = totalActualAmount.add(item
						.getActualAmount());
			}
		}
		bill.setAmount(totalAmount.getAmount());
		bill.setActualAmount(totalActualAmount);

		// Determine whether the bill is free or not

		bill.setFreeBill(calculator.isFreeBill(HospitalCoreUtils
				.buildParameters("attributes", attributes)));
		logger.info("Is free bill: " + bill.getFreeBill());

		bill = billingService.savePatientServiceBill(bill);
		return "redirect:/module/billing/patientServiceBill.list?patientId="
				+ patientId + "&billId=" + billId;
	}

	private void validate(Integer[] ids, BindingResult binding,
			HttpServletRequest request) {
		for (int id : ids) {
			try {
				Integer.parseInt(request.getParameter(id + "_qty"));
			} catch (Exception e) {
				binding.reject("billing.bill.quantity.invalid",
						"Quantity is invalid");
				return;
			}
		}
	}
}
