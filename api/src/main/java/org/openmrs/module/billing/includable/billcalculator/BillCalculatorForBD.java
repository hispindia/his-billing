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

public interface BillCalculatorForBD {
	
	/**
	 * Return the rate to calculate for a particular bill item
	 * 
	 * @param parameters TODO
	 * @return
	 */
	public BigDecimal getRate(Map<String, Object> parameters, String billType);
	
	/**
	 * Determine whether a bill should be free or not
	 * 
	 * @param parameters TODO
	 * @return
	 */
	//ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
	public int isFreeBill(String billType);
}
