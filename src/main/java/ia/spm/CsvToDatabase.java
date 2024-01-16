package ia.spm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CsvToDatabase {
    public static void update() throws ClassNotFoundException {

        try (Connection connection = MysqlConnection.openConnection()) {
            File folder = new File(FileUploadServlet.folderPath);

            if (folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

                if (files != null) {
                    for (File csvFile : files) {
                        processCsvFile(csvFile, connection);
                        deleteCsvFile(csvFile);
                    }

                    System.out.println("All CSV files processed successfully.");
                } else {
                    System.out.println("No CSV files found in the specified folder.");
                }
            } else {
                System.out.println("Invalid folder path.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processCsvFile(File csvFile, Connection connection) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Assuming the columns in your CSV are in the order: type, name, volume, date, broker, symbol, price, ownerID
                String type = data[0];
                String name = data[1];
                int volume = Integer.parseInt(data[2]);
                int date = Integer.parseInt(data[3]);
                double price = Double.parseDouble(data[4]);
                int ownerID = Integer.parseInt(data[5]);
                String broker = data[6];
                String symbol = data[7];

                // Prepare the SQL statement
                String sql = "INSERT INTO assets (type, name, volume, date, price, userID, broker, symbol ) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, type);
                    preparedStatement.setString(2, name);
                    preparedStatement.setInt(3, volume);
                    preparedStatement.setInt(4, date);
                    preparedStatement.setDouble(5, price);
                    preparedStatement.setInt(6, ownerID);
                    preparedStatement.setString(7, broker);
                    preparedStatement.setString(8, symbol);
                    // Execute the statement
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Data from " + csvFile.getName() + " imported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteCsvFile(File csvFile) {
        if (csvFile.delete()) {
            System.out.println("File " + csvFile.getName() + " deleted successfully.");
        } else {
            System.out.println("Failed to delete file " + csvFile.getName());
        }
    }
}

