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

package org.openmrs.module.billing.includable.billcalculator.solan;

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
	 * Get the percentage of price to pay If patient category is RSBY or BPL, the bill should be
	 * 100% free
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters) {
		// 17/05/12 - Marta: changed to consider the new free categories. Logic has been inverted. #188
		BigDecimal rate = new BigDecimal(0);
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		PatientServiceBillItem item = (PatientServiceBillItem) parameters.get("billItem");
		//ghanshyam 12-sept-2012 Bug #358 [Billing][3.2.7-SNAPSHOT][DDU, DDU_SDMX] Error screen appears on selecting patients for billing
		if(patientCategory!=null){
		
		// 17/05/12 - Marta: changed to consider the new free categories. Logic has been inverted. #188
		if (patientCategory.contains("General")) {
			rate = new BigDecimal(1);
		 }
		if (patientCategory.contains("Staff")) {
			rate = new BigDecimal(1);
		 }
		}
		else{
			rate = new BigDecimal(1);
		}
		
		//This is the old function
		/*if (!StringUtils.isBlank(patientCategory)) {
			
			if (patientCategory.contains("BPL")) {
				return new BigDecimal(0);
			} else if (patientCategory.contains("RSBY")) {
				return new BigDecimal(0);
			} else if (patientCategory.contains("Antenatal")) {
				return new BigDecimal(0);
			} else if (patientCategory.contains("Child Less Than 1yr")) {
				return new BigDecimal(0);
			} else if (patientCategory.contains("Other Free")) {
				return new BigDecimal(0);
			} // 17/05/2012: Marta added to make Senior Citizen Free bill - Bug #188
			  else if (patientCategory.contains("Senior Citizen")) {
				return new BigDecimal(0);
			}*/
			/*else if (patientCategory.contains("Senior Citizen")) {
				
				// Get test tree map
				if (testTreeMap == null) {
					buildTestTreeMap();
				}
				System.out.println("Billing test tree" + testTreeMap);
				
				// CALCULATE THE RATE
				Concept test = Context.getConceptService().getConcept(item.getService().getConceptId());
				
				if (testTreeMap.get("GENERAL LABORATORY").contains(test)) {
					
					// GENERAL LABORATORY
					if (!test.getName().getName().equalsIgnoreCase("LIPID PANEL")) {
						return new BigDecimal(0);
					}
				} else if (testTreeMap.get("RADIOLOGY").contains(test)) {
					
					// RADIOLOGY and ULTRASOUND
					if (testTreeMap.get("ULTRASOUND").contains(test)) {
						return new BigDecimal(0);
					}
				}
			}
		}*/
		
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
	@SuppressWarnings("unchecked")
	public boolean isFreeBill(Map<String, Object> parameters) {
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		//ghanshyam 12-sept-2012 Bug #358 [Billing][3.2.7-SNAPSHOT][DDU, DDU_SDMX] Error screen appears on selecting patients for billing
		if(patientCategory!=null){
			
		// 11/05/12 - Thai Chuong, chaged to consider the new free categories. Fixed bug#188
		if (patientCategory.contains("General")) {
			return false;
		 }
		if (patientCategory.contains("Staff")) {
			return false;
		 }
		}
		else{
			return false;
		}
		return true;
	}
}
