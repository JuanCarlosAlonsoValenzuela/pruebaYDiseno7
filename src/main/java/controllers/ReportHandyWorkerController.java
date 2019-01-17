
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ComplaintService;
import services.FixUpTaskService;
import services.HandyWorkerService;
import services.ReportService;
import domain.Application;
import domain.Complaint;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Report;

@Controller
@RequestMapping("/report/handyWorker")
public class ReportHandyWorkerController extends AbstractController {

	//Service
	@Autowired
	private HandyWorkerService	handyWorkerService;
	@Autowired
	private ReportService		reportService;
	@Autowired
	private ComplaintService	complaintService;
	@Autowired
	private FixUpTaskService	fixUpTaskService;


	//Constructor
	public ReportHandyWorkerController() {
		super();
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView reportList(@RequestParam int complaintId, @RequestParam int fixUpTaskId) {
		ModelAndView result;

		Complaint complaint = this.complaintService.findOne(complaintId);
		List<Report> reports = complaint.getReports();

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		UserAccount userAccount = LoginService.getPrincipal();
		HandyWorker logguedHandyWorker = this.handyWorkerService.getHandyWorkerByUsername(userAccount.getUsername());

		List<Application> fixUpTaksApplications = (List<Application>) fixUpTask.getApplications();

		Boolean isInvolved = false;

		for (Application a : fixUpTaksApplications) {
			if (a.getHandyWorker().equals(logguedHandyWorker) && a.getStatus().toString() == "ACCEPTED") {
				isInvolved = true;
			}
		}

		Assert.isTrue(isInvolved);

		result = new ModelAndView("handy-worker/complaintsReport");

		result.addObject("reports", reports);
		result.addObject("requestURI", "/report/handyWorker/list.do");

		result = this.isInvolved(fixUpTaskId, complaintId, result);

		return result;
	}

	//AttchmentsList
	@RequestMapping(value = "/attachmentList", method = RequestMethod.GET)
	public ModelAndView complaintAttachmentList(@RequestParam int reportId, @RequestParam int complaintId, @RequestParam int fixUpTaskId) {

		ModelAndView result;

		Report report = this.reportService.findOne(reportId);
		List<String> attachments = report.getAttachments();

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		UserAccount userAccount = LoginService.getPrincipal();
		HandyWorker logguedHandyWorker = this.handyWorkerService.getHandyWorkerByUsername(userAccount.getUsername());

		List<Application> fixUpTaksApplications = (List<Application>) fixUpTask.getApplications();

		Boolean isInvolved = false;

		for (Application a : fixUpTaksApplications) {
			if (a.getHandyWorker().equals(logguedHandyWorker) && a.getStatus().toString() == "ACCEPTED") {
				isInvolved = true;
			}
		}

		Assert.isTrue(isInvolved);

		result = new ModelAndView("handy-worker/reportsAttachments");

		result.addObject("attachments", attachments);
		result.addObject("requestURI", "report/handyWorker/attachmentList.do");

		result = this.isInvolved(fixUpTaskId, complaintId, reportId, result);

		return result;

	}

	//Security

	public ModelAndView isInvolved(int fixUpTaskId, int complaintId, ModelAndView result) {
		return this.isInvolved(fixUpTaskId, complaintId, 0, result);
	}

	public ModelAndView isInvolved(int fixUpTaskId, int complaintId, int reportId, ModelAndView result) {
		Boolean isInvolved = this.handyWorkerService.isInvolved(fixUpTaskId, complaintId, reportId);

		if (!isInvolved) {
			result = new ModelAndView("welcome/index");
		}

		return result;
	}
}
