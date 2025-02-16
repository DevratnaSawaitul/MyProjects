const ENCRYPTION_KEY = ['T', 'h', 'e', 'B', 'e', 's', 't', 'K', 'e', 'y'];

function encrypt(input) {
    let encrypted = '';
    const keyLength = ENCRYPTION_KEY.length;
    for (let i = 0; i < input.length; i++) {
        const encryptedChar = String.fromCharCode(input.charCodeAt(i) + ENCRYPTION_KEY[i % keyLength].charCodeAt(0));
        encrypted += encryptedChar;
    }
    return encrypted;
}

function validateInputs() {
    const usernameField = document.getElementById("username");
    const passwordField = document.getElementById("password");
    const loginButton = document.getElementById("loginButton");

    const username = usernameField.value;
    const password = passwordField.value;

    let isValid = true;

    const usernameError = document.getElementById("usernameError");
    if (username.length < 8 || username.length > 12) {
        usernameError.innerText = "Username must be between 8 and 12 characters.";
        isValid = false;
    } else {
        usernameError.innerText = "";
    }

    const passwordError = document.getElementById("passwordError");
    if (password.length < 8 || password.length > 12) {
        passwordError.innerText = "Password must be between 8 and 12 characters.";
        isValid = false;
    } else {
        passwordError.innerText = "";
    }

    loginButton.disabled = !isValid;
}


function togglePasswordVisibility() {
    const passwordField = document.getElementById("password");
    const toggleIcon = document.getElementById("toggleIcon");
    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleIcon.innerHTML = '<font face="Segoe UI Symbol">&#x1F600;</font>';
    } else {
        passwordField.type = "password";
        toggleIcon.innerHTML = '<font face="Arial">&#x1F648;</font>';
    }
}

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const encryptedUsername = encrypt(username);
    const encryptedPassword = encrypt(password);

    const jsonData = JSON.stringify({
        username: encryptedUsername,
        password: encryptedPassword
    });

    const loader = document.getElementById("loader");
    loader.style.display = "block";

    const apiUrl = `${window.location.origin}/ProcessManager/webapi/genral_controller/login`;

    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: jsonData
    })
        .then(response => response.json())
        .then(data => {
            loader.style.display = "none";
            if (data.success) {
                window.location.replace("main-page.jsp");
            } else {
                Swal.fire({
                    title: "Error",
                    text: data.msg,
                    icon: "error"
                });
            }
        })
        .catch(error => {
            loader.style.display = "none";
            console.error("Error during login:", error);
            Swal.fire({
                title: "Error",
                text: "An unexpected error occurred.",
                icon: "error"
            });
        });
}
