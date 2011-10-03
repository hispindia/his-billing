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
package org.openmrs.module.billing.web.controller.ambulance;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Ambulance;
import org.openmrs.module.hospitalcore.model.Company;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class AmbulanceValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return Ambulance.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	Ambulance ambulance= (Ambulance) command;
    	
    	if( StringUtils.isBlank(ambulance.getName())){
    		error.reject("billing.name.required");
    	}
    	
    	BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Integer companyId = ambulance.getAmbulanceId();
		if (companyId == null) {
			if (billingService.getAmbulanceByName(ambulance.getName())!= null) {
				error.reject("billing.name.existed");
			}
		} else {
			Ambulance dbStore = billingService.getAmbulanceById(companyId);
			if (!dbStore.getName().equalsIgnoreCase(ambulance.getName())) {
				if (billingService.getAmbulanceByName(ambulance.getName()) != null) {
					error.reject("billing.name.existed");
				}
			}
		}
    }

}
