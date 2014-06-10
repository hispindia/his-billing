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
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorForBDService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBill;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBillItem;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmitted;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.PatientUtils;
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
@RequestMapping("/module/billing/patientServiceBillForBD.list")
public class BillableServiceBillListForBDController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(Model model, @RequestParam("patientId") Integer patientId,
	                       @RequestParam(value = "billId", required = false) Integer billId,
	                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
	                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
	                       @RequestParam(value = "encounterId", required = false) Integer encounterId,
	                       @RequestParam(value = "admissionLogId", required = false) Integer admissionLogId,
	                       @RequestParam(value = "requestForDischargeStatus", required = false) Integer requestForDischargeStatus,
	                       HttpServletRequest request) {
		
		BillingService billingService = Context.getService(BillingService.class);
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);
		//ghanshyam 25-02-2013 New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module
		BillCalculatorForBDService calculator = new BillCalculatorForBDService();
		
		if (patient != null) {
			
			int total = billingService.countListPatientServiceBillByPatient(patient);
			// ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total,
			        patientId);
			
			model.addAttribute("age",patient.getAge());
			model.addAttribute("category",patient.getAttribute(14));
			model.addAttribute("gender",patient.getGender());
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("listBill",
			    billingService.listPatientServiceBillByPatient(pagingUtil.getStartPos(), pagingUtil.getPageSize(), patient));
		}
        User user = Context.getAuthenticatedUser();
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED));
		if (billId != null) {
			PatientServiceBill bill = billingService.getPatientServiceBillById(billId);
			//ghanshyam 25-02-2013 New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module
			//ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
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
			
			model.addAttribute("bill", bill);
		}
		
		if (encounterId != null) {
			List<IndoorPatientServiceBill> bills = billingService.getIndoorPatientServiceBillByEncounter(Context.getEncounterService().getEncounter(encounterId));
			model.addAttribute("billList", bills);
			model.addAttribute("requestForDischargeStatus", requestForDischargeStatus);
			return "/module/billing/indoorQueue/billListForIndoorPatient";
		}
		else{
			return "/module/billing/main/billableServiceBillListForBD";
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@RequestParam("patientId") Integer patientId, @RequestParam(value = "billId",required = false) Integer billId,
			@RequestParam(value = "encounterId", required = false) Integer encounterId,
			@RequestParam(value = "admissionLogId", required = false) Integer admissionLogId,
			@RequestParam(value = "waiverAmount", required = false) BigDecimal waiverAmount,
			@RequestParam(value = "paymentMode", required = false) String paymentMode,
			HttpServletRequest request) {
		if(encounterId!=null){
			BillingService billingService = Context.getService(BillingService.class);
			IpdService ipdService = Context.getService(IpdService.class);
			PatientService patientService = Context.getPatientService();
			Patient patient = patientService.getPatient(patientId);
			PatientServiceBill bill = new PatientServiceBill();
			
			bill.setCreatedDate(new Date());
			bill.setPatient(patient);
			bill.setCreator(Context.getAuthenticatedUser());
			
			PatientServiceBillItem item;
			Money totalAmount = new Money(BigDecimal.ZERO);
			Money mUnitPrice;
			Money itemAmount;
			BigDecimal totalActualAmount = new BigDecimal(0);
			
			List<IndoorPatientServiceBill> bills = billingService.getIndoorPatientServiceBillByEncounter(Context.getEncounterService().getEncounter(encounterId));
			
			for(IndoorPatientServiceBill ipsb:bills){
				
				for(IndoorPatientServiceBillItem ipsbi:ipsb.getBillItems()){
				mUnitPrice = new Money(ipsbi.getUnitPrice());
				itemAmount = mUnitPrice.times(ipsbi.getQuantity());
				totalAmount = totalAmount.plus(itemAmount);
				item = new PatientServiceBillItem();
				item.setCreatedDate(new Date());
				item.setName(ipsbi.getName());
				item.setPatientServiceBill(bill);
				item.setQuantity(ipsbi.getQuantity());
				item.setService(ipsbi.getService());
				item.setUnitPrice(ipsbi.getUnitPrice());
				item.setAmount(ipsbi.getAmount());
				item.setOrder(ipsbi.getOrder());
				item.setActualAmount(ipsbi.getActualAmount());
				totalActualAmount = totalActualAmount.add(item.getActualAmount());
				bill.addBillItem(item);
				}
			}
			bill.setAmount(totalAmount.getAmount());
			bill.setReceipt(billingService.createReceipt());
			bill.setFreeBill(0);
			//bill.setActualAmount(totalActualAmount.subtract(waiverAmount));
			bill.setActualAmount(totalActualAmount);
			if(waiverAmount != null){
				bill.setWaiverAmount(waiverAmount);
			}
			else {
				BigDecimal wavAmt = new BigDecimal(0);
				bill.setWaiverAmount(wavAmt);
			}
			bill.setEncounter(Context.getEncounterService().getEncounter(encounterId));	
			bill.setPaymentMode(paymentMode);
			bill = billingService.savePatientServiceBill(bill);
			
			if(bill!=null){
				for(IndoorPatientServiceBill ipsb:bills){
					billingService.deleteIndoorPatientServiceBill(ipsb);
				}
				IpdPatientAdmissionLog ipdPatientAdmissionLog = ipdService.getIpdPatientAdmissionLog(admissionLogId);
				ipdPatientAdmissionLog.setBillingStatus(1);
				IpdPatientAdmitted ipdPatientAdmitted = ipdService.getAdmittedByAdmissionLogId(ipdPatientAdmissionLog);
				ipdPatientAdmitted.setBillingStatus(1);
				ipdService.saveIpdPatientAdmissionLog(ipdPatientAdmissionLog);
				ipdService.saveIpdPatientAdmitted(ipdPatientAdmitted);
			}
			return "redirect:/module/billing/indoorPatientServiceBill.list?patientId=" + patientId + "&billId=" + bill.getPatientServiceBillId() +"&encounterId=" + encounterId;
		}
		else{
			BillingService billingService = (BillingService) Context.getService(BillingService.class);
			PatientServiceBill patientServiceBill = billingService.getPatientServiceBillById(billId);
			if (patientServiceBill != null && !patientServiceBill.getPrinted() && patientServiceBill.getEncounter()==null) {
				patientServiceBill.setPrinted(true);
				Map<String, String> attributes = PatientUtils.getAttributes(patientServiceBill.getPatient());
				//ghanshyam 25-02-2013 New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module
				BillCalculatorForBDService calculator = new BillCalculatorForBDService();
				
				//ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
				if (patientServiceBill.getFreeBill().equals(1)) {
					String billType = "free";
					patientServiceBill.setFreeBill(calculator.isFreeBill(billType));
				} else if (patientServiceBill.getFreeBill().equals(2)) {
					String billType = "mixed";
					patientServiceBill.setFreeBill(2);
				} else {
					String billType = "paid";
					patientServiceBill.setFreeBill(calculator.isFreeBill(billType));
				}
				
				billingService.saveBillEncounterAndOrder(patientServiceBill);
			}
			return "redirect:/module/billing/patientServiceBillForBD.list?patientId=" + patientId;
		}
	}
}
