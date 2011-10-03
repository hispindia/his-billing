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
package org.openmrs.module.billing.web.controller.miscellaneousservice;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.MiscellaneousService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class MiscellaneousServiceValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return MiscellaneousService.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	MiscellaneousService miscellaneousService = (MiscellaneousService) command;
    	
    	if( StringUtils.isBlank(miscellaneousService.getName())){
    		error.reject("billing.name.required");
    	}
    	if( miscellaneousService.getPrice() == null){
    		error.reject("billing.price.required");
    	}
    	
    	BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Integer miscellaneousServiceId = miscellaneousService.getId();
		if (miscellaneousServiceId == null) {
			if (billingService.getMiscellaneousServiceByName(miscellaneousService.getName()) != null) {
				error.reject("billing.name.existed");
			}
		} else {
			MiscellaneousService dbStore = billingService.getMiscellaneousServiceById(miscellaneousServiceId);
			if (dbStore!= null && !dbStore.getName().equalsIgnoreCase(miscellaneousService.getName())) {
				if (billingService.getMiscellaneousServiceByName(miscellaneousService.getName()) != null) {
					error.reject("billing.name.existed");
				}
			}
		}
    	
    }

}
