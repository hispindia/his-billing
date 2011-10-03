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
package org.openmrs.module.billing.web.controller.driver;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Driver;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class DriverValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return Driver.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	Driver driver = (Driver) command;
    	
    	if( StringUtils.isBlank(driver.getName())){
    		error.reject("billing.name.required");
    	}
    	
    	BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Integer driverId = driver.getDriverId();
		if (driverId == null) {
			if (billingService.getDriverByName(driver.getName())!= null) {
				error.reject("billing.name.existed");
			}
		} else {
			Driver dbStore = billingService.getDriverById(driverId);
			if (!dbStore.getName().equalsIgnoreCase(driver.getName())) {
				if (billingService.getDriverByName(driver.getName()) != null) {
					error.reject("billing.name.existed");
				}
			}
		}
    }

}
