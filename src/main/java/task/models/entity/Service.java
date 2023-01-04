package task.models.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;

    @Override
    public String toString() {
        return  name + " - " + price;
    }

    public static class ServiceBuilder {
        private Service newService;

        public ServiceBuilder() {
            newService = new Service();
        }

        public ServiceBuilder withId(int id) {
            newService.id = id;
            return this;
        }

        public ServiceBuilder withName(String name) {
            newService.name = name;
            return this;
        }

        public ServiceBuilder withPrice(int price) {
            newService.price = price;
            return this;
        }

        public Service build() {
            return newService;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Service service = (Service) o;
        return id == service.id && price == service.price && Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
