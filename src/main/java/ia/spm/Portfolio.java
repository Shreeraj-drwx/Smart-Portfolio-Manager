package ia.spm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class Portfolio {
    private int ownerId;
    private List<AssetBean> assetList;

    public Portfolio(int owner){
        this.ownerId = owner;
    }
    public int getOwnerId() {
        return ownerId;
    }

    public List<AssetBean> getAssetList() {
        return assetList;
    }


    public void fetchAssets() throws ClassNotFoundException {

        List<AssetBean> assetList = new ArrayList<>();
        Class.forName("com.mysql.cj.jdbc.Driver");

        String jdbcUrl = "jdbc:mysql://localhost:3306/demo";
        String username = "suppalapati";
        String password = "epicrider";

        try {
            // Open a connection
            System.out.println("Connecting to the database...");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Print a message if connected successfully
            System.out.println("Successfully connected again!");

            String sql = "SELECT * FROM assets WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(this.ownerId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Retrieve data from the result set and store it in variables
                int id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                int date = resultSet.getInt("date");
                String broker = resultSet.getString("broker");
                int volume = resultSet.getInt("volume");
                double price = resultSet.getDouble("price");
                int userID = resultSet.getInt("userID");
                String symbol = resultSet.getString("symbol");

                AssetBean newAsset = new AssetBean(id, type, name, date, broker, volume, price, userID, symbol);
                assetList.add(newAsset);

            }

            this.assetList = assetList;
            
            for (AssetBean asset : assetList) {
                System.out.println(asset.toString());
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
