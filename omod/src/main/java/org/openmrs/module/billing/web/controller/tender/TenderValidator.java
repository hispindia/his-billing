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
