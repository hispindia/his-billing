package org.openmrs.module.billing.includable.billcalculator.ddu;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.billing.includable.billcalculator.BillCalculator;

public class BillCalculatorImpl implements BillCalculator {

	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL,
	 * the bill should be 100% free
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters) {
		BigDecimal ratio = new BigDecimal(1);
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		String bplNumber = attributes.get("BPL Number");
		String rsbyNumber = attributes.get("RSBY Number");

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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isFreeBill(Map<String, Object> parameters) {
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		String bplNumber = attributes.get("BPL Number");
		String rsbyNumber = attributes.get("RSBY Number");

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
