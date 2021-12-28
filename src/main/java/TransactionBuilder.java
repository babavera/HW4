import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;

public class TransactionBuilder {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/HW4";
    static final String USER = "postgres";
    static final String PASS = "Asdfg123";

    public static String transactionBuilder() throws SQLException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No postgres driver");
        }

        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        System.out.println("Введите ID пользователя");
        String userID = reader.readLine();
        ResultSet rs = statement.executeQuery("SELECT userid FROM \"Users\";");
        ArrayList<String> userIDList = new ArrayList<>();
        while (rs.next()) {
            String s = rs.getString("userid");
            userIDList.add(s);
        }
        while (!userIDList.contains(userID)) {
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
        while (!currencyList.contains(currency)) {
            System.out.println("Аккаунта с данной валютой не существует, введите другую");
            currency = reader.readLine();
        }
        int accountID = 0;
        rs = statement.executeQuery("SELECT accountid FROM \"Accounts\" WHERE userid=" + Integer.parseInt(userID) +
                " AND currency='" + currency + "';");
        rs.next();
        accountID = rs.getInt("accountid");

        String result = null;
        int transactionID = 0;
        rs = statement.executeQuery("SELECT MAX (transactionid) FROM \"Transactions\" ;");
        rs.next();
        transactionID = rs.getInt("max");

        System.out.println("Введите тип операции:");
        System.out.println("1. Пополнение счета;");
        System.out.println("2. Списание со счета;");
        String num = reader.readLine();

        while (true) {
            if (num.equals("1") || num.equals("2")) {
                break;
            } else {
                System.out.println("Неверно введен символ! Введите 1 (пополнение) или 2 (снятие)");
                num = reader.readLine();
            }
        }

        if (num.equals("1")) {
            System.out.println("Введите сумму пополнения");
            int amount = Integer.parseInt(reader.readLine());
            while (true) {
                if (amount >= 0 && amount <= 1000000000) {
                    break;
                } else {
                    System.out.println("Неверно введено значение! Размер транзакции не может " +
                            "превышать 100’000’000 и быть меньше 0!");
                    amount = Integer.parseInt(reader.readLine());
                }
            }
            int actualBalance = 0;
            rs = statement.executeQuery("SELECT balance FROM \"Accounts\" WHERE userid=" + Integer.parseInt(userID) +
                    "AND currency ='" + currency + "';");
            while (rs.next()) {
                actualBalance = rs.getInt("balance");
                if ((actualBalance + amount) > 2000000000) {
                    System.out.println("Пополнение превышает допустимый баланс в 2'000'000'000 на " +
                            Math.abs(actualBalance - amount) + ", введите меньшее значение");
                    amount = Integer.parseInt(reader.readLine());
                }
            }
            statement.executeUpdate("INSERT INTO  \"Transactions\"  VALUES (" + (transactionID + 1) + ", " +
                    accountID + ", " + amount + ");");
            result = "UPDATE \"Accounts\" SET balance=" + (actualBalance + amount) + "WHERE accountid=" + accountID + ";";
        }
        if (num.equals("2")) {
            System.out.println("Введите сумму снятия");
            int amount = Integer.parseInt(reader.readLine());

            while (true) {
                if (amount >= 0 && amount <= 1000000000) {
                    break;
                } else {
                    System.out.println("Неверно введено значение! Размер транзакции не может " +
                            "превышать 100’000’000 и быть меньше 0!");
                    amount = Integer.parseInt(reader.readLine());
                }
            }

            int actualBalance = 0;
            rs = statement.executeQuery("SELECT balance FROM \"Accounts\" WHERE userid=" + Integer.parseInt(userID) +
                    "AND currency ='" + currency + "';");
            while (rs.next()) {
                actualBalance = rs.getInt("balance");
                if ((actualBalance - amount) <= 0) {
                    System.out.println("Сумма снятия больше существующей на балансе. Баланс составляет " +
                            actualBalance + ", введите меньшее значение.");
                    amount = Integer.parseInt(reader.readLine());
                }
            }
            statement.executeUpdate("INSERT INTO  \"Transactions\"  VALUES (" + (transactionID + 1) + ", " +
                    accountID + ", " + -amount + ");");
            result = "UPDATE \"Accounts\" SET balance=" + (actualBalance - amount) + "WHERE accountid=" + accountID + ";";
        }
        statement.close();
        connection.close();
        return result;
    }
}
