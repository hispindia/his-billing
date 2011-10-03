package org.openmrs.module.billing.includable.billcalculator.common;

import java.math.BigDecimal;
import java.util.Map;

import org.openmrs.module.billing.includable.billcalculator.BillCalculator;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public class CommonBillCalculatorImpl implements BillCalculator {

	/**
	 * Return 100%
	 */
	public BigDecimal getRate(Map<String, String> patientAttributes,
			PatientServiceBillItem item) {
		return new BigDecimal(1);
	}

	public boolean isFreeBill(Map<String, String> patientAttributes) {

		return false;
	}
}
