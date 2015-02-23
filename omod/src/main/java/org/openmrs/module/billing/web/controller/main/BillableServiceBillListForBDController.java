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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorForBDService;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBill;
import org.openmrs.module.hospitalcore.model.IndoorPatientServiceBillItem;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmitted;
import org.openmrs.module.hospitalcore.model.PatientServiceBill;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.openmrs.module.hospitalcore.util.RequestUtil;
//import org.openmrs.module.ipd.util.IpdConstants;
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
	                       @RequestParam(value = "typeOfPatient", required = false) String typeOfPatient,
	                       @RequestParam(value = "admissionLogId", required = false) Integer admissionLogId,
	                       @RequestParam(value = "requestForDischargeStatus", required = false) Integer requestForDischargeStatus,
                           @RequestParam(value="itemID", required = false) Integer itemID, 
                           @RequestParam(value = "voidStatus",required = false) Boolean voidStatus,
                           @RequestParam(value = "selectedCategory", required = false) Integer selectedCategory,
	                       HttpServletRequest request) {
		long admitMili = 0;
		BillingService billingService = Context.getService(BillingService.class);
		
		Patient patient = Context.getPatientService().getPatient(patientId);
		Map<String, String> attributes = PatientUtils.getAttributes(patient);

		BillCalculatorForBDService calculator = new BillCalculatorForBDService();
		IpdService ipdService = Context.getService(IpdService.class);
		IpdPatientAdmissionLog ipdPatientAdmissionLog = ipdService.getIpdPatientAdmissionLog(admissionLogId);
		IpdPatientAdmitted ipdPatientAdmitted = ipdService.getAdmittedByAdmissionLogId(ipdPatientAdmissionLog);
                
                // 13/2/2015 PatientCategory storing            
                if(selectedCategory!=null){
                    BillingService billingService3 = Context.getService(BillingService.class);
                    billingService3.updatePatientCategory(selectedCategory,Context.getEncounterService().getEncounter(encounterId),patient);
                }
        
        if(itemID!=null){
            BillingService billingService2 = Context.getService(BillingService.class);
            String voidedBy =voidStatus?Context.getAuthenticatedUser().getUsername():null;
            Date voidedDate=voidStatus?new Date():null;
            billingService2.updateVoidBillItems(voidStatus, voidedBy, voidedDate, itemID);
         }

		if (patient != null) {
			
			int total = billingService.countListPatientServiceBillByPatient(patient);
			
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total,
			        patientId);
			
			model.addAttribute("age",patient.getAge());
			
			Concept category = Context.getConceptService().getConceptByName("Patient Category");
			List<ConceptAnswer> categoryList = (category!= null ?  new ArrayList<ConceptAnswer>(category.getAnswers()) : null);
			if(CollectionUtils.isNotEmpty(categoryList)){
				Collections.sort(categoryList, new ConceptAnswerComparator());
			}
			
			Integer nhifCatId = Context.getConceptService().getConceptByName("NHIF PATIENT").getConceptId();
			Integer generalCatId = Context.getConceptService().getConceptByName("GENERAL PATIENT").getConceptId();
			Integer exemptedCatId = Context.getConceptService().getConceptByName("EXEMPTED PATIENT").getConceptId();
			Integer ChildCatId = Context.getConceptService().getConceptByName("CHILD LESS THAN 5 YEARS").getConceptId();
			model.addAttribute("nhifCatId", nhifCatId);
			model.addAttribute("generalCatId", generalCatId);
			model.addAttribute("exemptedCatId", exemptedCatId);
			model.addAttribute("ChildCatId", ChildCatId);
			model.addAttribute("categoryList", categoryList);
			
			model.addAttribute("category",patient.getAttribute(14));
			model.addAttribute("fileNumber",patient.getAttribute(43));
			
			if(patient.getGender().equals("M"))
			{
				model.addAttribute("gender","Male");
			}
			if(patient.getGender().equals("F"))
			{
				model.addAttribute("gender","Female");
			}


			if (typeOfPatient != null) {
				if (encounterId != null) {
					if(ipdPatientAdmitted.getAdmittedWard()!=null){	
						model.addAttribute("ward", ipdPatientAdmitted.getAdmittedWard());
						}
						if(ipdPatientAdmitted.getBed()!=null){
						model.addAttribute("bed", ipdPatientAdmitted.getBed());
						}
						PersonAttribute fileNumber = patient.getAttribute(43);
						if(fileNumber!=null){
							model.addAttribute("fileNumber", fileNumber.getValue());					
						}
						if(ipdPatientAdmitted.getUser().getGivenName()!=null){
							model.addAttribute("doctor", ipdPatientAdmitted.getIpdAdmittedUser().getGivenName());				
						}
						if(ipdPatientAdmitted.getPatientAdmittedLogTransferFrom()!=null){
							IpdPatientAdmissionLog ipdPatientAdmissionLog1 = ipdService.getIpdPatientAdmissionLog(ipdPatientAdmitted.getPatientAdmissionLog().getId());
								model.addAttribute("admissionDate", ipdPatientAdmissionLog1.getAdmissionDate());
								admitMili = ipdPatientAdmissionLog1.getAdmissionDate().getTime();
						}
						else{
							if(ipdPatientAdmitted.getAdmissionDate()!=null){
								model.addAttribute("admissionDate", ipdPatientAdmitted.getAdmissionDate());
								admitMili = ipdPatientAdmitted.getAdmissionDate().getTime();
							
							}
						}

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					model.addAttribute("curDate", formatter.format(new Date()));
					
					    Calendar c2 = Calendar.getInstance();
	                    Date eDt = new Date(new Date().getTime());
					    c2.setTime(eDt);
					    c2.set(Calendar.HOUR_OF_DAY, 23);
					    c2.set(Calendar.MINUTE, 59);
					    c2.set(Calendar.SECOND, 59);
					    // Put it back in the Date object

			        long admittedDays =   (c2.getTime().getTime() - admitMili)/(3600000*24);
			        
                                        if(admittedDays<1) {
                                        	admittedDays=1;
                                        	}
                    
					model.addAttribute("admittedDays", admittedDays);
					
					
	                List<IndoorPatientServiceBill> indpsb=billingService.getSelectedCategory(Context.getEncounterService().getEncounter(encounterId),patient);
	                Iterator it=indpsb.listIterator();
	                while (it.hasNext()) {
	                    IndoorPatientServiceBill ipsb =(IndoorPatientServiceBill) it.next();
	                    selectedCategory=ipsb.getSelectedCategory();                      
	                }    
	                if(selectedCategory==null){
	                    selectedCategory=0;
	                }               
	              
	                model.addAttribute("selectedCategory",selectedCategory);
					
					}
				}
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("patient", patient);
			model.addAttribute("listBill",
			    billingService.listPatientServiceBillByPatient(pagingUtil.getStartPos(), pagingUtil.getPageSize(), patient));
			model.addAttribute("address",patient.getPersonAddress().getAddress1()+", "+patient.getPersonAddress().getCityVillage());
			}
		
		
        User user = Context.getAuthenticatedUser();
        
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED));
		if (billId != null) {
			PatientServiceBill bill = billingService.getPatientServiceBillById(billId);
                   

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
			model.addAttribute("dateTime",bill.getCreatedDate());
			model.addAttribute("paymentMode",bill.getPaymentMode());
			model.addAttribute("cashier",bill.getCreator().getGivenName());
			model.addAttribute("bill", bill);                        
		}
                

		
		if (typeOfPatient != null) {
			if (encounterId != null) {
			List<IndoorPatientServiceBill> bills = billingService.getIndoorPatientServiceBillByEncounter(Context.getEncounterService().getEncounter(encounterId));
			model.addAttribute("billList", bills);
			}
			model.addAttribute("requestForDischargeStatus", requestForDischargeStatus);
			
			return "/module/billing/indoorQueue/billListForIndoorPatient";
		}
		else{
			return "/module/billing/main/billableServiceBillListForBD";
		}
		
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@RequestParam("patientId") Integer patientId, 
                        @RequestParam(value = "billId",required = false) Integer billId,
			@RequestParam(value = "encounterId", required = false) Integer encounterId,
			@RequestParam(value = "admissionLogId", required = false) Integer admissionLogId,
			@RequestParam(value = "waiverAmount", required = false) BigDecimal waiverAmount,
			@RequestParam(value = "paymentMode", required = false) String paymentMode,
			@RequestParam(value = "adDays", required = false) Integer admittedDays,
            @RequestParam(value = "rebateAmount", required = false) BigDecimal rebateAmount,
            @RequestParam(value = "comment", required = false) String comment,          
            @RequestParam(value = "patientCategory", required = false) String patientCategory,
            @RequestParam(value = "voidedAmount", required = false) BigDecimal voidedAmount,
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
						item.setVoidedby(ipsbi.getVoidedby());
						if(ipsbi.getVoidedby()!=null){
							item.setVoided(true);
			                item.setVoidedDate(new Date());                                        
						}
//						item.setVoided(ipsbi.getVoided());
//		                item.setVoidedDate(ipsbi.getVoidedDate());                                        
						item.setUnitPrice(ipsbi.getUnitPrice());
						item.setAmount(ipsbi.getAmount());
						item.setOrder(ipsbi.getOrder());
						item.setActualAmount(ipsbi.getActualAmount());
						
						if(patientCategory.equals("EXEMPTED PATIENT")){
							totalActualAmount = BigDecimal.ZERO;
						}
						else{
						totalActualAmount = totalActualAmount.add(item.getActualAmount());
						}
						bill.addBillItem(item);
					}
			}
			bill.setAmount(totalAmount.getAmount());
			bill.setReceipt(billingService.createReceipt());
			bill.setFreeBill(0);
			//bill.setActualAmount(totalActualAmount.subtract(waiverAmount));
                        
                        if(voidedAmount!=null){
                            totalActualAmount=totalActualAmount.subtract(voidedAmount);
                        }
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
			bill.setAdmittedDays(admittedDays);
                        bill.setRebateAmount(rebateAmount);
                        bill.setPatientCategory(patientCategory);
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
			return "redirect:/module/billing/indoorPatientServiceBill.list?patientId=" + patientId + "&billId=" + bill.getPatientServiceBillId() +"&encounterId=" + encounterId+"&admissionLogId="+admissionLogId;
		}
		else{
			BillingService billingService = (BillingService) Context.getService(BillingService.class);
			PatientServiceBill patientServiceBill = billingService.getPatientServiceBillById(billId);
			if (patientServiceBill != null && !patientServiceBill.getPrinted() && patientServiceBill.getEncounter()==null) {
				patientServiceBill.setPrinted(true);
				Map<String, String> attributes = PatientUtils.getAttributes(patientServiceBill.getPatient());
				
				BillCalculatorForBDService calculator = new BillCalculatorForBDService();
				
				// 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
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
//			return "redirect:/module/billing/patientServiceBillForBD.list?patientId=" + patientId;
			return "redirect:/module/billing/billingqueue.form";
		}
	}
}
