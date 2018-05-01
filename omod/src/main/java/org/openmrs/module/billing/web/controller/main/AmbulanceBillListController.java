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

import javax.servlet.http.HttpServletRequest;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingConstants;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.AmbulanceBill;
import org.openmrs.module.hospitalcore.model.Driver;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.hospitalcore.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 */
@Controller
@RequestMapping("/module/billing/ambulanceBill.list")
public class AmbulanceBillListController {

	@RequestMapping(method=RequestMethod.POST)
	public String printBill(@RequestParam("ambulanceBillId") Integer ambulanceBillId,
	                        @RequestParam("driverId") Integer driverId){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
    	AmbulanceBill ambulanceBill = billingService.getAmbulanceBillById(ambulanceBillId);
    	if( ambulanceBill != null && !ambulanceBill.getPrinted()){
    		ambulanceBill.setPrinted(true);
    		billingService.saveAmbulanceBill(ambulanceBill);
    	}
    	return "redirect:/module/billing/ambulanceBill.list?driverId="+driverId;
	}
	@RequestMapping(method=RequestMethod.GET)
	public String listBill(@RequestParam("driverId") Integer driverId,
	                       @RequestParam(value="pageSize",required=false)  Integer pageSize, 
	                       @RequestParam(value="currentPage",required=false)  Integer currentPage,
	                       @RequestParam(value="ambulanceBillId",required=false) Integer ambulanceBillId,
	                         Model model, HttpServletRequest request){
		BillingService billingService = (BillingService)Context.getService(BillingService.class);
		Driver driver = billingService.getDriverById(driverId);
		if( driver != null ){
			int total = billingService.countListAmbulanceBillByDriver(driver);
			PagingUtil pagingUtil = new PagingUtil(RequestUtil.getCurrentLink(request), pageSize, currentPage, total);
			model.addAttribute("ambulanceBills", billingService.listAmbulanceBillByDriver(pagingUtil.getStartPos(), pagingUtil.getPageSize(), driver) );
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("driver", driver);
		}
		if( ambulanceBillId != null ){
			AmbulanceBill ambulanceBill = billingService.getAmbulanceBillById(ambulanceBillId);
			model.addAttribute("ambulanceBill", ambulanceBill);
		}
		model.addAttribute("driverId", driverId);
		User user = Context.getAuthenticatedUser();
		
		model.addAttribute("canEdit", user.hasPrivilege(BillingConstants.PRIV_EDIT_BILL_ONCE_PRINTED) );
		return "/module/billing/main/ambulanceBillList";
	}
}
