package DAO;

import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import task.models.dao.TimeslotDAO;
import task.models.entity.User;
import task.models.service.DatetimeService;

public class TimeslotDAOTest {

  private TimeslotDAO timeslotDAO;

  public TimeslotDAOTest() {
    timeslotDAO = new TimeslotDAO(TestDB.getSessionFactory(), new DatetimeService());
  }

  @Test
  void getAllTest() {
    ArrayList<String> resultTimeslots = new ArrayList<>();
    timeslotDAO.getTimeslots().forEach(i -> resultTimeslots.add(i.toString()));
    Assertions.assertEquals(resultTimeslots, new ArrayList<String>() {
      {
        add("10:00:00");
        add("10:30:00");
        add("11:00:00");
        add("11:30:00");
        add("12:00:00");
      }
    });
  }

  @ParameterizedTest
  @MethodSource("servicesForMaster")
  void getTest(String time, String timeslot) {

  }

  @Test
  void getFreeTest() {
    String date = TestDB.getTestDate();
    User user = new User.UserBuilder().withId(3).build();
    ArrayList<String> resultTimeslots = new ArrayList<>();
    timeslotDAO.getFreeTimeslots(user, date).forEach(i -> resultTimeslots.add(i.toString()));
    Assertions.assertEquals(resultTimeslots, new ArrayList<String>() {
      {
        add("11:00:00");
        add("11:30:00");
        add("12:00:00");
      }
    });

  }

  private static Stream<Arguments> timeslots() {
    return Stream.of(
        Arguments.of(1, "service1 - 100"),
        Arguments.of(2, "service2 - 200"),
        Arguments.of(3, "service3 - 300"),
        Arguments.of(4, "service4 - 400")
    );
  }

}
