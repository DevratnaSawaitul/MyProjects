document.addEventListener("DOMContentLoaded", () => {
    loadSubProcesses();
});

/**
 * Fetches all sub-processes from the API and displays them in the table.
 */
function loadSubProcesses() {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_sub_process";
    const jsonData = JSON.stringify({ load_type: "all" });

    const tableBody = document.getElementById("subprocesses-table-body");
    tableBody.innerHTML = "<tr><td colspan='4'>Loading...</td></tr>";

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: jsonData
    })
    .then(response => response.json())
    .then(data => {
        tableBody.innerHTML = "";

        if (data.success  && data.sub_process.length > 0) {
            populateSubProcessTable(data.sub_process);
        } else {
            tableBody.innerHTML = "<tr><td colspan='4'>No sub-processes available.</td></tr>";
        }
    })
    .catch(error => {
        console.error("Error fetching sub-process data:", error);
        tableBody.innerHTML = `<tr><td colspan='4'>Error: ${error.message}</td></tr>`;
    });
}

/**
 * Populates the table with sub-process data.
 */
function populateSubProcessTable(subProcesses) {
    const tableBody = document.getElementById("subprocesses-table-body");
    subProcesses.forEach(subProcess => {
        const row = document.createElement("tr");
        row.dataset.subProcess = JSON.stringify(subProcess); // Store sub-process object in dataset

        row.innerHTML = `
            <td>${subProcess.process_name}</td>
            <td>${subProcess.subprocess_name}</td>
            <td>${subProcess.active ? "Yes" : "No"}</td>
            <td>
                <span class="action-btn edit-btn" title="Edit Sub-Process" onclick="editSubProcess(this)">
                    <font face="Arial">&#x270E;</font> <!-- ✎ Edit Icon -->
                </span>
                <span class="action-btn delete-btn" title="Delete Sub-Process" onclick="deleteSubProcess(this)">
                    <font face="Arial">&#x1F5D1;</font> <!-- 🗑 Delete Icon -->
                </span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

/**
 * Handles adding a sub-process.
 */
function addSubProcess() {
    openAddSubProcessModal(); // Function to open modal for adding sub-process
 //alert("add click");
}

/**
 * Handles editing a sub-process.
 */
function editSubProcess(element) {
    const subProcess = JSON.parse(element.closest("tr").dataset.subProcess);
    openEditSubProcessModal(subProcess); // Function to open modal for editing sub-process
//alert("update click");
}

/**
 * Handles deleting a sub-process.
 */
function deleteSubProcess(element) {
    const subProcess = JSON.parse(element.closest("tr").dataset.subProcess);

    if (!confirm(`Are you sure you want to delete "${subProcess.subprocess_name}"?`)) {
        return; // User canceled the deletion
    }

    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/delete_sub_process";
    const requestData = JSON.stringify({ 
        subprocess_id: subProcess.subprocess_id, 
        subprocess_name: subProcess.subprocess_name 
    });

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: requestData,
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Sub-Process deleted successfully!");
            loadSubProcesses();
        } else {
            alert(`Failed to delete sub-process: ${data.msg || "Unknown error"}`);
        }
    })
    .catch(error => {
        console.error("Error deleting sub-process:", error);
        alert(`Error: ${error.message}`);
    });
}
