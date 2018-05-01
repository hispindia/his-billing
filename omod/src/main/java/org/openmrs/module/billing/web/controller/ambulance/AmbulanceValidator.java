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
