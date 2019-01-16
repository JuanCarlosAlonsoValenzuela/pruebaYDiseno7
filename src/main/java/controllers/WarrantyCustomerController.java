/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import services.WarrantyService;
import domain.FixUpTask;
import domain.Warranty;

@Controller
@RequestMapping("/warranty/customer")
public class WarrantyCustomerController extends AbstractController {

	@Autowired
	private WarrantyService	warrantyService;

	@Autowired
	private CustomerService	customerService;


	// Constructors -----------------------------------------------------------

	public WarrantyCustomerController() {
		super();
	}

	// List ---------------------------------------------------------------		

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showWarranty(@RequestParam int warrantyId) {
		ModelAndView result;

		Warranty warranty = this.warrantyService.findOne(warrantyId);

		List<FixUpTask> fixUpTasks = (List<FixUpTask>) this.customerService.showFixUpTasks();

		Boolean hasWarranty = false;
		for (FixUpTask f : fixUpTasks) {
			if (f.getWarranty().equals(warranty)) {
				hasWarranty = true;
				break;
			}
		}

		if (hasWarranty) {
			result = new ModelAndView("customer/warranty");
			result.addObject("warranty", warranty);

			result.addObject("terms", warranty.getTerms());
			result.addObject("laws", warranty.getLaws());

			result.addObject("requestURI", "warranty/customer/show.do");
		} else {
			result = new ModelAndView("redirect:/fixUpTask/customer/list.do");
		}

		return result;
	}

}
