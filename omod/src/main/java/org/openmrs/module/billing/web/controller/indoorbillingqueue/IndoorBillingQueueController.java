/**
 *  Copyright 2014 Society for Health Information Systems Programmes, India (HISP India)
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
 *  
 **/

package org.openmrs.module.billing.web.controller.indoorbillingqueue;

import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmission;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("IndoorBillingQueueController")
@RequestMapping("/module/billing/indoorbillingqueue.form")
public class IndoorBillingQueueController {
	@RequestMapping(method=RequestMethod.GET)
	public String main(Model model){
		IpdService ipdService = (IpdService) Context.getService(IpdService.class);
		List<IpdPatientAdmission> listIndoorPatient1 = ipdService.getAllIndoorPatient();
		List<IpdPatientAdmissionLog> listIndoorPatient2 = ipdService.getAllIndoorPatientFromAdmissionLog();
		
		model.addAttribute("listIndoorPatient1", listIndoorPatient1);
		model.addAttribute("listIndoorPatient2", listIndoorPatient2);
		return "/module/billing/indoorQueue/indoorBillingQueue";
	}

}
