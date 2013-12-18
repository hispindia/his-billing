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
 *  author: Abhishek-Ankur
 *  date: 17-Dec-2013
 *  Requirement: Indoor Patient Billing
 */

package org.openmrs.module.billing.web.contoller.indoorbillingqueue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.util.MaintainUtil;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmitted;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("BillingPatientSearchForIndoorBillingQueueController")
public class PatientSearchForIndoorBillingQueueController {
	
	@RequestMapping(value = "/module/billing/patientsearchindoorbillingqueue.form", method = RequestMethod.GET)
	public String main(
			@RequestParam(value = "searchPatient", required = false) String searchPatient,//patient name or patient identifier
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "ipdWardString", required = false) String ipdWardString, //note ipdWardString = 1,2,3,4.....
            @RequestParam(value = "doctorString", required = false) String doctorString,// note: doctorString= 1,2,3,4.....
            Model model) {
		IpdService ipdService = (IpdService) Context.getService(IpdService.class);
		List<IpdPatientAdmitted> listPatientAdmitted = ipdService.searchIpdPatientAdmitted(searchPatient,
		    MaintainUtil.convertStringToList(doctorString), fromDate, toDate, MaintainUtil.convertStringToList(ipdWardString), "");
		
		model.addAttribute("listPatientAdmitted", listPatientAdmitted);
		
		Map<Integer, String> mapRelationName = new HashMap<Integer, String>();
		Map<Integer, String> mapRelationType = new HashMap<Integer, String>();
		for (IpdPatientAdmitted admit : listPatientAdmitted) {
			PersonAttribute relationNameattr = admit.getPatient().getAttribute("Father/Husband Name");
			PersonAddress add =admit.getPatient().getPersonAddress();
			String address1 = add.getAddress1();
			if(address1!=null){
			String address = " " + add.getAddress1() +" " + add.getCountyDistrict() + " " + add.getCityVillage();
			model.addAttribute("address", address);
			}
			else{
				String address = " " + add.getCountyDistrict() + " " + add.getCityVillage();
				model.addAttribute("address", address);
			}
			PersonAttribute relationTypeattr = admit.getPatient().getAttribute("Relative Name Type");
			if(relationTypeattr!=null){
				mapRelationType.put(admit.getId(), relationTypeattr.getValue());
			}
			else{
				mapRelationType.put(admit.getId(), "Relative Name");
			}
			mapRelationName.put(admit.getId(), relationNameattr.getValue());	
		}
		model.addAttribute("mapRelationName", mapRelationName);
		model.addAttribute("mapRelationType", mapRelationType);
		model.addAttribute("dateTime", new Date().toString());
		
		return "module/billing/indoorqueue/searchResult";
	}
}
