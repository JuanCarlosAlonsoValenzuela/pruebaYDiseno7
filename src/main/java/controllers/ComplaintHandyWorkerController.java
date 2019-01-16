
package controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ComplaintService;
import services.FixUpTaskService;
import services.HandyWorkerService;
import domain.Complaint;
import domain.FixUpTask;

@Controller
@RequestMapping("/complaint/handyWorker")
public class ComplaintHandyWorkerController extends AbstractController {

	//Service
	@Autowired
	private HandyWorkerService	handyWorkerService;
	@Autowired
	private ComplaintService	complaitnService;
	@Autowired
	private FixUpTaskService	fixUpTaskService;


	//Constructor
	public ComplaintHandyWorkerController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView complaintList(@RequestParam int fixUpTaskId) {

		ModelAndView result;

		Collection<Complaint> complaints;

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);

		complaints = fixUpTask.getComplaints();

		result = new ModelAndView("handy-worker/complaints");

		result.addObject("complaints", complaints);
		result.addObject("requestURI", "complaint/handyWorker/list.do");

		result = this.isInvolved(fixUpTaskId, result);

		return result;

	}
	//AttchmentsList
	@RequestMapping(value = "/attachmentList", method = RequestMethod.GET)
	public ModelAndView complaintAttachmentList(@RequestParam int complaintId, @RequestParam int fixUpTaskId) {

		ModelAndView result;
		this.handyWorkerService.findOne(fixUpTaskId);

		Complaint complaint = this.complaitnService.findOne(complaintId);
		List<String> attachments = complaint.getAttachments();

		result = new ModelAndView("handy-worker/complaintsAttachment");

		result.addObject("attachments", attachments);
		result.addObject("requestURI", "complaint/handyWorker/attachmentList.do");

		result = this.isInvolved(fixUpTaskId, complaintId, result);

		return result;

	}

	//Security

	public ModelAndView isInvolved(int fixUpTaskId, ModelAndView result) {
		return this.isInvolved(fixUpTaskId, 0, result);
	}

	public ModelAndView isInvolved(int fixUpTaskId, int complaintId, ModelAndView result) {
		Boolean isInvolved = this.handyWorkerService.isInvolved(fixUpTaskId, complaintId);

		if (!isInvolved) {
			result = new ModelAndView("welcome/index");
		}

		return result;
	}

}
