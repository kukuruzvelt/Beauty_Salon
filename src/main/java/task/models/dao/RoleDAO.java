package task.models.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import task.models.entity.Role;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDAO {

    @Autowired
    private final SessionFactory sessionFactory;

    public RoleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Role> getRole(String roleName) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        criteriaQuery.select(root).where(root.get("name").in(roleName));

        Query query = session.createQuery(criteriaQuery);
        Role role = (Role) query.getSingleResult();

        session.close();
        return Optional.of(role);
    }

    public Optional<Role> getRole(int roleId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        criteriaQuery.select(root).where(root.get("id").in(roleId));

        Query query = session.createQuery(criteriaQuery);
        Role role = (Role) query.getSingleResult();

        session.close();
        return Optional.of(role);
    }

    public List<SimpleGrantedAuthority> roleToAuthorityList(Role role){
        return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
    }

}
