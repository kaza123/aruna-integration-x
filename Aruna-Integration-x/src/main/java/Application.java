
import gui.Login;
import java.sql.SQLException;

/**
 *
 * @author 'Kasun Chamara'
 */
public class Application {

//    private static final SystemIntegrationSyncGUI systemIntegrationSyncGUI = null;

    public static void main(String[] args) throws SQLException {
        Login login = new Login();
        login.setLocationRelativeTo(null);
        login.setResizable(false);
        login.setVisible(true);
    }
}
