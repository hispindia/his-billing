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
 *  author: ghanshyam
 *  date: 3-june-2013
 *  issue no: #1632
 **/

package org.openmrs.module.billing.web.controller.billingqueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;

import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.openmrs.module.hospitalcore.model.PatientSearch;
import org.openmrs.module.hospitalcore.util.PagingUtil;

@Controller("PatientSearchForBillingQueueController")
@RequestMapping("/module/billing/patientsearchbillingqueue.form")
public class PatientSearchForBillingQueueController {
	@RequestMapping(method = RequestMethod.GET)
	public String main(
			@RequestParam(value = "date", required = false) String dateStr,
			@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "currentPage", required = false) Integer currentPage,
                        // 21/11/2014 to work with size selector
                        @RequestParam(value = "pgSize", required = false) Integer pgSize,
			Model model) {
		BillingService billingService = Context.getService(BillingService.class);
                
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
                
                // 21/11/2014 to work with size selector for OPDQueue
		List<PatientSearch> patientSearchResult = billingService.searchListOfPatient(date, searchKey, currentPage,pgSize);
		if (currentPage == null) currentPage = 1;
		int total = billingService.countSearchListOfPatient(date, searchKey, currentPage);
		PagingUtil pagingUtil = new PagingUtil(pgSize,currentPage, total);
		model.addAttribute("pagingUtil", pagingUtil);
		model.addAttribute("patientList", patientSearchResult);
		model.addAttribute("date", dateStr);
                                
                // 21/11/2014 to get the cashier processing column
//                User authenticatedUser=Context.getAuthenticatedUser();
//                model.addAttribute("user", authenticatedUser);  
                
                
		return "/module/billing/queue/searchResult";
	}
}

