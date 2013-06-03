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
 *  
 *  author: Ghanshyam
 *  date:   20-02-2013
 **/

package org.openmrs.module.billing.includable.billcalculator.bd_hospital;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculatorForBD;
import org.openmrs.module.hospitalcore.concept.TestTree;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public class BillCalculatorImpl implements BillCalculatorForBD {
	
	private static Map<String, Set<Concept>> testTreeMap;
	
	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL, the bill should be
	 * 100% free
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters, String billType) {
		BigDecimal rate = new BigDecimal(0);
		PatientServiceBillItem item = (PatientServiceBillItem) parameters.get("billItem");
		
		if (billType.equals("paid")) {
			rate = new BigDecimal(1);
		}
		
		return rate;
	}
	
	/**
	 * Build test tree map for senior citizen billing
	 */
	private static void buildTestTreeMap() {
		testTreeMap = new HashMap<String, Set<Concept>>();
		
		// General lab
		buildTestTree("GENERAL LABORATORY");
		buildTestTree("RADIOLOGY");
		buildTestTree("ULTRASOUND");
		buildTestTree("CARDIOLOGY");
	}
	
	/**
	 * Build test tree for a specific tests
	 * 
	 * @param conceptName
	 */
	private static void buildTestTree(String conceptName) {
		Concept generalLab = Context.getConceptService().getConcept(conceptName);
		TestTree tree = new TestTree(generalLab);
		if (tree.getRootNode() != null) {
			testTreeMap.put(conceptName, tree.getConceptSet());
		}
	}
	
	/**
	 * Determine whether a bill should be free or not. By default, all bills are not free
	 * 
	 * @return
	 */
	//ghanshyam 3-june-2013 New Requirement #1632 Orders from dashboard must be appear in billing queue.User must be able to generate bills from this queue
	@SuppressWarnings("unchecked")
	public int isFreeBill(String billType) {
		
		if (billType.equals("paid")) {
			return 0;
		}
		
		return 1;
	}
}
