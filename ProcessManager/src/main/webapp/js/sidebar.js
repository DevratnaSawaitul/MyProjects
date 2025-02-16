/**
 * Toggles the visibility of the Organization Module.
 */
function toggleOrganization() {
    const organizationModule = document.getElementById("organization-module");
    const recentSheets = document.getElementById("recent-sheets");

    // Close Recent Sheets if open
    if (recentSheets.style.display === "flex") {
        recentSheets.style.display = "none";
    }

    // Toggle Organization Module
    organizationModule.style.display = (organizationModule.style.display === "flex") ? "none" : "flex";
}

/**
 * Toggles the visibility of Recent Sheets.
 */
function toggleRecentSheets() {
    const recentSheets = document.getElementById("recent-sheets");
    const organizationModule = document.getElementById("organization-module");

    // Close Organization if open
    if (organizationModule.style.display === "flex") {
        organizationModule.style.display = "none";
    }

    // Refresh recent sheets when opening
    if (recentSheets.style.display === "none" || recentSheets.style.display === "") {
        fetchRecentSheets();
    }

    // Toggle Recent Sheets
    recentSheets.style.display = (recentSheets.style.display === "flex") ? "none" : "flex";
}

/**
 * Fetches recent sheets from the API and displays them.
 */
function fetchRecentSheets() {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/recent";
    const jsonData = JSON.stringify({});
    const recentSheetsContainer = document.getElementById("recent-sheets");
    recentSheetsContainer.innerHTML = "Loading recent sheets...";

    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: jsonData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("HTTP error! Status: " + response.status);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            recentSheetsContainer.innerHTML = "";
            data.recentSheets.forEach(sheet => {
                const sheetElement = document.createElement("div");
                sheetElement.textContent = sheet;
                sheetElement.className = "recent-sheet";

                sheetElement.onclick = function() {
                    showView('single-sheet-content');
                    loadSingleSheets(sheet);
                };

                recentSheetsContainer.appendChild(sheetElement);
            });
        } else {
            recentSheetsContainer.textContent = "Failed to load recent sheets.";
        }
    })
    .catch(error => {
        console.error("Error fetching recent sheets:", error);
        recentSheetsContainer.textContent = "Error: " + error.message;
    });
}

// Call fetchRecentSheets when the page loads
document.addEventListener("DOMContentLoaded", fetchRecentSheets);
