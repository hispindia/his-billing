/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.billing.web.controller.main;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.concept.ConceptNode;
import org.openmrs.module.hospitalcore.concept.TestTree;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.openmrs.module.hospitalcore.util.GlobalPropertyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 */
@Controller
@RequestMapping("/module/billing/serviceCategoryManage.form")
public class ServiceCategoryManageController {
	Log log = LogFactory.getLog(getClass());

	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(Model model) {

		Integer rootServiceConceptId = GlobalPropertyUtil.getInteger(
				"billing.rootServiceConceptId", 31);
		Concept rootServiceconcept = Context.getConceptService().getConcept(
				rootServiceConceptId);
		if (rootServiceconcept != null) {
			TestTree tree = new TestTree(rootServiceconcept);
			BillingService billingService = (BillingService) Context.getService(BillingService.class);
			List<BillableService> bss = billingService.getAllServices();
			
			for(BillableService bs:bss){				
				Concept serviceConcept = Context.getConceptService().getConcept(bs.getConceptId());				
				if(serviceConcept!=null){
					
					ConceptNode node = tree.findNode(serviceConcept);					
					if(node!=null){						
						while(!node.getParent().equals(tree.getRootLab())) {
							node = node.getParent();
						}
						bs.setCategory(node.getConcept());
						billingService.saveService(bs);
					} else {
						bs.setCategory(null);
						billingService.saveService(bs);
					}					
				}
			}
		}

		return "redirect:/module/billing/main.form";
	}
}
