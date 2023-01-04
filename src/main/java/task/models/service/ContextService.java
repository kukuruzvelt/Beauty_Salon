package task.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import task.models.dao.UserDAO;
import task.models.entity.User;

@Service
public class ContextService {

    private final UserDAO userDAO;

    public ContextService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserFromContext(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userDAO.getUser(username).get();
        return user;
    }
}
