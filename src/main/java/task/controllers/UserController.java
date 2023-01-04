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
import task.models.dao.ServiceDAO;
import task.models.dao.TimeslotDAO;
import task.models.dao.UserDAO;
import task.models.entity.Request;
import task.models.entity.Service;
import task.models.entity.Timeslot;
import task.models.entity.User;
import task.models.service.ContextService;
import task.models.service.DatetimeService;

import java.util.List;

@Component
@org.springframework.stereotype.Controller
@RequestMapping("/user")
public class UserController {

    private final UserDAO userDAO;
    private final ServiceDAO serviceDAO;
    private final RequestDAO requestDAO;
    private final TimeslotDAO timeslotDAO;
    private final DatetimeService datetimeService;
    private final ContextService contextService;

    public UserController(UserDAO userDAO, ServiceDAO serviceDAO, RequestDAO requestDAO, TimeslotDAO timeslotDAO,
                          DatetimeService datetimeService, ContextService contextService) {
        this.userDAO = userDAO;
        this.serviceDAO = serviceDAO;
        this.requestDAO = requestDAO;
        this.timeslotDAO = timeslotDAO;
        this.datetimeService = datetimeService;
        this.contextService = contextService;
    }

    @GetMapping("/main")
    public String main(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("message", message);
        return "user/user_main";
    }

    @GetMapping("/payment")
    public String payment() {
        return "user/payment";
    }

    @PostMapping("/payment")
    public String pay(Model model, @ModelAttribute("money") String moneyAmount) {
        try {
            long money = Long.parseLong(moneyAmount);
            if (money <= 0) {
                model.addAttribute("error", "Сумма должна быть больше 0");
                return "user/payment";
            }

            User user = contextService.getUserFromContext();
            long currentMoney = user.getMoney();

            if (currentMoney + money < 0) {
                model.addAttribute("error", "Сумма на вашем счету не может превышать " + Long.MAX_VALUE);
                return "user/payment";
            }

            user.setMoney(currentMoney + money);
            userDAO.updateUser(user);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.addAttribute("error", "Сумма должна быть меньше " + Long.MAX_VALUE);
            return "user/payment";
        }

        model.addAttribute("message", "Оплата прошла успешно");
        return "user/user_main";
    }

    @GetMapping("/request_service")
    public String requestService(Model model) {
        model.addAttribute("services", serviceDAO.getServices());
        return "user/request/choose_service";
    }


    @PostMapping("/request_master")
    public String requestMaster(Model model, @ModelAttribute("service_id") Integer service_id) {
        User user = contextService.getUserFromContext();
        Service service = serviceDAO.getService(service_id).get();

        if(user.getMoney()<service.getPrice()){
            model.addAttribute("error", "Недостаточно средств на счету для записи на эту услугу");
            return "user/user_main";
        }

        model.addAttribute("service_id", service_id);
        model.addAttribute("masters", userDAO.getMastersForService(serviceDAO.getService(service_id).get()));
        return "user/request/choose_master";
    }

    @PostMapping("/request_date")
    public String requestDate(Model model, @ModelAttribute("service_id") Integer service_id,
                              @ModelAttribute("master_id") Integer master_id) {
        String min = datetimeService.getCurrentDate();
        String max = datetimeService.getDateInTwoWeeks();

        model.addAttribute("service_id", service_id);
        model.addAttribute("master_id", master_id);
        model.addAttribute("min", min);
        model.addAttribute("max", max);

        return "user/request/choose_date";
    }

    @PostMapping("/request_time")
    public String requestTime(Model model, @ModelAttribute("service_id") Integer service_id,
                              @ModelAttribute("master_id") Integer master_id, @ModelAttribute("date") String date) {

        List<Timeslot> timeslots = timeslotDAO.getFreeTimeslots(userDAO.getUser(master_id).get(), date);
        if(timeslots.size()>0) {
            model.addAttribute("service_id", service_id);
            model.addAttribute("master_id", master_id);
            model.addAttribute("date", date);
            model.addAttribute("timeslots", timeslots);
        }
        else {
            model.addAttribute("error", "Нет свободных мест");
        }

        return "user/request/choose_time";
    }

    @PostMapping("create_request")
    public RedirectView createRequest(Model model, @ModelAttribute("service_id") Integer service_id,
                                      @ModelAttribute("master_id") Integer master_id, @ModelAttribute("date") String date,
                                      @ModelAttribute("time") String time, RedirectAttributes attributes) {

        User user = contextService.getUserFromContext();
        Service service = serviceDAO.getService(service_id).get();
        Timeslot timeslot = timeslotDAO.getTimeslot(time).get();
        User master = userDAO.getUser(master_id).get();

        user.setMoney(user.getMoney() - service.getPrice());
        userDAO.updateUser(user);

        requestDAO.createRequest(master, user, service, date, timeslot);

        attributes.addFlashAttribute("message", "Успешная запись на услугу");

        return new RedirectView("/user/main");
    }

    @GetMapping("/requests")
    public String getRequests(Model model){
        List<Request> requests= requestDAO.getRequestsForUser(contextService.getUserFromContext());
        model.addAttribute("requests", requests);

        return "/user/get_requests";
    }


}
