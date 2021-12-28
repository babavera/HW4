import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/HW4";
    static final String USER = "postgres";
    static final String PASS = "Asdfg123";

    public static void main(String[] args) throws SQLException, IOException {

        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        TablesCreateScript.createTables();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Добрый день!");
        boolean b = false;
        while (!b) {
            System.out.println("Какую операцию требуется выполнить? Введите номер:");
            System.out.println("1. Добавить пользователя?");
            System.out.println("2. Добавить аккаунт?");
            System.out.println("3. Выполнить транзакцию?");
            System.out.println("4. Выход.");

            String num = scanner.nextLine();

            while (true) {
                if (num.equals("1") || num.equals("2") || num.equals("3") || num.equals("4")) {
                    break;
                } else {
                    System.out.println("Неверно введен символ! Введите число от 1 до 4");
                    num = scanner.next();
                }
            }
            switch (num) {
                case "1": statement.executeUpdate(UserBuilder.userBuilder());
                    break;
                case "2": statement.executeUpdate(AccountBuilder.accountBuilder());
                    break;
                case "3": statement.executeUpdate(TransactionBuilder.transactionBuilder());
                    break;
                case "4":
                    break;
            }
            System.out.println("Хотите продолжить Y/N");
            while (true) {
                String val = scanner.nextLine();
                if (val.equals("Y")) {
                    break;
                }
                if (val.equals("N")) {
                    b = true;
                    break;
                } else {
                    System.out.println("Введено неверное значение. Хотите продолжить? (Y/N)");
                }
            }
        }
        System.out.println("Спасибо, досвидания!");
        statement.close();
        connection.close();
    }
}
