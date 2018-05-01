/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
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
 **/

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
