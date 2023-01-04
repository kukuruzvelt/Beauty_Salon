package task.models.dao;

import java.util.ArrayList;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import task.models.entity.Request;
import task.models.entity.Service;
import task.models.entity.Timeslot;
import task.models.entity.User;
import task.models.enums.REQUEST_STATUS;

@Repository
public class RequestDAO {

    private final SessionFactory sessionFactory;
    private static final String defaultStatus = REQUEST_STATUS.UNDER_CONSIDERATION.toString();
    private static final String inProgressStatus = REQUEST_STATUS.IN_PROGRESS.toString();
    private static final String completeStatus = REQUEST_STATUS.COMPLETED.toString();

    public RequestDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ArrayList<Request> getRequestsForStatus(String request_status) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root).where(root.get("status").in(request_status));

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        ArrayList<Request> result = (ArrayList<Request>) query.getResultList();
        session.close();

        return result;
    }

    public ArrayList<Request> getMastersRequestsForDate(User master, String date) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root).where(root.get("master").in(master), root.get("date").in(date),
                root.get("status").in(inProgressStatus, completeStatus));

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        ArrayList<Request> result = (ArrayList<Request>) query.getResultList();
        session.close();

        return result;
    }

    public ArrayList<Request> getRequestsForUser(User user) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root).where(root.get("user").in(user));

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        ArrayList<Request> result = (ArrayList<Request>) query.getResultList();
        session.close();

        return result;
    }

    public Optional<Request> getRequest(int id) {
        Session session = sessionFactory.openSession();
        Optional<Request> request = Optional.of(session.get(Request.class, id));
        session.close();
        return request;
    }

    public void createRequest(User master, User user, Service service, String date, Timeslot timeslot) {
        Session session = sessionFactory.openSession();

        Request request = new Request();
        request.setDate(date);
        request.setMaster(master);
        request.setStatus(defaultStatus);
        request.setUser(user);
        request.setService(service);
        request.setTimeslot(timeslot);
        request.setVersion(1);

        session.save(request);

        session.close();
    }

    public void updateRequest(Request request) throws IllegalArgumentException {
        Request requestInDB = this.getRequest(request.getId()).get();
        if (requestInDB.getVersion() != request.getVersion()) {
            throw new IllegalArgumentException("This request was already changed");
        }

        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Request> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Request.class);
        Root<Request> root = criteriaUpdate.from(Request.class);
        criteriaUpdate.set("status", request.getStatus());
        criteriaUpdate.set("timeslot", request.getTimeslot());
        criteriaUpdate.set("version", request.getVersion() + 1);
        criteriaUpdate.where(root.get("id").in(request.getId()));

        Transaction transaction = session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
    }
}
