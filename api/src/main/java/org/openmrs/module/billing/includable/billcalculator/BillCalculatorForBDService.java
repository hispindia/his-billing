/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
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
 *  author: Ghanshyam
 *  date:   25-02-2013
 *  New Requirement #966[Billing]Add Paid Bill/Add Free Bill for Bangladesh module
 **/

package org.openmrs.module.billing.includable.billcalculator;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.hospitalcore.util.HospitalCoreConstants;

public class BillCalculatorForBDService implements BillCalculatorForBD {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private BillCalculatorForBD calculator = null;
	
	/**
	 * Get the calculator relying on the hospital name. If can't find one, a warning will be thrown
	 * and the default calculator will be used
	 */
	public BillCalculatorForBDService() {
		
		String hospitalName = GlobalPropertyUtil.getString(HospitalCoreConstants.PROPERTY_HOSPITAL_NAME, "");
		if (StringUtils.isBlank(hospitalName)) {
			hospitalName = "common";
			logger.warn("CAN'T FIND THE HOSPITAL NAME. ALL TESTS WILL BE CHARGED 100%");
		}
		
		hospitalName = hospitalName.toLowerCase();
		String qualifiedName = "org.openmrs.module.billing.includable.billcalculator." + hospitalName
		        + ".BillCalculatorImpl";
		try {
			calculator = (BillCalculatorForBD) Class.forName(qualifiedName).newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Return the rate to calculate for a particular bill item. If the `calculator` found, it will
	 * be used to calculate Otherwise, it will return 1 which means patient will be charged 100%
	 */
	public BigDecimal getRate(Map<String, Object> parameters, String billType) {
		if (calculator != null) {
			return calculator.getRate(parameters, billType);
		} else {
			return new BigDecimal(1);
		}
	}
	
	/**
	 * Determine whether a bill should be free or not. If the `calculator` found, it will be used to
	 * determine. Otherwise, it will return `false` which means the bill is not free.
	 */
	//ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
	public int isFreeBill(String billType) {
		if (billType.equals("free")) {
			return 1;
		} else {
			return 0;
		}
	}
}
