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
@Controller("BillableServiceBillListForOrderController")
@RequestMapping("/module/billing/patientServiceBillForOrder.list")
public class BillableServiceBillListForOrderController {

	@RequestMapping(method=RequestMethod.GET)
	public String viewForm( Model model, @RequestParam("patientId") Integer patientId, @RequestParam(value="billId",required=false) Integer billId
	                        ,@RequestParam(value="pageSize",required=false)  Integer pageSize,
	                        @RequestParam(value = "encounterId", required = false) Integer encounterId,
		                    @RequestParam(value="currentPage",required=false)  Integer currentPage,
		                    HttpServletRequest request){
		
		BillingService billingService = Context.getService(BillingService.class);
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);
		BillCalculatorService calculator = new BillCalculatorService();		
	
		if( patient != null ){
			int total = billingService.countListPatientServiceBillByPatient(patient);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total, patientId);
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("listBill", billingService.listPatientServiceBillByPatient(pagingUtil.getStartPos(), pagingUtil.getPageSize(), patient));
		
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			List<PersonAttribute> pas = hcs.getPersonAttributes(patientId);
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
		}
		
		PatientServiceBill bill = null;
		if( billId != null ){
			bill = billingService.getPatientServiceBillById(billId);			
						model.addAttribute("bill", bill);
					}
		User user = Context.getAuthenticatedUser();
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );	
		return "/module/billing/main/billableServiceBillListForOrder";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(@RequestParam("patientId") Integer patientId,
			@RequestParam(value = "comment", required = false) String comment,
			@RequestParam("billId") Integer billId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
		PatientServiceBill bill = new PatientServiceBill();
    	PatientServiceBill patientServiceBill = billingService.getPatientServiceBillById(billId);
    	if( patientServiceBill != null && !patientServiceBill.getPrinted()){
    		patientServiceBill.setPrinted(true);
    		Map<String, String> attributes = PatientUtils.getAttributes(patientServiceBill.getPatient());
			BillCalculatorService calculator = new BillCalculatorService();
			
    		billingService.saveBillEncounterAndOrder(patientServiceBill);
    	}
    	
    	HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
    	
		List<PersonAttribute> pas = hcs.getPersonAttributes(patientId);
		String patientCategory = null;
       for (PersonAttribute pa : pas) {
            PersonAttributeType attributeType = pa.getAttributeType();
            PersonAttributeType personAttributePC = Context.getPersonService().getPersonAttributeTypeByName("Patient Category");
            
            if(attributeType.getPersonAttributeTypeId()==personAttributePC.getPersonAttributeTypeId());
            {
            	patientCategory = pa.getValue();
            }
           
        }
	
		bill.setPatientCategory(patientCategory);
		bill.setComment(comment);
    	
		return "redirect:/module/billing/patientServiceBill.list?patientId="+patientId;
	}
}
