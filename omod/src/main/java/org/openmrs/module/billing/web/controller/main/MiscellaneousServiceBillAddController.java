package org.openmrs.module.billing.web.controller.main;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.BillingService;
import org.openmrs.module.hospitalcore.model.MiscellaneousService;
import org.openmrs.module.hospitalcore.model.MiscellaneousServiceBill;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/billing/addMiscellaneousServiceBill.form")
public class MiscellaneousServiceBillAddController {
	Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(Model model,
			@RequestParam("serviceId") Integer miscellaneousServiceId, 
			@RequestParam("name") String name, Object command, BindingResult binding ){

		
		BillingService billingService = (BillingService) Context.getService(BillingService.class);

		MiscellaneousServiceBill miscellaneousServiceBill = new MiscellaneousServiceBill();
		miscellaneousServiceBill.setCreatedDate(new Date());
		miscellaneousServiceBill.setCreator(Context.getAuthenticatedUser().getUserId());
		miscellaneousServiceBill.setLiableName(name);
		
		MiscellaneousService miscellaneousService = billingService.getMiscellaneousServiceById(miscellaneousServiceId);
		miscellaneousServiceBill.setAmount(miscellaneousService.getPrice());
		miscellaneousServiceBill.setService(miscellaneousService);
		miscellaneousServiceBill.setReceipt(billingService.createReceipt());
		miscellaneousServiceBill = billingService.saveMiscellaneousServiceBill(miscellaneousServiceBill);

		return "redirect:/module/billing/miscellaneousServiceBill.list?serviceId="+miscellaneousServiceId+"&billId="+miscellaneousServiceBill.getId();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String displayForm(@ModelAttribute("command") Object command,  HttpServletRequest request, Model model){

		BillingService billingService = (BillingService) Context.getService(BillingService.class);
		
		List<MiscellaneousService> listMiscellaneousService = billingService.getAllMiscellaneousService();
		
		if( listMiscellaneousService == null || listMiscellaneousService.size() == 0  )
		{
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "No MiscellaneousService found.");
		}else {
			model.addAttribute("listMiscellaneousService", listMiscellaneousService);
		}
		return "module/billing/main/miscellaneousServiceBillAdd";
	}

	
}
