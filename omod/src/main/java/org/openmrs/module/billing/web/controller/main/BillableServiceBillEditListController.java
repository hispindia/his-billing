/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author ghanshyam
 **/

package org.openmrs.module.billing.web.controller.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.util.HospitalCoreUtils;
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
@RequestMapping("/module/billing/patientServiceBillEdit.list")
public class BillableServiceBillEditListController {

	@RequestMapping(method=RequestMethod.GET)
	public String viewForm( Model model, @RequestParam("patientId") Integer patientId, @RequestParam(value="billId",required=false) Integer billId
	                        ,@RequestParam(value="pageSize",required=false)  Integer pageSize, 
		                    @RequestParam(value="currentPage",required=false)  Integer currentPage,
		                    HttpServletRequest request){
		
		BillingService billingService = Context.getService(BillingService.class);
		HospitalCoreService hospitalCoreService = Context.getService(HospitalCoreService.class);
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);
		BillCalculatorService calculator = new BillCalculatorService();		
		//New Requirement add Paid bill & Free bill Both
		//model.addAttribute("freeBill", calculator.isFreeBill(HospitalCoreUtils.buildParameters("attributes", attributes)));
		
		if( patient != null ){
			
			int total = billingService.countListPatientServiceBillByPatient(patient);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("listBill", billingService.listPatientServiceBillByPatient(pagingUtil.getStartPos(), pagingUtil.getPageSize(), patient));
		}
		if( billId != null ){
			PatientServiceBill bill = billingService.getPatientServiceBillById(billId);			
			//New Requirement add Paid bill & Free bill Both
		//	bill.setFreeBill(calculator.isFreeBill(HospitalCoreUtils.buildParameters("attributes", attributes)));
			model.addAttribute("bill", bill);
		}
		User user = Context.getAuthenticatedUser();
		
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
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );		
		return "/module/billing/main/billableServiceBillList";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(@RequestParam("patientId") Integer patientId, @RequestParam("billId") Integer billId){
		
		return "redirect:/module/billing/patientServiceBill.list?patientId="+patientId;
	}
}
