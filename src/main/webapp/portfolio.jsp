<%@ page import="ia.spm.Portfolio" %>
<%@ page import="ia.spm.AssetBean" %>
<%@ page import="ia.spm.Prices" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/styles2.css">
    <title>Portfolio</title>
</head>
<body>
<%
    //allow access only if session exists
    String user = null;
    if(session.getAttribute("user") == null){
        response.sendRedirect("index.jsp");
    }else user = (String) session.getAttribute("user");
    String userName = null;
    String sessionID = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("user")) userName = cookie.getValue();
            if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
        }
    }

    Portfolio portfolio = (Portfolio) request.getAttribute("portfolio");

%>
<div class="header">
    <h1>Portfolio View</h1>
</div>

<div class="sections-container">
    <div class="section section1">

        <h2>Your Current Assets:</h2>

        <%List<AssetBean> assetBeans = portfolio.getAssetList();%>

        <table>
            <thead>
            <tr>
                <th>Type</th>
                <th>Company Name</th>
                <th>Symbol</th>
                <th>Broker</th>
                <th>Volume</th>
                <th>Purchase Price</th>
                <th>Current Price</th>
            </tr>
            </thead>
            <tbody>
            <%

                if (assetBeans != null) {
                    for (AssetBean asset : assetBeans) {
            %>
            <tr>
                <td><%= asset.getType() %></td>
                <td><%= asset.getName() %></td>
                <td><%= asset.getSymbol() %></td>
                <td><%= asset.getBroker() %></td>
                <td><%= asset.getVolume() %></td>
                <td><%= asset.getPrice() %></td>
                <td><%= Prices.getPrice(asset.getSymbol()) %></td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>

    <div class="section section2">
        <h2>Your Portfolio Statistics</h2>
        <%
            double sum = 0;
            for (AssetBean item: assetBeans){
                sum = sum + (Prices.getPrice(item.getSymbol()) * item.getVolume());
            }
        %>
        <p> Total Current Portfolio value: <%= sum %> </p>
        <%
            double sum2 = 0;
            for (AssetBean item: assetBeans){
                sum2 = sum2 + (item.getPrice() * item.getVolume());
            }
            double ROI = ((sum - sum2)/sum2)*100;
        %>
        <p> Return on Investment (%): <%= ROI %> </p>
    </div>
    <div class="section section2">
        <!-- Content for the second section -->
        <h2>News</h2>

        <%
            List<String> companyUrls = new ArrayList<>();
            for (AssetBean item: assetBeans){
                String symbol = item.getSymbol();
                String url = "https://finance.yahoo.com/quote/" + symbol;
                companyUrls.add(url);
            }
        %>
        <%
            int i = 0;
            for (String url : companyUrls) {
                i++;
        %> <p> <%= i %> </p> <%
    %>
        <p> <a href="<%= url %>" target="_blank"><%= url %></a></p>
        <%
            }
        %>
    </div>
</div>
<br>
<div >

            <div class="top-right-buttons" >
                <form action="ProfileServlet" method="get">
                    <input type="image" src="images/user.png" alt="Profile" class="submit-image">
                </form>
                <form  action="LogoutServlet" method="post">
                    <button type="submit" >Logout</button>
                </form>
            </div>
            <br>
            <br>

</div>
</body>
</html>
