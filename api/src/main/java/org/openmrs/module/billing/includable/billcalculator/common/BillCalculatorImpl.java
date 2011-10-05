package org.openmrs.module.billing.includable.billcalculator.common;

import java.math.BigDecimal;
import java.util.Map;

import org.openmrs.module.billing.includable.billcalculator.BillCalculator;

public class BillCalculatorImpl implements BillCalculator {

	/**
	 * Return 100%
	 */
	public BigDecimal getRate(Map<String, Object> parameters) {
		return new BigDecimal(1);
	}

	public boolean isFreeBill(Map<String, Object> parameters) {

		return false;
	}
}
