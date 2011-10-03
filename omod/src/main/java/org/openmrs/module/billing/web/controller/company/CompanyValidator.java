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
package org.openmrs.module.billing.web.controller.company;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Company;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 */
public class CompanyValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return Company.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	Company company= (Company) command;
    	
    	if( StringUtils.isBlank(company.getName())){
    		error.reject("billing.name.required");
    	}
    	
    	BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Integer companyId = company.getCompanyId();
		if (companyId == null) {
			if (billingService.getCompanyByName(company.getName())!= null) {
				error.reject("billing.name.existed");
			}
		} else {
			Company dbStore = billingService.getCompanyById(companyId);
			if (!dbStore.getName().equalsIgnoreCase(company.getName())) {
				if (billingService.getCompanyByName(company.getName()) != null) {
					error.reject("billing.name.existed");
				}
			}
		}
    }

}
