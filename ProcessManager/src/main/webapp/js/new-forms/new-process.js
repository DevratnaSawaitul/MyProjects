// Select modal and related elements
const addProcessModal = document.getElementById("add-process-modal");
const saveProcessButton = document.getElementById("save-add-process-btn");

// Validate process inputs
function validateProcessInputs() {
	const processName = document.getElementById("add-process-name").value.trim();
	saveProcessButton.disabled = processName === ""; // Disable if process name is empty
}

// Open the Add Process modal (no process data)
function openAddProcessModal() {
    const processNameField = document.getElementById("add-process-name");
    processNameField.value = "";
    document.getElementById("add-process-is-active").checked = false;
    processNameField.removeAttribute("readonly");  // Remove readonly for adding new process
    processNameField.dataset.operation = "add";  // Set operation to add
    saveProcessButton.disabled = false;
    document.getElementById("add-process-title").textContent = "Add Process"; // Set title for Add
    addProcessModal.classList.add("show");
}


// Open the Edit Process modal (with existing process data)
function openEditProcessModal(process) {
	document.getElementById("add-process-name").value = process.process_name;
	document.getElementById("add-process-is-active").checked = process.active;

	// Make process_name readonly and set the operation to "update"
	const processNameField = document.getElementById("add-process-name");
	processNameField.setAttribute("readonly", "true");  // Ensure it's readonly

	// Store operation and process ID in the dataset
	processNameField.dataset.operation = "update";
	processNameField.dataset.processId = process.process_id; // Store process ID for update

	saveProcessButton.disabled = false;
	document.getElementById("add-process-title").textContent = "Edit Process"; // Set title for Edit
	addProcessModal.classList.add("show");
}


// Close the Add/Edit Process modal
function closeAddProcessModal() {
	addProcessModal.classList.remove("show");
}

// Save the new or edited process
function saveProcessData() {
	const processName = document.getElementById("add-process-name").value.trim();
	const isActive = document.getElementById("add-process-is-active").checked;
	const operation = document.getElementById("add-process-name").dataset.operation || "add"; // Default operation is "add"
	const processId = document.getElementById("add-process-name").dataset.processId || null; // Process ID for update

	// Prepare the request payload
	const requestData = {
		operation: operation,
		process_name: processName,
		active: isActive,
		process_id: processId // Include process ID for update operation
	};

	// Set the URL dynamically in case it changes
	const apiUrl = window.location.origin + "/ProcessManager/webapi/org/add_process"; // Update this URL if necessary

	// Make the API call to add or update the process
	fetch(apiUrl, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(requestData)
	})
		.then(response => response.json()) // Parse JSON
		.then(data => {
			if (data.success) {
				// Process added or updated successfully
				alert(`Process ${operation === "add" ? "added" : "updated"} successfully!`);
				loadProcesses(); // Refresh the processes list
			} else {
				// Handle failure based on the response message
				if (data.message === "already_exist") {
					alert("Process already exists!");
				} else if (data.message === "process_name_not_exist") {
					alert("Process name does not exist!");
				}
			}
		})
		.catch(error => {
			console.error("Error saving process data:", error);
			alert("An error occurred while saving the process.");
		});

	closeAddProcessModal(); // Close the modal after saving
}
