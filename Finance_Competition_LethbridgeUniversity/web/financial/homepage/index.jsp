<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:p="http://primefaces.org/ui"
      xmlns:pe="http://primefaces.org/ui/extensions"
>
<head>
    <meta http-equiv="content-type" content="text/html" charset="UTF-8"/>
    <title>Home Page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/theme/lethbridge.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/osa/dist/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/resources/osa/dist/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/resources/osa/dist/js/bootstrap.min.js"></script>
    <link rel="icon" type="image/ico" href="${pageContext.request.contextPath}/resources/images/header/logo.png"/>
</head>

<body class="homePage">

<div class="ui-fluid">

    <div class="container">

        <a class="btn btn-primary btn-block" href="#" role="button">Version: 2018.06.05 </a>

        <img src="${pageContext.request.contextPath}/resources/images/header/fiatlogo.png"
             width="900" height="330" style="margin-top: 1px;"/>

        <%--<img src="${pageContext.request.contextPath}/resources/images/header/dhlogo.png"
             width="900" height="130" style="margin-top: 1px;"/>--%>

        <div style="height: 20px;"></div>

        <a class="btn btn-primary btn-block" href="#" role="button" style="font-size: 1em;">
            Financial Investment Analysis and Trading Simulation Software</a>
        <br/>

        <form class="navbar-form navbar-right" action="${pageContext.request.contextPath}/login" method="post">

            <p:panel id="infoPanel">
                <img id="content" src="${pageContext.request.contextPath}/resources/images/header/Info.png" width="32"
                     height="32"  onclick="document.getElementById('id01').style.display='block'"/>
            </p:panel>

            <div class="form-group">
                <input type="text" placeholder="User name" class="form-control" name="username" autofocus>
            </div>
            <div class="form-group">
                <input type="password" placeholder="Password" class="form-control" name="password">
            </div>
            <button type="submit" class="btn btn-primary">Login</button>

        </form>

    </div>

    <footer class="footer">
        <div class="container">
            <p class="text-muted">
            </p>
        </div>
    </footer>

    <div id="id01" class="w3-modal">
        <div class="w3-modal-content w3-animate-top w3-card-4">
            <header class="w3-container w3-teal">
        <span onclick="document.getElementById('id01').style.display='none'"
              class="w3-button w3-display-topright">Close</span>
                <h2>About FIAT SIMULATION SOFTWARE:</h2>
            </header>
            <div class="w3-container">
                <h4>
                    This project was envisioned by a group from the <strong>University of Lethbridge</strong>,
                    Faculty of Management, to enhance the experience of students,
                    professors, and the community by providing the fundamental and technical tools
                    to help market participants make appropriate decisions.
                </h4>
                <h4>
                    The key members of this team include:<br/>
                    <strong>Dr. Shamsul Alam, Lynda Thai-Baird, Clayton Varjassy, and Joshua Lindemann.</strong>
                </h4>
                <h4>The whole development of this application was done by our senior software developer
                    <strong>Sina Golestanirad.</strong>
                </h4>
            </div>
            <footer class="w3-container w3-teal">
                <p>University of Lethbridge</p>
            </footer>
        </div>
    </div>


</div>

</body>
</html>
