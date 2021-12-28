import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

public class TablesCreateScript {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/HW4";
    static final String USER = "postgres";
    static final String PASS = "Asdfg123";

    public static void createTables() throws SQLException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No postgres driver");
        }
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        statement.executeUpdate(readScanner("D:\\QAAutomation\\HomeWork\\HW4\\src\\main\\resources\\Users.sql"));
        statement.executeUpdate(readScanner("D:\\QAAutomation\\HomeWork\\HW4\\src\\main\\resources\\Accounts.sql"));
        statement.executeUpdate(readScanner("D:\\QAAutomation\\HomeWork\\HW4\\src\\main\\resources\\Transactions.sql"));
        statement.close();
        connection.close();
    }

    private static String  readScanner(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Scanner scanner = new Scanner(path);
        return scanner.nextLine();
    }
}
