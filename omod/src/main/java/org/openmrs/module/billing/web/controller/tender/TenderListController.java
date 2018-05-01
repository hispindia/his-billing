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

package org.openmrs.module.billing.web.controller.tender;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Tender;
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
@RequestMapping("/module/billing/tender.list")
public class TenderListController {
	Log log = LogFactory.getLog(getClass());
    
    @RequestMapping(method=RequestMethod.POST)
    public String deleteTenders(@RequestParam("ids") String[] ids,HttpServletRequest request){
    	
    	HttpSession httpSession = request.getSession();
		Integer tenderId  = null;
		try{
			BillingService billingService = (BillingService)Context.getService(BillingService.class);
			if( ids != null && ids.length > 0 ){
				for(String sId : ids )
				{
					tenderId = Integer.parseInt(sId);
					Tender tender = billingService.getTenderById(tenderId);
					if( tender != null )
					{
						billingService.deleteTender(tender);
					}
				}
			}
		}catch (Exception e) {
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			"Can not delete tender because it has link to bill ");
			log.error(e);
			return "redirect:/module/billing/tender.list";
		}
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
		"Tender.deleted");
    	
    	return "redirect:/module/billing/tender.list";
    }
	
    @RequestMapping(method=RequestMethod.GET)
	public String listTender(@RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                         @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                         Map<String, Object> model, HttpServletRequest request){
		
		BillingService billingService = Context.getService(BillingService.class);
		
		int total = billingService.countListTender();
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request) , pageSize, currentPage, total );
		
		List<Tender> tenders = billingService.listTender(pagingUtil.getStartPos(), pagingUtil.getPageSize());
		
		model.put("tenders", tenders );
		
		model.put("pagingUtil", pagingUtil);
		
		return "/module/billing/tender/list";
	}
}
