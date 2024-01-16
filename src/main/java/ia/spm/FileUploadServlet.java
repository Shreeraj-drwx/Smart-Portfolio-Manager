package ia.spm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "FileUploadServlet", urlPatterns = { "/fileuploadservlet" })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class FileUploadServlet extends HttpServlet {

    private static final String CSV_CONTENT_TYPE = "text/csv";
    public static String folderPath = System.getProperty("user.home") + File.separator + "uploads";

    static {
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* Receive file uploaded to the Servlet from the HTML5 form */
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        // Check if the file has a CSV extension
        if (isCSVFile(fileName)) {
            // Specify the path where you want to save the file
            String savePath = "/Users/sraj/IdeaProjects/Smart-Portfolio-Manager/src/main/CSVStore/";
            filePart.write(savePath + fileName);

            Cookie[] cookies = request.getCookies(); // Retrieve all cookies from the request
            String email = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) { // Check for the cookie named "user"
                        email = cookie.getValue(); // Retrieve the value of the cookie
                        // Use the username as needed
                        System.out.println("Username from cookie @portfolio: " + email);
                        break; // Exit the loop after finding the cookie
                    }
                }
            }
            UserBean user = null;
            try {
                user = LoginDAO.identifier(email);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            int ownerId = user.getId();
            Date uploadDate = new Date();
            String name = fileName;
            // Save upload history to the database
            saveUploadHistory(uploadDate, name, ownerId);
            try {
                CsvToDatabase.update();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().print("Success");

        } else {
            response.getWriter().print("Error: Only CSV files are allowed.");

        }
    }
    private void saveUploadHistory(Date uploadDate, String fileName, int ownerId) {
        try (Connection connection = MysqlConnection.openConnection()) {
            String sql = "INSERT INTO upload_history (date, filename, userID) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setTimestamp(1, new java.sql.Timestamp(uploadDate.getTime()));
                preparedStatement.setString(2, fileName);
                preparedStatement.setInt(3, ownerId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isCSVFile(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }
}

