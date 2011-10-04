package org.openmrs.module.billing.includable.billcalculator;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.module.hospitalcore.model.PatientServiceBillItem;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.openmrs.module.hospitalcore.util.HospitalCoreConstants;

public class BillCalculatorService implements BillCalculator {

	private Log logger = LogFactory.getLog(getClass());

	private BillCalculator calculator;

	/**
	 * Get the calculator relying on the hospital name. If can't find one, a
	 * warning will be thrown and the default calculator will be used
	 */
	public BillCalculatorService() {
		String hospitalName = GlobalPropertyUtil.getString(
				HospitalCoreConstants.PROPERTY_HOSPITAL_NAME, "");
		if (!StringUtils.isBlank(hospitalName)) {
			hospitalName = "common";
			logger.warn("CAN'T FIND THE HOSPITAL NAME. ALL TESTS WILL BE CHARGED 100%");
		}
		
		hospitalName = hospitalName.toLowerCase();
		String qualifiedName = "org.openmrs.module.billing.includable.billcalculator." + hospitalName + ".BillCalculatorImpl";
		try {
			calculator = (BillCalculator) Class.forName(qualifiedName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Return the rate to calculate for a particular bill item. If the
	 * `calculator` found, it will be used to calculate Otherwise, it will
	 * return 1 which means patient will be charged 100%
	 */
	public BigDecimal getRate(Patient patient,
			Map<String, String> patientAttributes, PatientServiceBillItem item) {
		if (calculator != null) {
			return calculator.getRate(patient, patientAttributes, item);
		} else {
			return new BigDecimal(1);
		}
	}

	/**
	 * Determine whether a bill should be free or not. If the `calculator` found,
	 * it will be used to determine. Otherwise, it will return `false` which means
	 * the bill is not free.
	 */
	public boolean isFreeBill(Patient patient, Map<String, String> patientAttributes) {
		if (calculator != null) {
			return calculator.isFreeBill(patient, patientAttributes);
		} 
		return false;
	}
}
