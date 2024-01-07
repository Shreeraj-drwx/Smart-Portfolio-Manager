package ia.spm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadHistory {
    private Date uploadDate;
    private String fileName;

    public UploadHistory(Date uploadDate, String fileName) {
        this.uploadDate = uploadDate;
        this.fileName = fileName;
    }

    // Getter and setter methods for uploadDate
    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    // Getter and setter methods for fileName
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static List<UploadHistory> userUploadHistory(int userID){
        List<UploadHistory> uploadHistory = new ArrayList<>();

        try {
            Connection connection = MysqlConnection.openConnection();
            String sql = "SELECT * FROM upload_history WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(userID));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Retrieve data from the result set and store it in variables

                Date date = resultSet.getDate("date");
                String name = resultSet.getString("filename");

                UploadHistory entry = new UploadHistory(date, name);
                uploadHistory.add(entry);

            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadHistory;
    }
}
