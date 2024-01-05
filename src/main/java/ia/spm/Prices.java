package ia.spm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Prices {
    public static double getPrice(String symbol) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String jdbcUrl = "jdbc:mysql://localhost:3306/demo";
        String username = "suppalapati";
        String password = "epicrider";

        double price = 0;
        try {
            // Open a connection
            System.out.println("Connecting to the database...");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Print a message if connected successfully
            System.out.println("Successfully connected again!");

            String sql = "SELECT * FROM asset_prices WHERE symbol = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, symbol);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                price = resultSet.getDouble("market_price");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
}
