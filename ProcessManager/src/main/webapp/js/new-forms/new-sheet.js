// Select modal and related elements
const sheetModal = document.getElementById("new-sheet-modal");
const saveSheetButton = document.getElementById("save-sheet-btn");
let active_status = false;

// Validate sheet inputs and enable Save button only when all fields are filled
function validateSheetInputs() {
	const fileName = document.getElementById("sheet-file-name").value.trim();
	const version = document.getElementById("sheet-version").value.trim();
	const designNo = document.getElementById("sheet-design-no").value.trim();
	const department = document.getElementById("sheet-department").value.trim();
	const floor = document.getElementById("sheet-floor").value.trim();
	const lastUpdatedBy = document.getElementById("sheet-last-updated-by").value.trim();

	// Check if the file name contains '/' or '\'
	const invalidChars = /[\/\\]/;
	if (invalidChars.test(fileName)) {
		alert("File name cannot contain '/' or '\\'.");
		// Clear the file name input field
		document.getElementById("sheet-file-name").value = "";
		return; // Exit function to prevent enabling the save button
	}

	// Enable Save button only if all fields are filled
	saveSheetButton.disabled = !(fileName && version && designNo && department && floor && lastUpdatedBy);
}

// Open the Add Sheet modal
function openAddSheetModal() {
	document.getElementById("sheet-file-name").value = "";
	document.getElementById("sheet-file-name").removeAttribute("readonly");
	document.getElementById("sheet-version").value = "";
	document.getElementById("sheet-design-no").value = "";
	document.getElementById("sheet-department").value = "";
	document.getElementById("sheet-floor").value = "";
	document.getElementById("sheet-last-updated-by").value = "";

	// Store operation type
	const fileNameField = document.getElementById("sheet-file-name");
	fileNameField.dataset.operation = "add";
	fileNameField.dataset.date = ""; // No date for new sheets

	saveSheetButton.disabled = true; // Initially disabled
	document.getElementById("new-sheet-title").textContent = "Add Sheet";
	sheetModal.classList.add("show");
}

// Open the Edit Sheet modal
function openEditSheetModal(sheet) {
	document.getElementById("sheet-file-name").value = sheet.file_name;
	document.getElementById("sheet-version").value = sheet.version;
	document.getElementById("sheet-design-no").value = sheet.design_no;
	document.getElementById("sheet-department").value = sheet.department;
	document.getElementById("sheet-floor").value = sheet.floor;
	document.getElementById("sheet-last-updated-by").value = sheet.last_updated_by;

	// Make file_name readonly and store operation
	const fileNameField = document.getElementById("sheet-file-name");
	fileNameField.setAttribute("readonly", "true");
	fileNameField.dataset.operation = "update";
	active_status = sheet.status;

	// Store the existing date for update
	fileNameField.dataset.date = sheet.date;

	validateSheetInputs(); // Ensure Save button reflects the field states
	document.getElementById("new-sheet-title").textContent = "Edit Sheet";
	sheetModal.classList.add("show");
}

// Close the Sheet modal
function closeSheetModal() {
	sheetModal.classList.remove("show");
}

// Save the new or edited sheet
function saveSheetData() {
	const fileName = document.getElementById("sheet-file-name").value.trim();
	const version = document.getElementById("sheet-version").value.trim();
	const designNo = document.getElementById("sheet-design-no").value.trim();
	const department = document.getElementById("sheet-department").value.trim();
	const floor = document.getElementById("sheet-floor").value.trim();
	const lastUpdatedBy = document.getElementById("sheet-last-updated-by").value.trim();

	const operation = document.getElementById("sheet-file-name").dataset.operation || "add";
	const existingDate = document.getElementById("sheet-file-name").dataset.date || ""; // Get stored date (for update)

	// Prepare request payload
	const requestData = {
		file_name: fileName,
		version: version,
		department: department,
		design_no: designNo,
		floor: floor,
		last_updated_by: lastUpdatedBy,
		status: active_status
	};

	// **Only add the date field if it's an update and use the existing date**
	if (operation === "update" && existingDate) {
		requestData.date = existingDate;
		requestData.operation = "update";
	} else {
		requestData.operation = "add";
	}

	// Determine API URL
	const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/add_sheets";

	// API Call
	fetch(apiUrl, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(requestData)
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				alert(`Sheet ${operation === "add" ? "added" : "updated"} successfully!`);
				closeSheetModal();
				loadExistingSheets(); // Refresh sheet list
				fetchRecentSheets();
			} else {
				alert(data.msg === "file_name_already_exist" ? "File name already exists!" : "Error occurred!");
			}
		})
		.catch(error => {
			console.error("Error saving sheet data:", error);
			alert("An error occurred while saving the sheet.");
		});
}
