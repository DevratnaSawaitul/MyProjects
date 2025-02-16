document.addEventListener("DOMContentLoaded", () => {
    loadTools();
});

/**
 * Fetches all tools from the API and displays them in the table.
 */
function loadTools() {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_tools";
    const jsonData = JSON.stringify({ load_type: "all" });

    const tableBody = document.getElementById("tools-table-body");
    tableBody.innerHTML = "<tr><td colspan='4'>Loading...</td></tr>";

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: jsonData
    })
    .then(response => response.json())
    .then(data => {
        tableBody.innerHTML = "";

        if (data.success  && data.tools.length > 0) {
            populateToolsTable(data.tools);
        } else {
            tableBody.innerHTML = "<tr><td colspan='4'>No tools available.</td></tr>";
        }
    })
    .catch(error => {
        console.error("Error fetching tools data:", error);
        tableBody.innerHTML = `<tr><td colspan='4'>Error: ${error.message}</td></tr>`;
    });
}

/**
 * Populates the table with tools data.
 */
function populateToolsTable(tools) {
    const tableBody = document.getElementById("tools-table-body");
    tools.forEach(tool => {
        const row = document.createElement("tr");
        row.dataset.tool = JSON.stringify(tool); // Store tool object in dataset

        row.innerHTML = `
            <td>${tool.tool_name}</td>
            <td>${tool.sub_process}</td>
            <td>${tool.active ? "Yes" : "No"}</td>
            <td>
                <span class="action-btn edit-btn" title="Edit Tool" onclick="editTool(this)">
                    <font face="Arial">&#x270E;</font> <!-- ✎ Edit Icon -->
                </span>
                <span class="action-btn delete-btn" title="Delete Tool" onclick="deleteTool(this)">
                    <font face="Arial">&#x1F5D1;</font> <!-- 🗑 Delete Icon -->
                </span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

/**
 * Handles adding a tool.
 */
function addTool() {
    openAddToolsModal(); // Function to open modal for adding tool
    //alert("add click");
}

/**
 * Handles editing a tool.
 */
function editTool(element) {
    const tool = JSON.parse(element.closest("tr").dataset.tool);
    openEditToolsModal(tool); // Function to open modal for editing tool
    //alert("update click");
}

/**
 * Handles deleting a tool.
 */
function deleteTool(element) {
    const tool = JSON.parse(element.closest("tr").dataset.tool);

    if (!confirm(`Are you sure you want to delete "${tool.tool_name}"?`)) {
        return; // User canceled the deletion
    }

    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/delete_tools";
    const requestData = JSON.stringify({ tool_id: tool.tool_id });

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: requestData,
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Tool deleted successfully!");
            loadTools();
        } else {
            alert(`Failed to delete tool: ${data.msg || "Unknown error"}`);
        }
    })
    .catch(error => {
        console.error("Error deleting tool:", error);
        alert(`Error: ${error.message}`);
    });
}
