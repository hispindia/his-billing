/**
 *  Copyright 2009 Health Information Systems Project of India
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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.PatientUtil;
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
@RequestMapping("/module/billing/patientServiceBill.list")
public class BillableServiceBillListController {

	@RequestMapping(method=RequestMethod.GET)
	public String viewForm( Model model, @RequestParam("patientId") Integer patientId, @RequestParam(value="billId",required=false) Integer billId
	                        ,@RequestParam(value="pageSize",required=false)  Integer pageSize, 
		                    @RequestParam(value="currentPage",required=false)  Integer currentPage,
		                    HttpServletRequest request){
		
		BillingService billingService = Context.getService(BillingService.class);
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtil.getAttributes(patient);
		BillCalculatorService calculator = new BillCalculatorService();		
		model.addAttribute("freeBill", calculator.isFreeBill(HospitalCoreUtils.buildParameters("attributes", attributes)));
		
		if( patient != null ){
			
			int total = billingService.countListPatientServiceBillByPatient(patient);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("listBill", billingService.listPatientServiceBillByPatient(pagingUtil.getStartPos(), pagingUtil.getPageSize(), patient));
		}
		if( billId != null ){
			PatientServiceBill bill = billingService.getPatientServiceBillById(billId);			
			
			bill.setFreeBill(calculator.isFreeBill(HospitalCoreUtils.buildParameters("attributes", attributes)));
			model.addAttribute("bill", bill);
		}
		User user = Context.getAuthenticatedUser();
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );		
		return "/module/billing/main/billableServiceBillList";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(@RequestParam("patientId") Integer patientId, @RequestParam("billId") Integer billId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
    	PatientServiceBill patientSerciceBill = billingService.getPatientServiceBillById(billId);
    	if( patientSerciceBill != null && !patientSerciceBill.getPrinted()){
    		patientSerciceBill.setPrinted(true);
    		Map<String, String> attributes = PatientUtil.getAttributes(patientSerciceBill.getPatient());
			BillCalculatorService calculator = new BillCalculatorService();
			patientSerciceBill.setFreeBill(calculator.isFreeBill(HospitalCoreUtils.buildParameters("attributes", attributes)));			
    		billingService.saveBillEncounterAndOrder(patientSerciceBill);
    	}
		return "redirect:/module/billing/patientServiceBill.list?patientId="+patientId;
	}
}
