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
