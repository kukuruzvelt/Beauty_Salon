package task.seleniumTests.readProperties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {
    Config config = readConfig();

    static Config readConfig() {
        return ConfigFactory.systemProperties().hasPath("testProfile")
                ? ConfigFactory.load(ConfigFactory.systemProperties().getString("testProfile"))
                : ConfigFactory.load("application.conf");
    }

    String URL = readConfig().getString("url");
    String EMAIL_TAKEN_ERROR_MESSAGE = readConfig().getString("email_taken_error_message");
    String FILLING_ALL_FIELDS_ERROR_MESSAGE = readConfig().getString("filling_all_fields_error_message");

    String ADMIN_LOGIN = readConfig().getString("usersParams.admin.login");
    String ADMIN_PASSWORD = readConfig().getString("usersParams.admin.password");
    String ADMIN_TITLE = readConfig().getString("usersParams.admin.title");

    String MASTER_LOGIN = readConfig().getString("usersParams.master.login");
    String MASTER_PASSWORD = readConfig().getString("usersParams.master.password");
    String MASTER_TITLE = readConfig().getString("usersParams.master.title");

    String USER_LOGIN = readConfig().getString("usersParams.user.login");
    String USER_PASSWORD = readConfig().getString("usersParams.user.password");
    String USER_TITLE = readConfig().getString("usersParams.user.title");

    String NEWUSER_NAME = readConfig().getString("usersParams.newuser.name");
    String NEWUSER_SURNAME = readConfig().getString("usersParams.newuser.surname");
    String NEWUSER_EMAIL = readConfig().getString("usersParams.newuser.email");
    String NEWUSER_PASSWORD = readConfig().getString("usersParams.newuser.password");
    String NEWUSER_MESSAGE = readConfig().getString("usersParams.newuser.message");

}
