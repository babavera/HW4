import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;


public class AccountBuilder {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/HW4";
    static final String USER = "postgres";
    static final String PASS = "Asdfg123";

    public static String accountBuilder() throws SQLException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No postgres driver");
        }
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        System.out.println("Введите ID пользователя");
        String  userID = reader.readLine();

        ResultSet rs = statement.executeQuery("SELECT userid FROM \"Users\";");
        ArrayList<String> userIDList= new ArrayList<>();

        while (rs.next()){
            String s = rs.getString("userid");
            userIDList.add(s);
        }
        while (!userIDList.contains(userID)){
            System.out.println("Пользователя с таким ID не существует. Введите новый ID пользователя.");
            userID = reader.readLine();
        }

        System.out.println("Введите тип валюты");
        String currency = reader.readLine();

        rs = statement.executeQuery("SELECT currency FROM \"Accounts\" WHERE userid=" + Integer.parseInt(userID) + ";");
        ArrayList<String> currencyList = new ArrayList<>();
        while (rs.next()) {
            String s = rs.getString("currency");
            currencyList.add(s);
        }
        while (currencyList.contains(currency)){
            System.out.println("Аккаунт с данной валютой существует, введите другую");
            currency = reader.readLine();
        }
        int accountID = 0;
        rs = statement.executeQuery("SELECT MAX (accountid) FROM \"Accounts\";");
        rs.next();
        accountID = rs.getInt ("max");

        int balance = 0;

        statement.close();
        connection.close();

        return ("INSERT INTO \"Accounts\" VALUES (" + (accountID + 1) + ", " + userID + ", "
                + balance + ", '" + currency + "');");

    }
}


