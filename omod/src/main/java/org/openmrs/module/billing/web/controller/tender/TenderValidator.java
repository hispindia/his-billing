/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.billing.web.controller.tender;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Tender;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class TenderValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return Tender.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	Tender tender = (Tender) command;
    	
    	if( StringUtils.isBlank(tender.getName())){
    		error.reject("billing.name.invalid");
    	}
    	if(tender.getNumber() == 0 ){
    		error.reject("billing.number.invalid");
    	}
    	if( tender.getPrice() == null){
    		error.reject("billing.price.invalid");
    	}
    	if( tender.getOpeningDate() == null){
    		error.reject("billing.openingDate.invalid");
    	}
    	if( tender.getClosingDate() == null){
    		error.reject("billing.closingDate.invalid");
    	}
    	
    	BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Integer tenderId = tender.getTenderId();
		if (tenderId == null) {
			if (billingService.getTenderByNameAndNumber(tender.getName(), tender.getNumber()) != null) {
				error.reject("billing.nameandnumber.existed");
			}
		} else {
			Tender dbStore = billingService.getTenderById(tenderId);
			if (dbStore!= null && !dbStore.getName().equalsIgnoreCase(tender.getName())) {
				if (billingService.getTenderByNameAndNumber(tender.getName(), tender.getNumber()) != null) {
					error.reject("billing.nameandnumber.existed");
				}
			}
		}
		
		if( tender.getOpeningDate()!= null && tender.getClosingDate() != null && 
			 (	tender.getOpeningDate().compareTo(tender.getClosingDate()) >0 
				||	 tender.getOpeningDate().compareTo(tender.getClosingDate()) ==0)){
			error.reject("billing.closingDate.invalid");
		}
    	
    }

}
