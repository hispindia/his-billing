package org.openmrs.module.billing.includable.billcalculator;

import java.math.BigDecimal;
import java.util.Map;

import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public interface BillCalculator {

	/**
	 * Return the rate to calculate for a particular bill item
	 * 
	 * @param patientAttributes
	 * @param item
	 * @return
	 */
	public BigDecimal getRate(Map<String, String> patientAttributes,
			PatientServiceBillItem item);

	/**
	 * Determine whether a bill should be free or not
	 * 
	 * @param patientAttributes
	 * @return
	 */
	public boolean isFreeBill(Map<String, String> patientAttributes);
}
