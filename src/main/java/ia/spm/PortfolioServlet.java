package ia.spm;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/PortfolioServlet")
public class PortfolioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        int owner = user.getId();

        Portfolio portfolio = new Portfolio(owner);
        try {
             portfolio.fetchAssets();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Portfolio Owner:" + portfolio.getOwnerId() );
        List<AssetBean> assetList = portfolio.getAssetList();
        for (AssetBean asset : assetList) {
            System.out.println(asset.toString());
        }

        request.setAttribute("portfolio", portfolio);
        RequestDispatcher dispatcher = request.getRequestDispatcher("portfolio.jsp");
        dispatcher.forward(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
