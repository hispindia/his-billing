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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.Tender;
import org.openmrs.module.hospitalcore.model.TenderBill;
import org.openmrs.module.hospitalcore.model.TenderBillItem;
import org.openmrs.module.hospitalcore.util.Money;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/billing/editTenderBill.form")
public class TenderBillEditController {
	Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method=RequestMethod.GET)
	public String displayForm(@RequestParam("companyId") Integer companyId, @RequestParam("tenderBillId") Integer tenderBillId, HttpServletRequest request, Model model){

		BillingService billingService = (BillingService) Context.getService(BillingService.class);
		
		List<Tender> listTender = billingService.getActiveTenders();
		
		if( listTender == null || listTender.size() == 0  )
		{
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "No Tender Service found.");
		}else {
			model.addAttribute("listTender", listTender);
		}
		model.addAttribute("companyId", companyId);
		
		TenderBill tenderBill = billingService.getTenderBillById(tenderBillId);
		
		model.addAttribute("tenderBill", tenderBill);
		return "module/billing/main/tenderBillEdit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(Model model,@RequestParam("tenderBillId") Integer tenderBillId,
	                       @RequestParam("companyId") Integer companyId,
	                       @RequestParam("tenderIds") Integer[] tenderIds,
	                       @RequestParam("action") String action,
	                       HttpServletRequest request,Object command, BindingResult binding ){

		validateQty(tenderIds, binding, request);
		if( binding.hasErrors()){
			model.addAttribute("errors", binding.getAllErrors());
			return "module/billing/main/tenderBillAdd";
		}
		
		BillingService billingService = (BillingService) Context.getService(BillingService.class);

		TenderBill tenderBill = billingService.getTenderBillById(tenderBillId);
		if( "void".equalsIgnoreCase(action)){
			tenderBill.setVoided(true);
			tenderBill.setVoidedDate(new Date());
			for(TenderBillItem item:tenderBill.getBillItems()){
				item.setVoided(true);
				item.setVoidedDate(new Date());
			}
			billingService.saveTenderBill(tenderBill);
			return "redirect:/module/billing/tenderBill.list?companyId="+companyId;
		}
		
		tenderBill.setPrinted(false);
		
		// void old items and reset amount
		Map<Integer,TenderBillItem> mapOldItems = new HashMap<Integer, TenderBillItem>();
		for( TenderBillItem item : tenderBill.getBillItems()){
			item.setVoided(true);
			item.setVoidedDate(new Date());
			mapOldItems.put(item.getTenderBillItemId(), item);
		}
		tenderBill.setAmount(BigDecimal.ZERO);
		
		Tender tender = null;
		int quantity = 0;
		Money itemAmount; 
		Money totalAmount = new Money(BigDecimal.ZERO);
		TenderBillItem item;
		for (Integer id : tenderIds) {
			
			tender = billingService.getTenderById(id);
			quantity = Integer.parseInt(request.getParameter(id+"_qty"));
			itemAmount = new Money(tender.getPrice());
			itemAmount = itemAmount.times(quantity);
			totalAmount = totalAmount.plus(itemAmount);
			
			String sItemId = request.getParameter(id+"_itemId");
			
			if( sItemId != null ){
				item = mapOldItems.get(Integer.parseInt(sItemId));
				item.setVoided(false);
				item.setVoidedDate(null);
				item.setQuantity(quantity);
				item.setAmount(itemAmount.getAmount());
			}else{
				item = new TenderBillItem();
				item.setName(tender.getName()+"_"+tender.getNumber());
				item.setCreatedDate(new Date());
				item.setTender(tender);
				item.setUnitPrice(tender.getPrice());
				item.setQuantity(quantity);
				item.setTenderBill(tenderBill);
				item.setAmount(itemAmount.getAmount());
				tenderBill.addBillItem(item);
			}
		}
		tenderBill.setAmount(totalAmount.getAmount());
		tenderBill = billingService.saveTenderBill(tenderBill);

		return "redirect:/module/billing/tenderBill.list?companyId="+companyId+"&tenderBillId="+tenderBill.getTenderBillId();
	}
	
	private void validateQty(Integer[] ids, BindingResult binding, HttpServletRequest request){
		for( int id : ids){
			try {
	            Integer.parseInt(request.getParameter(id+"_qty"));
            }
            catch (Exception e) {
            	binding.reject("billing.bill.quantity.invalid", "Quantity is invalid");
            	return;
            }
				
		}
	}
}
