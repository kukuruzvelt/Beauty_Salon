package DAO;

import java.util.ArrayList;
import java.util.stream.Stream;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import task.models.dao.RoleDAO;
import task.models.dao.UserDAO;
import task.models.entity.Role;
import task.models.entity.User;
import task.models.entity.User.UserBuilder;

public class UserDAOTest {

  private UserDAO userDAO;

  public UserDAOTest() {
    userDAO = new UserDAO(TestDB.getSessionFactory(), new BCryptPasswordEncoder());
  }

  @ParameterizedTest
  @MethodSource("emails")
  void getByEmailTest(String email, String userName) {
    Assertions.assertEquals(userDAO.getUser(email).get().getName(), userName);
  }

  @ParameterizedTest
  @MethodSource("IDs")
  void getByIDTest(int id, String userName) {
    Assertions.assertEquals(userDAO.getUser(id).get().getName(), userName);
  }

  @Test
  void registerTest() {
    String testEmail = "TestEmail";
    User user = new User.UserBuilder().withEmail(testEmail)
        .withName("TestName")
        .withSurname("TestSurname").withMoney(0).withRating(0).withRole(3).withPassword("1")
        .build();
    try {
      userDAO.registerUser(user);
    } catch (ConstraintViolationException e) {
      System.out.println(
          "This user has been already registered in previous tests, recreate test DB "
              + "to register user again");
    }
    User registeredUser = userDAO.getUser(testEmail).get();
    Assertions.assertEquals(user.getEmail(), registeredUser.getEmail());
  }

  @Test
  void registerWithCyrillicNameTest() {
    String testEmail = "TestEmail2";
    String testName = "ТестовеІмя";
    String testSurname = "ТестовеПрізвище";
    User user = new User.UserBuilder().withEmail(testEmail)
        .withName(testName)
        .withSurname(testSurname).withMoney(0).withRating(0).withRole(3).withPassword("1")
        .build();
    try {
      userDAO.registerUser(user);
    } catch (ConstraintViolationException e) {
      System.out.println(
          "This user has been already registered in previous tests, recreate test DB "
              + "to register user again");
    }
    User registeredUser = userDAO.getUser(testEmail).get();
    Assertions.assertEquals(user.getEmail(), registeredUser.getEmail());
    Assertions.assertEquals(user.getName(), registeredUser.getName());
    Assertions.assertEquals(user.getSurname(), registeredUser.getSurname());
  }

  @Test
  void getUsersByRoleTest() {
    Role masterRole = new RoleDAO(TestDB.getSessionFactory()).getRole("MASTER").get();
    Role adminRole = new RoleDAO(TestDB.getSessionFactory()).getRole("ADMIN").get();
    ArrayList<User> masters = userDAO.getUsersByRole(masterRole);
    ArrayList<User> admins = userDAO.getUsersByRole(adminRole);
    Assertions.assertEquals(new ArrayList<>(){{
      add(new UserBuilder().withId(3).build());
      add(new UserBuilder().withId(4).build());
    }}, masters);
    Assertions.assertEquals(new ArrayList<>(){{
      add(new UserBuilder().withId(1).build());
    }}, admins);
  }

  private static Stream<Arguments> emails() {
    return Stream.of(
        Arguments.of("admin", "admin_name"),
        Arguments.of("user1", "user_name1"),
        Arguments.of("master1", "master_name1"),
        Arguments.of("master2", "master_name2")
    );
  }

  private static Stream<Arguments> IDs() {
    return Stream.of(
        Arguments.of(1, "admin_name"),
        Arguments.of(2, "user_name1"),
        Arguments.of(3, "master_name1"),
        Arguments.of(4, "master_name2")
    );
  }
}
