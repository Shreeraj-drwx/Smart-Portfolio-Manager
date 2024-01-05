package ia.spm;

import java.io.*;
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* Receive file uploaded to the Servlet from the HTML5 form */
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        // Check if the file has a CSV extension
        if (isCSVFile(fileName)) {
            // Specify the path where you want to save the file
            String savePath = "/home/suppalapati/Documents/Java/IA-Stock/src/main/CSVStore/";
            filePart.write(savePath + fileName);
            response.getWriter().print("Success");
            try {
                CsvToDatabase.update();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } else {
            response.getWriter().print("Error: Only CSV files are allowed.");

        }
    }
    private boolean isCSVFile(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }
}

