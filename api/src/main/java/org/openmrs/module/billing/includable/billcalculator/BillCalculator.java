package org.openmrs.module.billing.includable.billcalculator;

import java.math.BigDecimal;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public interface BillCalculator {

	/**
	 * Return the rate to calculate for a particular bill item
	 * @param patient TODO
	 * @param patientAttributes
	 * @param item
	 * 
	 * @return
	 */
	public BigDecimal getRate(Patient patient,
			Map<String, String> patientAttributes, PatientServiceBillItem item);

	/**
	 * Determine whether a bill should be free or not
	 * @param patient TODO
	 * @param patientAttributes
	 * 
	 * @return
	 */
	public boolean isFreeBill(Patient patient, Map<String, String> patientAttributes);
}
