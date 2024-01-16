package ia.spm;

import java.sql.*;
import java.util.List;

public class PortfolioUpdater {

    public static void updatePortfolio(int ownerID) throws ClassNotFoundException, SQLException {

        Portfolio portfolio = new Portfolio(ownerID);
        portfolio.fetchAssets();
        List<AssetBean> assetList = portfolio.getAssetList();

        double sum = 0;
        for (AssetBean item: assetList){
            sum = sum + (Prices.getPrice(item.getSymbol()) * item.getVolume());
        }

        java.util.Date currentDate = new java.util.Date();
        Date sqlDate = new Date(currentDate.getTime());

        // SQL query to check if a record already exists for the user and date
        String checkExistingQuery = "SELECT COUNT(*) FROM portfolio_values WHERE userID = ? AND date = ?";

        // SQL query to insert values into the portfolio_values table
        String insertQuery = "INSERT INTO portfolio_values (date, value, userID) VALUES (?, ?, ?)";

        try (Connection connection = MysqlConnection.openConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkExistingQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // Set parameters for the check statement
            checkStatement.setInt(1, ownerID);
            checkStatement.setDate(2, sqlDate);

            // Execute the check query
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int existingRecords = resultSet.getInt(1);

            if (existingRecords == 0) {
                // If no record exists, proceed to insert
                // Set parameters for the insert statement
                insertStatement.setDate(1, sqlDate);
                insertStatement.setDouble(2, sum);
                insertStatement.setInt(3, ownerID);

                // Execute the insert query
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully!");
                } else {
                    System.out.println("Failed to insert data.");
                }
            } else {
                System.out.println("Data for the user and date already exists. Skipping insertion.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
