// Select modal and related elements
const addToolsModal = document.getElementById("add-tools-modal");
const saveToolsButton = document.getElementById("save-tools-btn");

/**
 * Validate tool inputs
 */
function validateToolInputs() {
	const toolName = document.getElementById("tool-name").value.trim();
	saveToolsButton.disabled = toolName === "";
}

/**
 * Open the Add Tool modal (for adding new tool)
 */
function openAddToolsModal() {
	document.getElementById("tool-name").value = "";
	document.getElementById("tool-is-active").checked = false;
	document.getElementById("select-sub-process").value = ""; // Reset dropdown

	// Enable input fields for adding new tool
	document.getElementById("tool-name").removeAttribute("readonly");
	document.getElementById("select-sub-process").removeAttribute("disabled");

	document.getElementById("tool-name").dataset.operation = "add"; // Set operation to add
	saveToolsButton.disabled = true;
	document.getElementById("add-tools-title").textContent = "Add Tool"; // Set modal title
	addToolsModal.classList.add("show");

	loadSubProcessesForDropdown(); // Load sub-processes into dropdown
}

/**
 * Open the Edit Tool modal (with existing tool data)
 */
function openEditToolsModal(tool) {
	document.getElementById("tool-name").value = tool.tool_name;
	document.getElementById("tool-is-active").checked = tool.active;

	document.getElementById("tool-name").dataset.operation = "update";
	document.getElementById("tool-name").dataset.toolId = tool.tool_id;

	document.getElementById("add-tools-title").textContent = "Edit Tool";
	addToolsModal.classList.add("show");
	saveToolsButton.disabled = false;

	// Load sub-processes and preselect the correct one
	loadSubProcessesForDropdown(tool.sub_process, true);

	// ❌ Make fields read-only in edit mode
	document.getElementById("tool-name").setAttribute("readonly", "true");
	document.getElementById("select-sub-process").setAttribute("disabled", "true");

	// ✅ Only active checkbox should be editable
	document.getElementById("tool-is-active").removeAttribute("disabled");
}

/**
 * Close the Add/Edit Tools modal
 */
function closeToolsModal() {
	addToolsModal.classList.remove("show");
}

/**
 * Load sub-processes for the dropdown
 */
function loadSubProcessesForDropdown(selectedSubProcess = "", isReadonly = false) {
	const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_sub_process";
	fetch(apiUrl, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ load_type: "active" })
	})
		.then(response => response.json())
		.then(data => {
			const subProcessDropdown = document.getElementById("select-sub-process");
			subProcessDropdown.innerHTML = `<option value="">Select Sub-Process</option>`;

			if (data.success && data.sub_process.length > 0) {
				data.sub_process.forEach(subProcess => {
					const option = document.createElement("option");
					option.value = subProcess.subprocess_name;
					option.textContent = subProcess.subprocess_name;
					if (subProcess.subprocess_name === selectedSubProcess) {
						option.selected = true;
					}
					subProcessDropdown.appendChild(option);
				});

				// ❌ Disable dropdown in edit mode
				if (isReadonly) {
					subProcessDropdown.setAttribute("disabled", "true");
				} else {
					subProcessDropdown.removeAttribute("disabled");
				}
			}
		})
		.catch(error => console.error("Error loading sub-processes:", error));
}

/**
 * Save the new or edited tool
 */
function saveToolData() {
	const subProcessName = document.getElementById("select-sub-process").value;
	const toolName = document.getElementById("tool-name").value.trim();
	const isActive = document.getElementById("tool-is-active").checked;
	const operation = document.getElementById("tool-name").dataset.operation || "add";
	const toolId = document.getElementById("tool-name").dataset.toolId || null;

	if (!subProcessName) {
		alert("Please select a sub-process.");
		return;
	}

	const requestData = {
		operation: operation,
		active: isActive,
		sub_process: subProcessName,
		tool_name: toolName,
		tool_id: toolId
	};

	const apiUrl = window.location.origin + "/ProcessManager/webapi/org/add_tools";

	// Make the API call to add or update the tool
	fetch(apiUrl, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(requestData)
	})
		.then(response => response.json()) // Parse JSON
		.then(data => {
			if (data.success) {
				// Tool added or updated successfully
				alert(`Tool ${operation === "add" ? "added" : "updated"} successfully!`);
				loadTools(); // Refresh the tools list
			} else {
				// Handle failure based on the response message
				if (data.message === "already_exist") {
					alert("Tool already exists!");
				} else if (data.message === "tool_name_not_exist") {
					alert("Tool name does not exist!");
				} else {
					alert("Error saving tool.");
				}
			}
		})
		.catch(error => {
			console.error("Error saving tool:", error);
			alert("An error occurred while saving the tool.");
		});

	closeToolsModal(); // Close the modal after saving
}
