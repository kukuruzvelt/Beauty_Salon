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
import task.models.entity.Request;
import task.models.entity.Service;
import task.models.entity.Timeslot;
import task.models.entity.User;
import task.models.enums.REQUEST_STATUS;
import task.models.service.ContextService;
import task.models.service.DatetimeService;

import java.util.Comparator;
import java.util.List;

@Component
@org.springframework.stereotype.Controller
@RequestMapping("/master")
public class MasterController {

    private final RequestDAO requestDAO;
    private final TimeslotDAO timeslotDAO;
    private final ContextService contextService;
    private final DatetimeService datetimeService;
    private static final String freeSlot = "FREE";
    private static final String completeStatus = REQUEST_STATUS.COMPLETED.toString();

    public MasterController(RequestDAO requestDAO, TimeslotDAO timeslotDAO, ContextService contextService, DatetimeService datetimeService) {
        this.requestDAO = requestDAO;
        this.timeslotDAO = timeslotDAO;
        this.contextService = contextService;
        this.datetimeService = datetimeService;
    }

    @GetMapping("/main")
    public String main() {
        return "master/master_main";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, @ModelAttribute("message") String message){
        String today = datetimeService.getCurrentDate();
        User master = contextService.getUserFromContext();
        List<Timeslot> timeslots = timeslotDAO.getTimeslots();
        List<Request> requests = requestDAO.getMastersRequestsForDate(master, today);

        timeslots.stream().filter(t -> requests.stream().noneMatch(r -> r.getTimeslot().equals(t)))
                .forEach(t -> requests.add(new Request.RequestBuilder().withId(0).withTimeslot(t)
                        .withService(new Service.ServiceBuilder().withName(freeSlot).build()).build()));

        requests.sort(new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                return o1.getTimeslot().getTime().compareTo(o2.getTimeslot().getTime());
            }
        });

        model.addAttribute("date", today);
        model.addAttribute("requests", requests);
        model.addAttribute("timeslots", timeslots);
        model.addAttribute("completeStatus", completeStatus);
        model.addAttribute("message", message);

        return "master/master_schedule";
    }

    @PostMapping("/complete_request")
    public RedirectView todaysRequests(@ModelAttribute("request_id") Integer request_id,
                                       @ModelAttribute("version") Integer version, RedirectAttributes attributes){
        Request request = requestDAO.getRequest(request_id).get();
        request.setVersion(version);
        String now = datetimeService.getCurrentTime();

        if(request.getTimeslot().getTime().compareTo(now) < 0) {
            request.setStatus(completeStatus);
            try {
                requestDAO.updateRequest(request);
            }
            catch (IllegalArgumentException e){
                attributes.addFlashAttribute("error", e.getMessage());
            }
        }
        else {
            attributes.addFlashAttribute("message", "Время этой записи еще не пришло");
        }

        return new RedirectView("/master/schedule");
    }
    

}
