package org.openmrs.module.billing.includable.billcalculator.solan;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.includable.billcalculator.BillCalculator;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmitted;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;

public class BillCalculatorImpl implements BillCalculator {

	/**
	 * Get the percentage of price to pay If patient category is RSBY or BPL,
	 * the bill should be 100% free
	 * 
	 * @param patient
	 * 
	 * @return
	 */
	public BigDecimal getRate(Patient patient,
			Map<String, String> patientAttributes, PatientServiceBillItem item) {
		BigDecimal ratio = new BigDecimal(1);
		String patientCategory = patientAttributes.get("Patient Category");
		if (!StringUtils.isBlank(patientCategory)) {

			if (patientCategory.contains("BPL")) {

				// all bills are free for impatient BPL
				IpdPatientAdmitted admitted = Context.getService(
						IpdService.class).getAdmittedByPatientId(
						patient.getId());
				if (admitted != null) {
					return new BigDecimal(0);
				}
			} else if (patientCategory.contains("Senior Citizen")) {

				// all bills for senior citizen are free except
				Concept concept = Context.getConceptService().getConcept(
						item.getService().getConceptId());
				if (concept.getName().getName()
						.equalsIgnoreCase("LIPID PANEL")) {
					return new BigDecimal(1);
				} else {
					return new BigDecimal(0);
				}
			}
		}

		return ratio;
	}

	/**
	 * Determine whether a bill should be free or not. By default, all bills are
	 * not free
	 * 
	 * @param patientAttributes
	 * @return
	 */
	public boolean isFreeBill(Patient patient,
			Map<String, String> patientAttributes) {

		return false;
	}
}
