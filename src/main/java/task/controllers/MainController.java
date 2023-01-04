package task.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import task.models.dao.RoleDAO;
import task.models.dao.ServiceDAO;
import task.models.dao.UserDAO;
import task.models.entity.Service;
import task.models.entity.SortByEntity;
import task.models.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@Component
@org.springframework.stereotype.Controller
public class MainController {

    private final UserDAO userDAO;
    private final ServiceDAO serviceDAO;
    private final RoleDAO roleDAO;

    public MainController(UserDAO userDAO, ServiceDAO serviceDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.serviceDAO = serviceDAO;
        this.roleDAO = roleDAO;
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/register")
    public String createUser(@ModelAttribute("user") User user) {
        return "register";
    }

    @PostMapping("/register")
    public String create(Model model, @ModelAttribute("user") User user) {
        try {
            userDAO.registerUser(user);
            model.addAttribute("message", "Success");
            return "main";
        } catch (ConstraintViolationException e) {
            model.addAttribute("error", "This email is already taken");
        }catch (IllegalStateException e){
            model.addAttribute("error", "Fill in all the fields");
        }
        return "register";
    }

    @GetMapping("/catalog")
    public String getCatalog(Model model) {
        ArrayList<User> masters = userDAO.getUsersByRole(roleDAO.getRole("MASTER").get());
        masters.add(0, new User.UserBuilder().withName("Выберете мастера").withSurname("").build());

        ArrayList<Service> services = serviceDAO.getServices();

        model.addAttribute("masters", masters);
        model.addAttribute("services", services);

        return "catalog";
    }

    @PostMapping("/catalog")
    public String catalog(Model model, @ModelAttribute("master_id") Integer master_id) {
        ArrayList<User> masters = userDAO.getUsersByRole(roleDAO.getRole("MASTER").get());
        masters.add(0, new User.UserBuilder().withName("Выберете мастера").withSurname("").build());

        ArrayList<Service> services;
        if (master_id != null && master_id != 0){
            services = serviceDAO.getServicesForMaster(
                    userDAO.getUser(master_id).get());
            User chosenUser = userDAO.getUser(master_id).get();
            masters.remove(chosenUser);
            masters.add(0, chosenUser);
        }
        else {
            services = serviceDAO.getServices();
        }

        model.addAttribute("masters", masters);
        model.addAttribute("services", services);

        return "catalog";
    }

    @GetMapping("staffList")
    public String getStaffList(Model model){
        ArrayList<Service> services = serviceDAO.getServices();
        ArrayList<User> masters = userDAO.getUsersByRole(roleDAO.getRole("MASTER").get());
        ArrayList<SortByEntity> sortBy = new ArrayList<>(Arrays.asList(
                new SortByEntity("rating", "Рейтинг"), new SortByEntity("surname", "Фамилия")));
        services.add(0, new Service.ServiceBuilder().withName("Выберете услугу").build());

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("masters", masters);
        model.addAttribute("services", services);

        return "staff_list";
    }

    @PostMapping("staffList")
    public String staffList(Model model, @ModelAttribute("service_id") Integer service_id
            , @ModelAttribute("sort_by") String sort_by){
        ArrayList<Service> services = serviceDAO.getServices();
        services.add(0, new Service.ServiceBuilder().withName("Выберете услугу").build());
        ArrayList<User> masters;
        ArrayList<SortByEntity> sortBy = new ArrayList<>(Arrays.asList(
                new SortByEntity("rating", "Рейтинг"), new SortByEntity("surname", "Фамилия")));

        if (service_id != null && service_id != 0){
            Service chosenService = serviceDAO.getService(service_id).get();
            services.remove(chosenService);
            services.add(0, chosenService);
            masters = userDAO.getMastersForService(chosenService);
        }
        else {
            masters = userDAO.getUsersByRole(roleDAO.getRole("MASTER").get());
        }


        if (sort_by!=null && !sort_by.equals("")) {

            SortByEntity chosenVariant = sortBy.stream().filter(i->i.getCallback().equals(sort_by)).findFirst().get();
            sortBy.remove(chosenVariant);
            sortBy.add(0, chosenVariant);

            if(sort_by.equals("surname")){
                masters.sort(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o1.getSurname().compareTo(o2.getSurname());
                    }
                });
            }
            else if(sort_by.equals("rating")){
                masters.sort(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return Integer.compare(o2.getRating(), o1.getRating());
                    }
                });
            }
        }

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("masters", masters);
        model.addAttribute("services", services);

        return "staff_list";
    }
}
