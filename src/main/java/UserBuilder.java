import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class UserBuilder {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/HW4";
    static final String USER = "postgres";
    static final String PASS = "Asdfg123";

    public static String userBuilder() throws SQLException, IOException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No postgres driver");
        }
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int userID = 0;
        String name = null;
        System.out.println("Введите имя пользователя");

        while (true) {
            name = reader.readLine();
            if(name.isEmpty()) {
                System.out.println("Имя пользователя должно быть введено");
            } else {
                break;
            }
        }

        ResultSet rs = statement.executeQuery("SELECT MAX (userid) FROM \"Users\";");
        rs.next();
        userID = rs.getInt("max");

        System.out.println("Введите адрес пользователя (не обязательно)");
        String address = reader.readLine();

        statement.close();
        connection.close();

        return "INSERT INTO \"Users\" VALUES (" + (userID+1) + ", '" + name + "', '"
               + address + "');";
    }
}
