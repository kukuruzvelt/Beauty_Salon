package task.models.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

@Repository
@Entity
@Table(name = "user_service")
@Setter
@Getter
public class UserService implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Service service;

    @ManyToOne
    private User user;
}
