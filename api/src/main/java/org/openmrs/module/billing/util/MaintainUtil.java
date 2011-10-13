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

package org.openmrs.module.billing.util;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;

public class MaintainUtil {
	
	/**
	 * Reset service order concept id
	 */
	public static void resetServiceOrderConceptId() {
		System.out.println("=== resetServiceOrderId ===");
		Concept concept = Context.getConceptService().getConcept(BillingConstants.SERVICE_ORDER_CONCEPT_NAME);
		if(concept!=null){
			GlobalPropertyUtil.setString(BillingConstants.PROPERTY_ROOT_SERVICE_CONCEPT_ID, concept.getConceptId().toString());
		} else {
			System.out.println("CAN'T FOUND SERVICE ORDER CONCEPT");
		}		
	}
}
