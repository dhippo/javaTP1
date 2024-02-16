import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3307/BDCOMMANDE";
        String user = "root";
        String password = "laflamme";

        try {
            // Établissement de la connexion
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données !");

            // Ici, vous pouvez ajouter plus de code pour interagir avec la base de données

            // Fermeture de la connexion
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }
}
