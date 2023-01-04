package task.models.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import task.models.entity.Service;
import task.models.entity.User;
import task.models.entity.UserService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceDAO {

    private final SessionFactory sessionFactory;

    public ServiceDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ArrayList<Service> getServices(){
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Service> criteriaQuery = criteriaBuilder.createQuery(Service.class);
        Root<Service> root = criteriaQuery.from(Service.class);
        criteriaQuery.select(root);

        Query query = session.createQuery(criteriaQuery);
        ArrayList<Service> result = (ArrayList<Service>) query.getResultList();
        session.close();

        return result;
    }


    public Optional<Service> getService(int id){
        Session session = sessionFactory.openSession();
        Optional<Service> service = Optional.of(session.get(Service.class, id));
        session.close();
        return service;
    }

    public ArrayList<Service> getServicesForMaster(User user){
        ArrayList<Service> result = new ArrayList<>();
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserService> criteriaQuery = criteriaBuilder.createQuery(UserService.class);
        Root<UserService> root = criteriaQuery.from(UserService.class);
        criteriaQuery.select(root).where(root.get("user").in(user));

        Query query = session.createQuery(criteriaQuery);
        List<UserService> userServiceList = query.getResultList();
        for (UserService userService : userServiceList){
            result.add(userService.getService());
        }
        session.close();

        return result;
    }
}
