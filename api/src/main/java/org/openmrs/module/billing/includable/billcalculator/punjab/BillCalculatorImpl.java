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

package org.openmrs.module.billing.includable.billcalculator.punjab;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.billing.includable.billcalculator.BillCalculator;

public class BillCalculatorImpl implements BillCalculator {
	
	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL, the bill should be
	 * 100% free
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters) {
		BigDecimal ratio = new BigDecimal(0);
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		
		// 30/04/12 - Marta, changed to consider the new free categories. Logic has been inverted.
		if (patientCategory.contains("General")) {
			ratio = new BigDecimal(1);
		}
		
		//This is the old function
		/*
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
			} else if (patientCategory.contains("Punjab Government Employee")) {
				ratio = new BigDecimal(0);
			} else if (patientCategory.contains("Ex Servicemen")) {
				ratio = new BigDecimal(0);
			} else if (patientCategory.contains("Pensioner")) {
				ratio = new BigDecimal(0);
			}
		} */
		
		return ratio;
	}
	
	/**
	 * Determine whether a bill should be free or not. If patient category is RSBY or BPL, the bill
	 * should be treated as the free bill
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isFreeBill(Map<String, Object> parameters) {
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		
		// 30/04/12 - Marta, chaged to consider the new free categories. Logic has been inverted.
		if (patientCategory.contains("General")) {
			return false;
		}
		
		return true;
		
		//This is the old function
		/*String bplNumber = attributes.get("BPL Number");
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
			} else if (patientCategory.contains("Punjab Government Employee")) {
				return true;
			} else if (patientCategory.contains("Ex Servicemen")) {
				return true;
			} else if (patientCategory.contains("Pensioner")) {
				return true;
			}
		}
		
		return false; 
		*/
	}
}
