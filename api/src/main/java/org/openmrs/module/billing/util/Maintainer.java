package org.openmrs.module.billing.util;

import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;

public class Maintainer {

	/**
	 * Maintain the module
	 */
	public static void maintain() {
		if (Context.getAuthenticatedUser() != null) {
			if (Context.getAuthenticatedUser().isSuperUser()) {
				String maintainCode = GlobalPropertyUtil.getString(
						BillingConstants.PROPERTY_MAINTAINCODE, "");

				// update service orders concept id
				if (!maintainCode.contains("{1}")) {
					maintainCode += "{1}";
					MaintainUtil.resetServiceOrderConceptId();
					GlobalPropertyUtil.setString(
							BillingConstants.PROPERTY_MAINTAINCODE,
							maintainCode);
				}
			}
		}
	}
}
