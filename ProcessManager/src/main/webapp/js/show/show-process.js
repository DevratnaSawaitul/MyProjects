document.addEventListener("DOMContentLoaded", () => {
	loadProcesses();
});

/**
 * Fetches all process data from the API and displays it in the table.
 */
function loadProcesses() {
	const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_process"; // API endpoint
	const jsonData = JSON.stringify({ load_type: "all" });

	const tableBody = document.getElementById("processes-table-body");
	tableBody.innerHTML = "<tr><td colspan='3'>Loading...</td></tr>";

	fetch(apiUrl, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: jsonData,
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			tableBody.innerHTML = ""; // Clear loading message

			if (data.success === true && data.processes.length > 0) {
				populateProcessTable(data.processes);
			} else {
				tableBody.innerHTML =
					"<tr><td colspan='3'>No processes available.</td></tr>";
			}
		})
		.catch(error => {
			console.error("Error fetching process data:", error);
			tableBody.innerHTML = `<tr><td colspan='3'>Error: ${error.message}</td></tr>`;
		});
}

/**
 * Populates the table with process data.
 */
function populateProcessTable(processes) {
	const tableBody = document.getElementById("processes-table-body");
	tableBody.innerHTML = ""; // Clear previous data

	processes.forEach((process) => {
		const row = document.createElement("tr");
		row.dataset.process = JSON.stringify(process); // Store process object in dataset

		row.innerHTML = `
            <td>${process.process_name}</td>
            <td>${process.active ? "Yes" : "No"}</td>
            <td>
                <span class="action-btn edit-btn" title="Edit Process" onclick="editProcess(this)">
                    <font face="Arial">&#x270E;</font> <!-- ✎ Edit Icon -->
                </span>
                <span class="action-btn delete-btn" title="Delete Process" onclick="deleteProcess(this)">
                    <font face="Arial">&#x1F5D1;</font> <!-- 🗑 Delete Icon -->
                </span>
            </td>
        `;
		tableBody.appendChild(row);
	});
}

/**
 * Handles deleting a process.
 */
function deleteProcess(element) {
	const process = JSON.parse(element.closest("tr").dataset.process);

	if (!confirm(`Are you sure you want to delete "${process.process_name}"?`)) {
		return; // User canceled the deletion
	}

	const apiUrl = window.location.origin + "/ProcessManager/webapi/org/delete_process";
	const requestData = JSON.stringify({ process_id: process.process_id, process_name: process.process_name });

	fetch(apiUrl, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: requestData,
	})
		.then(response => response.json()) // Parse JSON
		.then(data => {
			console.log("API Response:", data); // Debugging log

			if (data.success) { // Correctly checking the response
				alert("Process deleted successfully!");
				loadProcesses();
			} else {
				alert(`Failed to delete process: ${data.msg || "Unknown error"}`);
			}
		})
		.catch(error => {
			console.error("Error deleting process:", error);
			alert(`Error: ${error.message}`);
		});
}

/**
 * Handles editing a process.
 */
function editProcess(element) {
    const process = JSON.parse(element.closest("tr").dataset.process);
    openEditProcessModal(process); // Open modal for editing with process data
}

/**
 * Handles adding a process.
 */
function addProcess() {
    openAddProcessModal(); // Open modal for adding a new process
}
