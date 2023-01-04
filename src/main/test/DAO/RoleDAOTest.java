package DAO;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import task.models.dao.RoleDAO;

public class RoleDAOTest {
  private RoleDAO roleDAO;

  public RoleDAOTest() {
    roleDAO = new RoleDAO(TestDB.getSessionFactory());
  }

  @ParameterizedTest
  @MethodSource("rolesID")
  void getByIDTest(int id, String role) {
    assertEquals(roleDAO.getRole(id).get().toString(), role);
  }

  @ParameterizedTest
  @MethodSource("rolesName")
  void getByNameTest(String name, String role) {
    assertEquals(roleDAO.getRole(name).get().toString(), role);
  }

  private static Stream<Arguments> rolesID() {
    return Stream.of(
        Arguments.of(1, "Role(id=1, name=ADMIN)"),
        Arguments.of(2, "Role(id=2, name=MASTER)"),
        Arguments.of(3, "Role(id=3, name=USER)")
    );
  }

  private static Stream<Arguments> rolesName() {
    return Stream.of(
        Arguments.of("ADMIN", "Role(id=1, name=ADMIN)"),
        Arguments.of("MASTER", "Role(id=2, name=MASTER)"),
        Arguments.of("USER", "Role(id=3, name=USER)")
    );
  }

}
