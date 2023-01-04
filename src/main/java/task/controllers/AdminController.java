package task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import task.models.dao.RequestDAO;
import task.models.dao.TimeslotDAO;
import task.models.dao.UserDAO;
import task.models.entity.Request;
import task.models.entity.Service;
import task.models.entity.Timeslot;
import task.models.entity.User;
import task.models.enums.REQUEST_STATUS;
import task.models.service.DatetimeService;

import java.util.List;

@Component
@org.springframework.stereotype.Controller
@RequestMapping("/admin")
public class AdminController {

    private final RequestDAO requestDAO;
    private final UserDAO userDAO;
    private final TimeslotDAO timeslotDAO;
    private final DatetimeService datetimeService;
    private static final String canceledStatus = REQUEST_STATUS.CANCELED.toString();
    private static final String approvedStatus = REQUEST_STATUS.IN_PROGRESS.toString();
    private static final String defaultStatus = REQUEST_STATUS.UNDER_CONSIDERATION.toString();

    public AdminController(RequestDAO requestDAO, UserDAO userDAO, TimeslotDAO timeslotDAO, DatetimeService datetimeService) {
        this.requestDAO = requestDAO;
        this.userDAO = userDAO;
        this.timeslotDAO = timeslotDAO;
        this.datetimeService = datetimeService;
    }

    @GetMapping("/main")
    public String main() {
        return "admin/admin_main";
    }

    @GetMapping("/choose_request")
    public String chooseRequest(Model model) {
        List<Request> requests = requestDAO.getRequestsForStatus(defaultStatus);
        if (requests.size() > 0) {
            model.addAttribute("requests", requests);
        } else {
            model.addAttribute("message", "Нет запросов на рассмотрение");
        }
        return "admin/choose_request";
    }

    @PostMapping("/manage_request")
    public String manageRequests(Model model, @ModelAttribute("request_id") Integer request_id) {
        Request request = requestDAO.getRequest(request_id).get();
        boolean isActual = true;

        String currentDate = datetimeService.getCurrentDate();
        String currentTime = datetimeService.getCurrentTime();

        if (request.getDate().compareTo(currentDate) < 0 || (request.getDate().equals(currentDate) &&
                request.getTimeslot().getTime().compareTo(currentTime) < 0)) {
            isActual = false;
        }

        model.addAttribute("isActual", isActual);
        model.addAttribute("request", request);
        return "admin/manage_requests";
    }

    @PostMapping("/request_cancel")
    public RedirectView requestCancel(Model model, @ModelAttribute("request_id") Integer request_id
            , @ModelAttribute("version") Integer version, RedirectAttributes attributes) {
        Request request = requestDAO.getRequest(request_id).get();
        Service service = request.getService();
        User user = request.getUser();

        user.setMoney(user.getMoney() + service.getPrice());
        userDAO.updateUser(user);

        request.setStatus(canceledStatus);
        request.setVersion(version);
        try {
            requestDAO.updateRequest(request);
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/admin/choose_request");

    }

    @PostMapping("/request_approve")
    public RedirectView requestApprove(Model model, @ModelAttribute("request_id") Integer request_id
            , @ModelAttribute("version") Integer version, RedirectAttributes attributes) {
        Request request = requestDAO.getRequest(request_id).get();

        request.setStatus(approvedStatus);
        request.setVersion(version);
        try {
            requestDAO.updateRequest(request);
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/admin/choose_request");
    }


    @PostMapping("/request_change")
    public String requestChange(Model model, @ModelAttribute("request_id") Integer request_id
            , @ModelAttribute("version") Integer version) {
        Request request = requestDAO.getRequest(request_id).get();
        request.setVersion(version);
        List<Timeslot> freeTimeslots = timeslotDAO.getFreeTimeslots(request.getMaster(), request.getDate());

        if (freeTimeslots.size() < 1) {
            model.addAttribute("error", "Нет свободных слотов для смены времени");
            model.addAttribute("requests", requestDAO.getRequestsForStatus(defaultStatus));
            return "/admin/choose_request";
        }

        model.addAttribute("request", request);
        model.addAttribute("timeslots", freeTimeslots);
        return "admin/update_timeslot";
    }

    @PostMapping("/request_update_time")
    public RedirectView requestUpdateTime(Model model, @ModelAttribute("request_id") Integer request_id
            , @ModelAttribute("version") Integer version, @ModelAttribute("time") String time
            , RedirectAttributes attributes) {
        System.out.println("ID " + request_id + " TIME " + time);
        Request request = requestDAO.getRequest(request_id).get();
        request.setTimeslot(timeslotDAO.getTimeslot(time).get());
        request.setVersion(version);
        try {
            requestDAO.updateRequest(request);
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/admin/choose_request");
    }


}
