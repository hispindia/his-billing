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
 *  author: Sagar
 *  date: 11-nov-2014
 **/

package org.openmrs.module.billing.web.controller.indoorbillingqueue;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.IpdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmission;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.PagingUtil;


@Controller("PatientSearchForIpdBillingQueueController")
@RequestMapping("/module/billing/patientsearchipdbillingqueue.form")
public class PatientSearchForIpdBillingQueueController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(
//			@RequestParam(value = "date", required = false) String dateStr,
			@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "currentPage", required = false) Integer currentPage,
                        // 21/11/2014 to work with size selector
                        @RequestParam(value = "pgSize", required = false) Integer pgSize,
			Model model) {
		
		IpdService ipdService = (IpdService) Context.getService(IpdService.class);
	/*	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
*/
//		List<IpdPatientAdmission> listIndoorPatient1 = ipdService.getAllIndoorPatient();
                // 24/11/2014 to Work with size selctor for IPDQueue
		List<IpdPatientAdmissionLog> listIndoorPatient2 = ipdService.getAllIndoorPatientFromAdmissionLog(searchKey, currentPage,pgSize);

		if (currentPage == null) currentPage = 1;
                
                
		int total = ipdService.countGetAllIndoorPatientFromAdmissionLog(searchKey, currentPage);
		PagingUtil pagingUtil = new PagingUtil(pgSize,currentPage, total);

//		model.addAttribute("listIndoorPatient1", listIndoorPatient1);
		model.addAttribute("listIndoorPatient2", listIndoorPatient2);
		model.addAttribute("pagingUtil", pagingUtil);
	//	model.addAttribute("date", dateStr);
		return "/module/billing/indoorQueue/indoorBillingQueue";
	}
}

