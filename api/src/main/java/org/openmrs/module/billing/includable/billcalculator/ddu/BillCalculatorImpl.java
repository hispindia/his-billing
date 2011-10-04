package org.openmrs.module.billing.includable.billcalculator.ddu;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.module.billing.includable.billcalculator.BillCalculator;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public class BillCalculatorImpl implements BillCalculator {

	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL,
	 * the bill should be 100% free
	 * @param patient
	 * 
	 * @return
	 */
	public BigDecimal getRate(Patient patient,
			Map<String, String> patientAttributes, PatientServiceBillItem item) {
		BigDecimal ratio = new BigDecimal(1);
		String patientCategory = patientAttributes.get("Patient Category");
		String bplNumber = patientAttributes.get("BPL Number");
		String rsbyNumber = patientAttributes.get("RSBY Number");

		if (!StringUtils.isBlank(patientCategory)) {
			if (patientCategory.contains("RSBY")) {
				if (!StringUtils.isBlank(rsbyNumber)) {
					ratio = new BigDecimal(0);
				}
			} else if (patientCategory.contains("BPL")) {
				if (!StringUtils.isBlank(bplNumber)) {
					ratio = new BigDecimal(0);
				}
			}
		}

		return ratio;
	}

	/**
	 * Determine whether a bill should be free or not. If patient category is
	 * RSBY or BPL, the bill should be treated as the free bill
	 * @param patientAttributes
	 * 
	 * @return
	 */
	public boolean isFreeBill(Patient patient, Map<String, String> patientAttributes) {
		String patientCategory = patientAttributes.get("Patient Category");
		String bplNumber = patientAttributes.get("BPL Number");
		String rsbyNumber = patientAttributes.get("RSBY Number");

		if (!StringUtils.isBlank(patientCategory)) {
			if (patientCategory.contains("RSBY")) {
				if (!StringUtils.isBlank(rsbyNumber)) {
					return true;
				}
			} else if (patientCategory.contains("BPL")) {
				if (!StringUtils.isBlank(bplNumber)) {
					return true;
				}
			}
		}

		return false;
	}
}
