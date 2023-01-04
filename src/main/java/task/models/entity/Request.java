package task.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String date;
    private String status;
    private int version;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private User master;

    @ManyToOne
    private Service service;

    @ManyToOne
    private Timeslot timeslot;

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", master=" + master +
                ", service=" + service +
                ", timeslot=" + timeslot +
                '}';
    }

    public static class RequestBuilder {
        private Request newRequest;

        public RequestBuilder() {
            newRequest = new Request();
        }

        public RequestBuilder withId(int id) {
            newRequest.id = id;
            return this;
        }

        public RequestBuilder withDate(String date) {
            newRequest.date = date;
            return this;
        }

        public RequestBuilder withStatus(String status) {
            newRequest.status = status;
            return this;
        }

        public RequestBuilder withUser(User user) {
            newRequest.user = user;
            return this;
        }

        public RequestBuilder withTimeslot(Timeslot timeslot) {
            newRequest.timeslot = timeslot;
            return this;
        }

        public RequestBuilder withMaster(User master) {
            newRequest.master = master;
            return this;
        }

        public RequestBuilder withService(Service service) {
            newRequest.service = service;
            return this;
        }

        public RequestBuilder withVersion(int version) {
            newRequest.version = version;
            return this;
        }

        public Request build() {
            return newRequest;
        }
    }
}
