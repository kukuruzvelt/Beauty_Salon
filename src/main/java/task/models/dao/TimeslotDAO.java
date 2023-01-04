package task.models.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import task.models.entity.Request;
import task.models.entity.Timeslot;
import task.models.entity.User;
import task.models.service.DatetimeService;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeslotDAO {

    private final SessionFactory sessionFactory;
    private final DatetimeService datetimeService;

    public TimeslotDAO(SessionFactory sessionFactory, DatetimeService datetimeService) {
        this.sessionFactory = sessionFactory;
        this.datetimeService = datetimeService;
    }

    public ArrayList<Timeslot> getTimeslots() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Timeslot> criteriaQuery = criteriaBuilder.createQuery(Timeslot.class);
        Root<Timeslot> root = criteriaQuery.from(Timeslot.class);
        criteriaQuery.select(root);

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        ArrayList<Timeslot> result = (ArrayList<Timeslot>) query.getResultList();
        session.close();

        return result;
    }

    public Optional<Timeslot> getTimeslot(String time) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Timeslot> criteriaQuery = criteriaBuilder.createQuery(Timeslot.class);
        Root<Timeslot> root = criteriaQuery.from(Timeslot.class);
        criteriaQuery.select(root).where(root.get("time").in(time));

        Query query = session.createQuery(criteriaQuery);
        Timeslot timeslot = (Timeslot) query.getSingleResult();

        session.close();
        return Optional.of(timeslot);
    }

    public ArrayList<Timeslot> getFreeTimeslots(User master, String date) {
        ArrayList<Timeslot> takenTimeslots = new ArrayList<>();
        ArrayList<Timeslot> timeslots = this.getTimeslots();
        ArrayList<Timeslot> result = new ArrayList<>();

        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root).where(root.get("master").in(master), root.get("date").in(date));

        org.hibernate.query.Query query = session.createQuery(criteriaQuery);
        List<Request> requestList = query.getResultList();
        for (Request request : requestList){
            takenTimeslots.add(request.getTimeslot());
        }

        timeslots.stream().filter(i -> !takenTimeslots.contains(i)).forEach(result::add);
        String currentDate = datetimeService.getCurrentDate();
        String currentTime = datetimeService.getCurrentTime();

        if (date.equals(currentDate)){
            ArrayList<Timeslot> copy = (ArrayList<Timeslot>) result.clone();
            copy.stream().filter(i ->i.getTime().compareTo(currentTime)<0).forEach(result::remove);
        }
        session.close();

        return result;
    }

}
