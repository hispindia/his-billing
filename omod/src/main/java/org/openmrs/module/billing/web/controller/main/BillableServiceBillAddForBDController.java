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
 *  author: Ghanshyam
 *  date:   25-02-2013
 *  New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module
 **/

package org.openmrs.module.billing.web.controller.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptSet;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorForBDService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBill;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBillItem;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmission;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.module.hospitalcore.util.ConceptSetComparator;
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
@RequestMapping("/module/billing/addPatientServiceBillForBD.form")
public class BillableServiceBillAddForBDController {
	   ArrayList<String> arrayReturn=new ArrayList<String>();
	private Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(Model model, @RequestParam("patientId") Integer patientId,
	                       @RequestParam(value = "comment", required = false) String comment,
	                       @RequestParam(value = "billType", required = false) String billType,
	                       @RequestParam(value = "encounterId", required = false) Integer encounterId,
	                       @RequestParam(value = "typeOfPatient", required = false) String typeOfPatient) {
		BillingService billingService = Context.getService(BillingService.class);
		List<BillableService> services = billingService.getAllServices();
		Map<Integer, BillableService> mapServices = new HashMap<Integer, BillableService>();
		for (BillableService ser : services) {
			mapServices.put(ser.getConceptId(), ser);
		}
		Integer conceptId = Integer.valueOf(Context.getAdministrationService().getGlobalProperty(
		    "billing.rootServiceConceptId"));
		Concept concept = Context.getConceptService().getConcept(conceptId);
		model.addAttribute("tabs", billingService.traversTab(concept, mapServices, 1));

                model.addAttribute("patientId", patientId);
		if(typeOfPatient!=null){
			return "/module/billing/indoorQueue/billableServiceBillAddForIndoorPatient";	
		}
		else{
			return "/module/billing/main/billableServiceBillAdd";
		}
	}
     
        
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(Model model, Object command, BindingResult bindingResult, HttpServletRequest request,
	                       @RequestParam("cons") Integer[] cons, @RequestParam("patientId") Integer patientId,
	                       @RequestParam(value = "comment", required = false) String comment,
	                       @RequestParam(value = "waiverAmount", required = false) BigDecimal waiverAmount,
	                       @RequestParam(value = "paymentMode", required = false) String paymentMode,
	                       @RequestParam(value = "billType", required = false) String billType,
	                       @RequestParam(value = "encounterId", required = false) Integer encounterId) {
		validate(cons, bindingResult, request);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "module/billing/main/billableServiceBillEdit";
		}
		
		if(encounterId!=null){
		BillingService billingService = Context.getService(BillingService.class);
		IpdService ipdService = Context.getService(IpdService.class);
		PatientService patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(patientId);
		IndoorPatientServiceBill bill = new IndoorPatientServiceBill();
		bill.setCreatedDate(new Date());
		bill.setPatient(patient);
		bill.setCreator(Context.getAuthenticatedUser());
		
		IndoorPatientServiceBillItem item;
		int quantity = 0;
		Money itemAmount;
		Money mUnitPrice;
		Money totalAmount = new Money(BigDecimal.ZERO);
		BigDecimal totalActualAmount = new BigDecimal(0);
		BigDecimal unitPrice;
		String name;
		BillableService service;
		
		for (int conceptId : cons) {
			
			unitPrice = NumberUtils.createBigDecimal(request.getParameter(conceptId + "_unitPrice"));
			quantity = NumberUtils.createInteger(request.getParameter(conceptId + "_qty"));
			name = request.getParameter(conceptId + "_name");
			service = billingService.getServiceByConceptId(conceptId);
			
			mUnitPrice = new Money(unitPrice);
			itemAmount = mUnitPrice.times(quantity);
			totalAmount = totalAmount.plus(itemAmount);
			
			item = new IndoorPatientServiceBillItem();
			item.setCreatedDate(new Date());
			item.setName(name);
			item.setIndoorPatientServiceBill(bill);
			item.setQuantity(quantity);
			item.setService(service);
			item.setUnitPrice(unitPrice);
			item.setAmount(itemAmount.getAmount());
			item.setActualAmount(itemAmount.getAmount());
			item.setOrderType("SERVICE");
			totalActualAmount = totalActualAmount.add(item.getActualAmount());
			
			bill.addBillItem(item);
		}
		bill.setAmount(totalAmount.getAmount());
		bill.setActualAmount(totalActualAmount);
		bill.setEncounter(Context.getEncounterService().getEncounter(encounterId));	
		bill = billingService.saveIndoorPatientServiceBill(bill);
		List<IndoorPatientServiceBill> indoorPatientServiceBillList = billingService.getIndoorPatientServiceBillByEncounter(Context.getEncounterService().getEncounter(encounterId));
		IndoorPatientServiceBillItem indoorPatientServiceBillItem = billingService.getIndoorPatientServiceBillItem("ADMISSION FILE CHARGES",indoorPatientServiceBillList);
		IpdPatientAdmission ipdPatientAdmission = ipdService.getIpdPatientAdmissionByEncounter(Context.getEncounterService().getEncounter(encounterId));
		if (indoorPatientServiceBillItem !=null && ipdPatientAdmission != null) {
		ipdPatientAdmission.setInitialDepositStatus(1);
		ipdService.saveIpdPatientAdmission(ipdPatientAdmission);
		}
		if (bill != null) {
			billingService.saveBillEncounterAndOrderForIndoorPatient(bill);
		}
			return "redirect:/module/billing/ipdbillingqueue.form";
		
	  }
		else{
			BillingService billingService = Context.getService(BillingService.class);
			
			PatientService patientService = Context.getPatientService();
			
			// Get the BillCalculator to calculate the rate of bill item the patient has to pay
			Patient patient = patientService.getPatient(patientId);
			Map<String, String> attributes = PatientUtils.getAttributes(patient);
			
			BillCalculatorForBDService calculator = new BillCalculatorForBDService();
			
			PatientServiceBill bill = new PatientServiceBill();
			bill.setCreatedDate(new Date());
			bill.setPatient(patient);
			bill.setCreator(Context.getAuthenticatedUser());
		
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
				
				unitPrice = NumberUtils.createBigDecimal(request.getParameter(conceptId + "_unitPrice"));
				quantity = NumberUtils.createInteger(request.getParameter(conceptId + "_qty"));
				name = request.getParameter(conceptId + "_name");
				service = billingService.getServiceByConceptId(conceptId);
				
				mUnitPrice = new Money(unitPrice);
				itemAmount = mUnitPrice.times(quantity);
				totalAmount = totalAmount.plus(itemAmount);
				
				item = new PatientServiceBillItem();
				item.setCreatedDate(new Date());
				item.setName(name);
				item.setPatientServiceBill(bill);
				item.setQuantity(quantity);
				item.setService(service);
				item.setUnitPrice(unitPrice);
				
				item.setAmount(itemAmount.getAmount());
				
				// Get the ratio for each bill item
				Map<String, Object> parameters = HospitalCoreUtils.buildParameters("patient", patient, "attributes", attributes,
				    "billItem", item, "request", request);
				BigDecimal rate = calculator.getRate(parameters, billType);
				item.setActualAmount(item.getAmount().multiply(rate));
				totalActualAmount = totalActualAmount.add(item.getActualAmount());
				
				bill.addBillItem(item);
			}
			bill.setAmount(totalAmount.getAmount());
			bill.setActualAmount(totalActualAmount);
			/*added waiver amount */
			if(waiverAmount != null){
				bill.setWaiverAmount(waiverAmount);
			}
			else {
				BigDecimal wavAmt = new BigDecimal(0);
				bill.setWaiverAmount(wavAmt);
			}
			bill.setComment(comment);
			
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			List<PersonAttribute> pas = hcs.getPersonAttributes(patientId);
			String patientSubCategory = null;
	       for (PersonAttribute pa : pas) {
	            PersonAttributeType attributeType = pa.getAttributeType();
	            PersonAttributeType personAttributePCT=hcs.getPersonAttributeTypeByName("Paying Category Type");
	            PersonAttributeType personAttributeNPCT=hcs.getPersonAttributeTypeByName("Non-Paying Category Type");
	            PersonAttributeType personAttributeSSCT=hcs.getPersonAttributeTypeByName("Special Scheme Category Type");
	            if(attributeType.getPersonAttributeTypeId()==personAttributePCT.getPersonAttributeTypeId()){
	            	patientSubCategory = pa.getValue();
	            }
	            else if(attributeType.getPersonAttributeTypeId()==personAttributeNPCT.getPersonAttributeTypeId()){
	            	patientSubCategory = pa.getValue();
	            }
	            else if(attributeType.getPersonAttributeTypeId()==personAttributeSSCT.getPersonAttributeTypeId()){
	            	patientSubCategory = pa.getValue();
	            }
	        }
		
			bill.setPatientSubCategory(patientSubCategory);
			
			bill.setPaymentMode(paymentMode);

			bill.setFreeBill(calculator.isFreeBill(billType));
			logger.info("Is free bill: " + bill.getFreeBill());
			
			bill.setReceipt(billingService.createReceipt());
			bill = billingService.savePatientServiceBill(bill);
			return "redirect:/module/billing/patientServiceBillForBD.list?patientId=" + patientId + "&billId="
	        + bill.getPatientServiceBillId() + "&billType=" + billType;
		}
		
	}
	
	private void validate(Integer[] ids, BindingResult binding, HttpServletRequest request) {
		for (int id : ids) {
			try {
				Integer.parseInt(request.getParameter(id + "_qty"));
			}
			catch (Exception e) {
				binding.reject("billing.bill.quantity.invalid", "Quantity is invalid");
				return;
			}
		}
	}
}
