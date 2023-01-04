package task.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private int role_id;
    private long money;
    private int rating;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class UserBuilder {
        private User newUser;

        public UserBuilder() {
            newUser = new User();
        }

        public UserBuilder withId(int id) {
            newUser.id = id;
            return this;
        }

        public UserBuilder withEmail(String email) {
            newUser.email = email;
            return this;
        }

        public UserBuilder withName(String name) {
            newUser.name = name;
            return this;
        }

        public UserBuilder withSurname(String surname) {
            newUser.surname = surname;
            return this;
        }

        public UserBuilder withPassword(String password) {
            newUser.password = password;
            return this;
        }

        public UserBuilder withRole(int role_id) {
            newUser.role_id= role_id;
            return this;
        }

        public UserBuilder withMoney(long money) {
            newUser.money = money;
            return this;
        }

        public UserBuilder withRating(int rating) {
            newUser.rating = rating;
            return this;
        }

        public User build() {
            return newUser;
        }
    }

}
