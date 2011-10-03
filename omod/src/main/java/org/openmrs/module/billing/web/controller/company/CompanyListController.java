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
package org.openmrs.module.billing.web.controller.company;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Company;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.RequestUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@Controller
@RequestMapping("/module/billing/company.list")
public class CompanyListController {
	Log log = LogFactory.getLog(getClass());
    
    @RequestMapping(method=RequestMethod.POST)
    public String deleteCompanies(@RequestParam("ids") String[] ids,HttpServletRequest request){
    	
    	HttpSession httpSession = request.getSession();
		Integer companyId  = null;
		try{
			BillingService billingService = (BillingService)Context.getService(BillingService.class);
			if( ids != null && ids.length > 0 ){
				for(String sId : ids )
				{
					companyId = Integer.parseInt(sId);
					Company company = billingService.getCompanyById(companyId);
					if( company!= null )
					{
						billingService.deleteCompany(company);
					}
				}
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
		"billing.company.delete.done");
			}
		}catch (Exception e) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			"Can not delete company ");
			log.error(e);
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
		"billing.company.delete.fail");
		}
		
    	
    	return "redirect:/module/billing/company.list";
    }
	
    @RequestMapping(method=RequestMethod.GET)
	public String listTender(@RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                         @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                         Map<String, Object> model, HttpServletRequest request){
		
		BillingService billingService = Context.getService(BillingService.class);
		
		int total = billingService.countListCompany();
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request) , pageSize, currentPage, total );
		
		List<Company> companies = billingService.listCompany(pagingUtil.getStartPos(), pagingUtil.getPageSize());
		
		model.put("companies", companies );
		
		model.put("pagingUtil", pagingUtil);
		
		return "/module/billing/company/list";
	}
}
