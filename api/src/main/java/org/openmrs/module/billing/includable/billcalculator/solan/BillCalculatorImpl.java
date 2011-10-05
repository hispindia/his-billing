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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getRate(Map<String, Object> parameters) {
		BigDecimal ratio = new BigDecimal(1);
		Map<String, String> attributes = (Map<String, String>) parameters.get("attributes");
		String patientCategory = attributes.get("Patient Category");
		PatientServiceBillItem item = (PatientServiceBillItem) parameters.get("billItem");
		Patient patient = (Patient) parameters.get("patient");
		
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
	 * @return
	 */
	public boolean isFreeBill(Map<String, Object> parameters) {

		return false;
	}
}
