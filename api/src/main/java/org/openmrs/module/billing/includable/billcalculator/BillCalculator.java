package org.openmrs.module.billing.includable.billcalculator;

import java.math.BigDecimal;
import java.util.Map;


public interface BillCalculator {

	/**
	 * Return the rate to calculate for a particular bill item
	 * @param parameters TODO
	 * @return
	 */
	public BigDecimal getRate(Map<String, Object> parameters);

	/**
	 * Determine whether a bill should be free or not
	 * @param parameters TODO
	 * @return
	 */
	public boolean isFreeBill(Map<String, Object> parameters);
}
