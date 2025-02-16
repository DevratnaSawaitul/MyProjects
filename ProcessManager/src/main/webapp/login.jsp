<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="css/login.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script defer src="js/login.js"></script>
</head>

<body>
    <div class="container">
        <h2>Welcome! Sign Up to Continue...</h2>

        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" placeholder="Enter username" oninput="validateInputs()" />
            <div id="usernameError" class="error"></div>
        </div>

        <div class="form-group password-toggle">
            <label for="password">Password</label>
            <input type="password" id="password" placeholder="Enter password" oninput="validateInputs()" />
            <span id="toggleIcon" class="toggle-icon" onclick="togglePasswordVisibility()"> 
                <font face="Arial">&#x1f604;</font>
            </span>
            <div id="passwordError" class="error"></div>
        </div>

        <div id="loader" class="loader" style="display: none;"></div>

        <button id="loginButton" class="button" disabled onclick="login()">Login</button>
    </div>
</body>

</html>
