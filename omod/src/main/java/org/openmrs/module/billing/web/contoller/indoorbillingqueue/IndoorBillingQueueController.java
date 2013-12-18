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
 *  date: 16-Dec-2013
 *  Requirement: Indoor Patient Billing
 **/

package org.openmrs.module.billing.web.contoller.indoorbillingqueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.billing.util.BillingConstants;
import org.openmrs.module.billing.util.MaintainUtil;
import org.openmrs.module.hospitalcore.util.ConceptAnswerComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("IndoorBillingQueueController")
@RequestMapping("/module/billing/indoorbillingqueue.form")
public class IndoorBillingQueueController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String main(
			@RequestParam(value ="searchPatient",required=false) String searchPatient,//patient name or patient identifier
			@RequestParam(value ="fromDate",required=false) String fromDate,
			@RequestParam(value ="toDate",required=false) String toDate,
			@RequestParam(value ="ipdWard",required=false) String[] ipdWard, //ipdWard multiselect
			@RequestParam(value ="doctor",required=false) String[] doctor,
			Model model){
		
		Concept ipdConcept = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(BillingConstants.PROPERTY_IPDWARD));
		List<ConceptAnswer> list = (ipdConcept!= null ?  new ArrayList<ConceptAnswer>(ipdConcept.getAnswers()) : null);
		if(CollectionUtils.isNotEmpty(list)){
			Collections.sort(list, new ConceptAnswerComparator());
		}
		model.addAttribute("listIpd", list);
		String doctorRoleProps = Context.getAdministrationService().getGlobalProperty(BillingConstants.PROPERTY_NAME_DOCTOR_ROLE);
		Role doctorRole = Context.getUserService().getRole(doctorRoleProps);
		if( doctorRole != null ){
			List<User> listDoctor = Context.getUserService().getUsersByRole(doctorRole);
			model.addAttribute("listDoctor",listDoctor);
		}
		
		model.addAttribute("fromDate",fromDate);
		model.addAttribute("toDate",toDate);
		model.addAttribute("searchPatient",searchPatient);
		model.addAttribute("ipdWard",ipdWard);
		model.addAttribute("ipdWardString",MaintainUtil.convertStringArraytoString(ipdWard));
		model.addAttribute("doctor",doctor);
		model.addAttribute("doctorString",MaintainUtil.convertStringArraytoString(doctor));
		
		return "module/billing/indoorqueue/indoorPatientQueueOrder";
		
	}
}
