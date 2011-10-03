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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.BillableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 */
@Controller
@RequestMapping("/module/billing/service.form")
public class ServiceManageController {
	Log log = LogFactory.getLog(getClass());

	@RequestMapping(method = RequestMethod.GET)
	public String viewForm(Model model) {

		ConceptService cs = Context.getConceptService();
		Integer gp = Integer.valueOf(Context.getAdministrationService()
				.getGlobalProperty(
						BillingConstants.GLOBAL_PROPRETY_SERVICE_CONCEPT));

		Concept concept = cs.getConcept(gp);

		BillingService billingService = Context
				.getService(BillingService.class);

		List<BillableService> services = billingService.getAllServices();

		Map<Integer, BillableService> mapServices = new HashMap<Integer, BillableService>();

		for (BillableService ser : services) {
			if (ser.getPrice() != null)
				mapServices.put(ser.getConceptId(), ser);
		}

		model.addAttribute("tree",
				billingService.traversServices(concept, mapServices));

		return "/module/billing/main/serviceManage";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(@ModelAttribute("command") Object command,
			BindingResult binding, SessionStatus sessionStatus,
			@RequestParam("cons") Integer[] concepts,
			HttpServletRequest request, Model model) {

		ConceptService cs = Context.getConceptService();
		Integer root = Integer.valueOf(Context.getAdministrationService()
				.getGlobalProperty(
						BillingConstants.GLOBAL_PROPRETY_SERVICE_CONCEPT));
		validate(concepts, request, binding);
		if (binding.hasErrors()) {
			log.info(binding.getAllErrors());

			Concept concept = cs.getConcept(root);

			BillingService billingService = Context
					.getService(BillingService.class);

			List<BillableService> services = billingService.getAllServices();

			Map<Integer, BillableService> mapServices = new HashMap<Integer, BillableService>();

			for (BillableService ser : services) {
				if (ser.getPrice() != null)
					mapServices.put(ser.getConceptId(), ser);
			}

			model.addAttribute("errors", binding.getAllErrors());
			model.addAttribute("tree",
					billingService.traversServices(concept, mapServices));
			return "/module/billing/main/serviceManage";
		}

		BillingService billingService = Context
				.getService(BillingService.class);
		List<BillableService> services = billingService.getAllServices();

		Map<Integer, BillableService> mapServices = new HashMap<Integer, BillableService>();

		for (BillableService ser : services) {
			mapServices.put(ser.getConceptId(), ser);
		}
		for (int conId : concepts) {

			String name = request.getParameter(conId + "_name");
			String shortName = request.getParameter(conId + "_shortName");
			String price = request.getParameter(conId + "_price");
			String serviceTypeText = request.getParameter(conId + "_type");

			BillableService service = mapServices.get(conId);
			if (service == null) {
				service = new BillableService();
				service.setConceptId(conId);
				service.setName(name);
				service.setShortName(shortName);
				if (StringUtils.isNotBlank(price))
					service.setPrice(NumberUtils.createBigDecimal(price));				
				mapServices.put(conId, service);
			} else {
				service.setName(name);
				service.setShortName(shortName);
				if (StringUtils.isNotBlank(price))
					service.setPrice(NumberUtils.createBigDecimal(price));				
				mapServices.remove(conId);
				mapServices.put(conId, service);
			}
		}
		billingService.saveServices(mapServices.values());
		sessionStatus.setComplete();
		return "redirect:/module/billing/service.form";
	}

	private void validate(Integer[] concepts, HttpServletRequest request,
			BindingResult binding) {
		for (int conId : concepts) {
			String price = request.getParameter(conId + "_price");
			String name = request.getParameter(conId + "_name");
			if (StringUtils.isNotBlank(price)) {

				try {
					new BigDecimal(price);
				} catch (Exception e) {
					binding.reject("billing.service.price.invalid",
							"Invalid price for " + name);
					return;
				}
			}
		}
	}
}
