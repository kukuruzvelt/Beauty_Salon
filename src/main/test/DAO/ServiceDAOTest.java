package DAO;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import task.models.dao.ServiceDAO;
import task.models.entity.Service;
import task.models.entity.User;
import task.models.entity.User.UserBuilder;

public class ServiceDAOTest {

  private ServiceDAO serviceDAO;

  public ServiceDAOTest() {
    serviceDAO = new ServiceDAO(TestDB.getSessionFactory());
  }

  @Test
  public void getAllTest() {
    assertEquals(serviceDAO.getServices().size(), 4);
  }

  @ParameterizedTest
  @MethodSource("services")
  void getTest(int id, String service) {
    assertEquals(serviceDAO.getService(id).get().toString(), service);
  }

  @ParameterizedTest
  @MethodSource("servicesForMaster")
  void getForMasterTest(User user, ArrayList<Service> services) {
    ArrayList<String> resultServices = new ArrayList<>();
    serviceDAO.getServicesForMaster(user).forEach(i -> resultServices.add(i.toString()));
    assertEquals(resultServices, services);
  }

  private static Stream<Arguments> services() {
    return Stream.of(
        Arguments.of(1, "service1 - 100"),
        Arguments.of(2, "service2 - 200"),
        Arguments.of(3, "service3 - 300"),
        Arguments.of(4, "service4 - 400")
    );
  }

  private static Stream<Arguments> servicesForMaster() {
    return Stream.of(
        Arguments.of(new UserBuilder().withId(3).build(), new ArrayList<String>() {
          {
            add("service1 - 100");
            add("service2 - 200");
          }
        }),
        Arguments.of(new UserBuilder().withId(4).build(), new ArrayList<String>() {
          {
            add("service3 - 300");
            add("service4 - 400");
          }
        })
    );
  }

}
