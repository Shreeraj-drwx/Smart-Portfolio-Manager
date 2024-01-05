<%@ page import="ia.spm.UserBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="styles/styles2.css">
  <title>Profile</title>
</head>
<body>
<%
  String userName = null;
  String sessionID = null;
  Cookie[] cookies = request.getCookies();
  if(cookies !=null){
    for(Cookie cookie : cookies){
      if(cookie.getName().equals("username")) userName = cookie.getValue();
      if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
    }
  }

  UserBean user = (UserBean) request.getAttribute("UserBean");
%>
<div class="header">
  <h1>Profile</h1>
</div>
<br>

<p class="section">First Name: <%=user.getFirstName()%></p>
<p class="section">Last Name: <%=user.getLastName()%></p>
<p class="section">Email: <%=user.getEmail()%></p>


<div class="button-container">
  <div class="top-right-buttons">
    <form action="PortfolioServlet" method="post">
      <button type="submit" >Back to Portfolio</button>
    </form>
    <br>
    <br>
    <form action="LogoutServlet" method="post">
      <button type="submit" >Logout</button>
    </form>
  </div>
  <div class="button-container">
    <form method="post" action="fileuploadservlet" enctype="multipart/form-data">
      <input type="file" name="file" id="file" />
      <input type="submit" value="Upload" />
    </form>
    <% String UploadStatus = (String) request.getAttribute("UploadStatus");
      if (UploadStatus != null && UploadStatus.equals("Failed")) {   %>
    <p> Upload Failed. Please retry!!</p>
    <% } %>
  </div>

</div>
</body>
</html>
