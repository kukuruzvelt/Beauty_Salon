package task.models.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import task.models.entity.Role;
import task.models.entity.Service;
import task.models.entity.User;
import task.models.entity.UserService;

@Repository
public class UserDAO {

    private final SessionFactory sessionFactory;
    private final PasswordEncoder passwordEncoder;

    public UserDAO(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUser(int id) {
        Session session = sessionFactory.openSession();
        Optional<User> user = Optional.of(session.get(User.class, id));
        session.close();
        return user;
    }

    public Optional<User> getUser(String email) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(root.get("email").in(email));

        Query query = session.createQuery(criteriaQuery);
        User user = (User) query.getSingleResult();

        session.close();
        return Optional.of(user);
    }

    public ArrayList<User> getUsersByRole(Role role) {
        Session session = sessionFactory.openSession();
        ArrayList<User> result;

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(root.get("role_id").in(role.getId()));

        Query query = session.createQuery(criteriaQuery);
        List<User> userList = query.getResultList();
        result= (ArrayList<User>) userList;
        session.close();

        return result;
    }

    public ArrayList<User> getMastersForService(Service service) {
        ArrayList<User> result = new ArrayList<>();
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserService> criteriaQuery = criteriaBuilder.createQuery(UserService.class);
        Root<UserService> root = criteriaQuery.from(UserService.class);
        criteriaQuery.select(root).where(root.get("service").in(service));

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        List<UserService> userServiceList = query.getResultList();
        for (UserService userService : userServiceList){
            result.add(userService.getUser());
        }
        session.close();

        return result;
    }

    public void registerUser(User user) throws ConstraintViolationException, IllegalStateException {

        Session session = sessionFactory.openSession();

        if (user.getName()==null||user.getSurname()==null||user.getEmail()==null||user.getPassword()==null
                ||user.getName().equals("")||user.getSurname().equals("")||user.getEmail().equals("")||user.getPassword().equals("")){
            throw new IllegalStateException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMoney(0);
        user.setRating(0);
        user.setRole_id(3);
        session.save(user);

        session.close();
    }

    @Transactional
    public void updateUser(User user) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        criteriaUpdate.set("money", user.getMoney());
        criteriaUpdate.where(root.get("id").in(user.getId()));

        Transaction transaction = session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
    }

}
