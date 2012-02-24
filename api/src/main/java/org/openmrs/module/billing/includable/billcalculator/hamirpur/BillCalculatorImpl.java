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

package org.openmrs.module.billing.includable.billcalculator.hamirpur;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculator;
import org.openmrs.module.hospitalcore.concept.TestTree;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public class BillCalculatorImpl implements BillCalculator {

	private static Map<String, Set<Concept>> testTreeMap;

	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL,
	 * the bill should be 100% free
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters) {
		BigDecimal rate = new BigDecimal(1);
		Map<String, String> attributes = (Map<String, String>) parameters
				.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		PatientServiceBillItem item = (PatientServiceBillItem) parameters
				.get("billItem");		

		if (!StringUtils.isBlank(patientCategory)) {

			if (patientCategory.contains("BPL")) {

				return new BigDecimal(0);
			} else if (patientCategory.contains("RSBY")) {

				return new BigDecimal(0);
			} else if (patientCategory.contains("Senior Citizen")) {

				// Get test tree map
				if (testTreeMap == null) {
					buildTestTreeMap();
				}
				System.out.println("Billing test tree" + testTreeMap);

				// CALCULATE THE RATE
				Concept test = Context.getConceptService().getConcept(
						item.getService().getConceptId());

				if (testTreeMap.get("GENERAL LABORATORY").contains(test)) {

					// GENERAL LABORATORY
					if (!test.getName().getName()
							.equalsIgnoreCase("LIPID PANEL")) {
						return new BigDecimal(0);
					}
				} else if (testTreeMap.get("RADIOLOGY").contains(test)) {

					// RADIOLOGY and ULTRASOUND
					if (testTreeMap.get("ULTRASOUND").contains(test)) {
						return new BigDecimal(0);
					}
				}
			}
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
		Concept generalLab = Context.getConceptService()
				.getConcept(conceptName);
		TestTree tree = new TestTree(generalLab);
		if (tree.getRootNode() != null) {
			testTreeMap.put(conceptName, tree.getConceptSet());
		}
	}

	/**
	 * Determine whether a bill should be free or not. By default, all bills are
	 * not free
	 * 
	 * @return
	 */
	public boolean isFreeBill(Map<String, Object> parameters) {

		return false;
	}
}
