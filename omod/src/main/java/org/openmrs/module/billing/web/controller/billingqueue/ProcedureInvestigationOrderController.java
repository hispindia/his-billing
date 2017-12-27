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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorService;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("ProcedureInvestigationOrderController")
@RequestMapping("/module/billing/procedureinvestigationorder.form")
public class ProcedureInvestigationOrderController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(Model model, @RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam(value = "date", required = false) String dateStr) {
		BillingService billingService = Context.getService(BillingService.class);
		List<BillableService> serviceOrderList = billingService.listOfServiceOrder(patientId,encounterId);
		model.addAttribute("serviceOrderList", serviceOrderList);
		model.addAttribute("serviceOrderSize", serviceOrderList.size());
		model.addAttribute("patientId", patientId);
		model.addAttribute("encounterId", encounterId);
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		PatientSearch patientSearch = hospitalCoreService.getPatientByPatientId(patientId);
		Patient patient = Context.getPatientService().getPatient(patientId);
		model.addAttribute("age",patient.getAge());
		model.addAttribute("fileNumber",patient.getAttribute(43));
		if(patient.getGender().equals("M"))
		{
			model.addAttribute("gender","Male");
		}
		if(patient.getGender().equals("F"))
		{
			model.addAttribute("gender","Female");
		}

		model.addAttribute("patientSearch", patientSearch);
		model.addAttribute("date", dateStr);
		
		List<PersonAttribute> pas = hospitalCoreService.getPersonAttributes(patientId);
		Concept conceptPaidCategory=Context.getConceptService().getConceptByName("Paid Category");
		Collection<ConceptAnswer> cpcAns=conceptPaidCategory.getAnswers();
		List<String> conceptListForPaidCategory = new ArrayList<String>();
		for(ConceptAnswer cpc:cpcAns){
			conceptListForPaidCategory.add(cpc.getAnswerConcept().getId().toString());
		}
		
		Concept conceptPrograms=Context.getConceptService().getConceptByName("Programs");
		Collection<ConceptAnswer> cpAns=conceptPrograms.getAnswers();
		List<String> conceptListForPrograms = new ArrayList<String>();
		for(ConceptAnswer cp:cpAns){
			conceptListForPrograms.add(cp.getAnswerConcept().getId().toString());
		}
		
		for (PersonAttribute pa : pas) {
			PersonAttributeType attributeType = pa.getAttributeType();
			if (attributeType.getPersonAttributeTypeId() == 14 && conceptListForPaidCategory.contains(pa.getValue())) {
				model.addAttribute("selectedCategory", pa.getValue());
				model.addAttribute("category", "Paid Category");
				model.addAttribute("subCategory", Context.getConceptService().getConcept(Integer.parseInt(pa.getValue())));
			}
			else if(attributeType.getPersonAttributeTypeId() == 14 && conceptListForPrograms.contains(pa.getValue())){
				model.addAttribute("selectedCategory", pa.getValue());	
				model.addAttribute("category", "Programs");
				model.addAttribute("subCategory", Context.getConceptService().getConcept(Integer.parseInt(pa.getValue())));
			}
			
			if (attributeType.getPersonAttributeTypeId() == 31) {
				model.addAttribute("childCategory", Context.getConceptService().getConcept(Integer.parseInt(pa.getValue())));
			}
	      }
		return "/module/billing/queue/procedureInvestigationOrder";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Model model, Object command,
			HttpServletRequest request,
			@RequestParam("patientId") Integer patientId,
			@RequestParam("encounterId") Integer encounterId,
			@RequestParam("indCount") Integer indCount,
			@RequestParam(value= "waiverComment", required = false) String waiverComment,
			@RequestParam(value = "total", required = false) float total) {

		BillingService billingService = Context.getService(BillingService.class);

		PatientDashboardService patientDashboardService = Context.getService(PatientDashboardService.class);

		PatientService patientService = Context.getPatientService();

		// Get the BillCalculator to calculate the rate of bill item the patient has to pay
		Patient patient = patientService.getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);

		BillCalculatorService calculator = new BillCalculatorService();

		PatientServiceBill bill = new PatientServiceBill();
		bill.setCreatedDate(new Date());
		bill.setPatient(patient);
		bill.setCreator(Context.getAuthenticatedUser());

		PatientServiceBillItem item;
		String servicename;
		int quantity = 0;
		String selectservice;
		BigDecimal unitPrice;
		String reschedule;
		String paybill;
		BillableService service;
		Money mUnitPrice;
		Money itemAmount;
		Money totalAmount = new Money(BigDecimal.ZERO);
		BigDecimal rate;
		String billTyp;
		BigDecimal totalActualAmount = new BigDecimal(0);
		OpdTestOrder opdTestOrder=new OpdTestOrder();
		HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
		List<PersonAttribute> pas = hcs.getPersonAttributes(patientId);
		String patientCategory = null;
		for (PersonAttribute pa : pas) {
			PersonAttributeType attributeType = pa.getAttributeType();
			if (attributeType.getPersonAttributeTypeId() == 14) {
				patientCategory=pa.getValue();
			}
		}

		String credit=request.getParameter("credit");
		
		if(credit!=null){
			for (Integer i = 1; i <= indCount; i++) {
				selectservice = request.getParameter(i.toString() + "selectservice");
				if("billed".equals(selectservice)){
				servicename = request.getParameter(i.toString() + "service");
				quantity = NumberUtils.createInteger(request.getParameter(i.toString()+ "servicequantity"));
				reschedule = request.getParameter(i.toString() + "reschedule");
				paybill = request.getParameter(i.toString() + "paybill");
				unitPrice = NumberUtils.createBigDecimal(request.getParameter(i.toString() + "unitprice"));
				service = billingService.getServiceByConceptName(servicename);

				mUnitPrice = new Money(unitPrice);
				itemAmount = mUnitPrice.times(quantity);
				totalAmount = totalAmount.plus(itemAmount);

				item = new PatientServiceBillItem();
				item.setCreatedDate(new Date());
				item.setName(servicename);
				item.setPatientServiceBill(bill);
				item.setQuantity(quantity);
				item.setService(service);
				item.setUnitPrice(unitPrice);

				item.setAmount(itemAmount.getAmount());


				// Get the ratio for each bill item
				Map<String, Object> parameters = HospitalCoreUtils.buildParameters(
						"patient", patient, "attributes", attributes, "billItem",
						item, "request", request);

				//rate = calculator.getRate(parameters, billTyp);
				rate=new BigDecimal(1);
				item.setActualAmount(item.getAmount().multiply(rate));
				totalActualAmount = totalActualAmount.add(item.getActualAmount());
				bill.addBillItem(item);

				opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
				opdTestOrder.setBillingStatus(1);
				patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);

			  }
				else{
					servicename = request.getParameter(i.toString() + "service");
					service = billingService.getServiceByConceptName(servicename);
					opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
					opdTestOrder.setCancelStatus(1);
					patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
				}
			}

			bill.setAmount(totalAmount.getAmount());
			bill.setActualAmount(totalActualAmount);
			bill.setPatientCategory(patientCategory);
			bill.setComment(waiverComment);
			bill.setBillType("out/credit");
			bill.setReceipt(billingService.createReceipt());	
		}
		else{
			float waiverPercentage=Float.parseFloat(request.getParameter("waiverPercentage"));
			BigDecimal totalAmountPayable = new BigDecimal(request.getParameter("totalAmountPayable"));
			Integer amountGiven=Integer.parseInt(request.getParameter("amountGiven"));
			Integer amountReturned=Integer.parseInt(request.getParameter("amountReturned"));
			for (Integer i = 1; i <= indCount; i++) {
				selectservice = request.getParameter(i.toString() + "selectservice");
				if("billed".equals(selectservice)){
				servicename = request.getParameter(i.toString() + "service");
				quantity = NumberUtils.createInteger(request.getParameter(i.toString()+ "servicequantity"));
				reschedule = request.getParameter(i.toString() + "reschedule");
				paybill = request.getParameter(i.toString() + "paybill");
				unitPrice = NumberUtils.createBigDecimal(request.getParameter(i.toString() + "unitprice"));
				service = billingService.getServiceByConceptName(servicename);

				mUnitPrice = new Money(unitPrice);
				itemAmount = mUnitPrice.times(quantity);
				totalAmount = totalAmount.plus(itemAmount);

				item = new PatientServiceBillItem();
				item.setCreatedDate(new Date());
				item.setName(servicename);
				item.setPatientServiceBill(bill);
				item.setQuantity(quantity);
				item.setService(service);
				item.setUnitPrice(unitPrice);

				item.setAmount(itemAmount.getAmount());


				// Get the ratio for each bill item
				Map<String, Object> parameters = HospitalCoreUtils.buildParameters(
						"patient", patient, "attributes", attributes, "billItem",
						item, "request", request);

				//rate = calculator.getRate(parameters, billTyp);
				rate=new BigDecimal(1);
				item.setActualAmount(item.getAmount().multiply(rate));
				totalActualAmount = totalActualAmount.add(item.getActualAmount());
				bill.addBillItem(item);

				opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
				opdTestOrder.setBillingStatus(1);
				patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);

			  }
				else{
					servicename = request.getParameter(i.toString() + "service");
					service = billingService.getServiceByConceptName(servicename);
					opdTestOrder=billingService.getOpdTestOrder(encounterId,service.getConceptId());
					opdTestOrder.setCancelStatus(1);
					patientDashboardService.saveOrUpdateOpdOrder(opdTestOrder);
				}
			}

			bill.setAmount(totalAmount.getAmount());
			bill.setActualAmount(totalActualAmount);
			bill.setPatientCategory(patientCategory);
			bill.setWaiverPercentage(waiverPercentage);
			float waiverAmount=total*waiverPercentage/100;
			bill.setWaiverAmount(waiverAmount);
			bill.setAmountPayable(totalAmountPayable);
			bill.setAmountGiven(amountGiven);
			bill.setAmountReturned(amountReturned);
			bill.setComment(waiverComment);
			bill.setBillType("out/paid");
			bill.setReceipt(billingService.createReceipt());
			
		}
		bill = billingService.savePatientServiceBill(bill);

		return "redirect:/module/billing/patientServiceBillForOrder.list?patientId=" + patientId + "&billId="
        + bill.getPatientServiceBillId();
	}
}
