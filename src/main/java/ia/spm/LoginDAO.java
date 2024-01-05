package ia.spm;

import javax.servlet.http.Cookie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public boolean validate(UserBean userBean) throws ClassNotFoundException {
        boolean status = false;

        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo?useSSL=false", "suppalapati", "epicrider");

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement("select * from users where email = ? and password = ? ")) {
            preparedStatement.setString(1, userBean.getEmail());
            preparedStatement.setString(2, userBean.getPassword());

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            status = rs.next();

        } catch (SQLException e) {
            // process sql exception
            printSQLException(e);
        }
        return status;
    }
    //to fetch user name and password for indentification
    public static UserBean identifier(String email) throws ClassNotFoundException{

        Class.forName("com.mysql.cj.jdbc.Driver");

        String jdbcUrl = "jdbc:mysql://localhost:3306/demo";
        String username = "suppalapati";
        String password = "epicrider";
        UserBean user = new UserBean();
        try {
            // Open a connection
            System.out.println("Connecting to the database...");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Print a message if connected successfully
            System.out.println("Successfully connected again!");

            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("userID");
                user.setId(Integer.parseInt(userId));
                System.out.println("UserID"+ user.getId());
                user.setEmail(resultSet.getString("email"));
                System.out.println("UserID"+ user.getEmail());
                user.setPassword(resultSet.getString("password"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
