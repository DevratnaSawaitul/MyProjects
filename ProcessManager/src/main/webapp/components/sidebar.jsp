<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Process Management System</title>
    <link rel="stylesheet" href="css/sidebar.css">
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-header">
            <span>Menu</span>
        </div>
        <div class="sidebar-buttons">
            <button onclick="showView('existing-sheets-content')">Sheets</button>

            <!-- Organization Module -->
            <button onclick="toggleOrganization()">Organization</button>
            <div class="organization-module" id="organization-module">
                <button onclick="showView('show-process')">Processes</button>
                <button onclick="showView('show-sub-process')">Sub Processes</button>
                <button onclick="showView('show-tools')">Tools</button>
                <button onclick="showView('show-skills')">Skills</button>
            </div>

            <!-- Recent Sheets Module -->
            <button onclick="toggleRecentSheets()">Recents</button>
            <div class="recent-sheets" id="recent-sheets">
                <!-- Recent sheets will be populated dynamically -->
            </div>
        </div>
    </div>

    <script src="js/sidebar.js"></script>
</body>
</html>
