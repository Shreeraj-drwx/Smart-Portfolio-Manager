package ia.spm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Prices {
    public static double getPrice(String symbol) throws ClassNotFoundException {

        double price = 0;
        try {
            Connection connection = MysqlConnection.openConnection();
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
