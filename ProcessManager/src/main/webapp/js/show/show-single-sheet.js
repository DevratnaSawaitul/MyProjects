/**
 * sheetManagement.js
 *
 * This file contains functions to load sheet details, populate sheet/process tables,
 * and handle deletion of sheets, sheet processes, and steps.
 */

/* ========== Loading & Populating Data ========== */
let sheetData = {};
/**
 * Loads a single sheet (and its associated processes) by file name.
 * @param {string} sheet - The file name of the sheet to load.
 */
function loadSingleSheets(sheet) {
	const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/showSheets";
	const jsonData = JSON.stringify({ load_type: "singleSheet", file_name: sheet });

	fetch(apiUrl, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: jsonData
	})
		.then(response => response.json())
		.then(data => {
			if (data.success && data.sheets.length > 0) {
				populateSingleTable(data.sheets);
				sheetData = data;
			} else {
				document.getElementById("sheets-table-body-single-sheet").innerHTML =
					"<tr><td colspan='8'>No sheets available to display.</td></tr>";
			}

			if (data.sheet_process && data.sheet_process.length > 0) {
				populateSheetProcesses(data.sheet_process);
			} else {
				document.getElementById("sheet-process-container").innerHTML =
					"<p style='margin-top: 10px;'>No processes found. Click 'Add Process' to create one.</p>";
			}
		})
		.catch(error => console.error("Error fetching sheet data:", error));
}

/**
 * Populates the sheet details table.
 * Each table row includes data attributes for sheet_id and file_name.
 * @param {Array} sheets - Array of sheet objects.
 */
function populateSingleTable(sheets) {
	const tableBody = document.getElementById("sheets-table-body-single-sheet");
	tableBody.innerHTML = "";

	sheets.forEach(sheet => {
		tableBody.innerHTML += `
            <tr data-sheet-id="${sheet.sheet_id}" data-file-name="${sheet.file_name}">
                <td>${sheet.file_name}</td>
                <td>${sheet.design_no}</td>
                <td>${sheet.department}</td>
                <td>${sheet.floor}</td>
                <td>${sheet.date}</td>
                <td>${sheet.last_updated_by}</td>
                <td>${sheet.date_of_last_update}</td>
                <td>${sheet.version}</td>
            </tr>`;
	});
}

/**
 * Populates the sheet processes and their steps.
 * @param {Array} sheetProcesses - Array of sheet process objects.
 */
function populateSheetProcesses(sheetProcesses) {
	const container = document.getElementById("sheet-process-container");
	container.innerHTML = "";

	sheetProcesses.forEach(process => {
		let stepsHtml = "";
		if (process.steps && process.steps.length > 0) {
			process.steps.forEach(step => {
				stepsHtml += `
                    <tr>
                        <td>${step.step_number}</td>
                        <td>${step.subprocess_name}</td>
                        <td>${step.tool_name}</td>
                        <td>${step.tool_spec}</td>
                        <td>${step.skill}</td>
                        <td>${step.time_minutes} min</td>
                        <td class="shrink">
						<span class="action-btn eye-btn" title="Preview" onclick='openViewStepModal("${process.sheet_process_id}", "${process.process_name}", getSheetFileName(), ${JSON.stringify(step)})'>
						  <font face="Arial">&#128065;</font>
						</span>
						<span class="action-btn edit-btn" title="Edit Step" onclick='openAddStepModal("${process.sheet_process_id}", "${process.process_name}", getSheetFileName(), ${JSON.stringify(step)})'>
						  <font face="Arial">&#x270E;</font>
						</span>
                            <span class="action-btn delete-btn" title="Delete Step" onclick="deleteStep(event, '${step.step_id}')">
                                <font face="Arial">&#x1F5D1;</font>
                            </span>
                        </td>
                    </tr>`;
			});
		} else {
			stepsHtml = `<tr><td colspan="7">No Steps Found</td></tr>`;
		}

		container.innerHTML += `
            <div class="sheet-process">
                <div class="process-header" onclick="toggleSteps('steps-${process.sheet_process_id}')">
                    <span class="toggle-icon">&#9660;</span>
                    <span>${process.process_name}</span>
                    <div class="process-buttons">
                        <button class="add-btn" title="Add Step in the Process" onclick="openAddStepModal('${process.sheet_process_id}', '${process.process_name}', getSheetFileName()); event.stopPropagation();">+ Steps</button>
                        <span class="delete-sheet-process-icon" title="Delete Process" onclick="deleteSheetProcess(event, '${process.sheet_process_id}')">
                            <font face="Arial">&#x1F5D1;</font>
                        </span>
                    </div>
                </div>
                <table id="steps-${process.sheet_process_id}" class="steps-table">
                    <thead>
                        <tr>
                            <th>Step No</th>
                            <th>Sub Process</th>
                            <th>Tool Name</th>
                            <th>Tool Spec</th>
                            <th>Skill</th>
                            <th>Time</th>
                            <th class="shrink">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${stepsHtml}
                    </tbody>
                </table>
            </div>`;
	});
}

/**
 * Toggles the visibility of the steps table with the given ID.
 * Ensures that only one steps table is open at a time.
 * @param {string} tableId - The ID of the table to toggle.
 */
function toggleSteps(tableId) {
	const allTables = document.querySelectorAll(".steps-table");
	allTables.forEach(table => {
		if (table.id !== tableId) {
			table.style.display = "none";
		}
	});

	const table = document.getElementById(tableId);
	table.style.display = (table.style.display === "none" || table.style.display === "") ? "table" : "none";
}

/**
 * Retrieves the file name from the first row of the sheet table.
 * @returns {string} The sheet file name.
 */
function getSheetFileName() {
	const row = document.querySelector("#sheets-table-body-single-sheet tr td:first-child");
	return row ? row.textContent.trim() : "";
}

/**
 * Opens the modal to add a new process.
 * Assumes openAddSheetProcessModal() is defined elsewhere.
 */
function addProcessToSheet() {
	const fileNameElement = document.querySelector("#sheets-table-body-single-sheet tr td:first-child");
	if (fileNameElement) {
		const fileName = fileNameElement.textContent.trim();
		openAddSheetProcessModal(fileName);
	} else {
		alert("File name not found. Please select a valid sheet.");
	}
}

/* ========== Delete Functions ========== */

/**
 * Deletes the currently displayed sheet.
 * Expects the sheet table row to have data attributes for sheet_id and file_name.
 */
function deleteSingleSheet() {
	const row = document.querySelector("#sheets-table-body-single-sheet tr");
	if (row) {
		const sheetId = row.getAttribute("data-sheet-id");
		const fileName = row.getAttribute("data-file-name");
		if (confirm("Are you sure you want to delete this sheet?")) {
			const requestData = { sheet_id: sheetId, file_name: fileName };
			const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/deleteSheet";
			fetch(apiUrl, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(requestData)
			})
				.then(response => response.json())
				.then(data => {
					if (data.success) {
						alert("Sheet deleted successfully!");
						fetchRecentSheets();
						showView("existing-sheets-content");
					} else {
						alert(data.msg === "sheet_not_exist" ? "Sheet does not exist!" : "Error deleting sheet.");
					}
				})
				.catch(error => console.error("Error deleting sheet:", error));
		}
	} else {
		alert("Sheet not found.");
	}
}

/**
 * Deletes a sheet process.
 * Stops propagation so that the steps table does not toggle.
 * @param {Event} e - The click event.
 * @param {string} sheetProcessId - The ID of the sheet process to delete.
 */
function deleteSheetProcess(e, sheetProcessId) {
	e.stopPropagation();
	if (confirm("Are you sure you want to delete this process?")) {
		const requestData = { sheet_process_id: sheetProcessId };
		const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/deleteSheetProcess";
		fetch(apiUrl, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(requestData)
		})
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					alert("Sheet process deleted successfully!");
					const fileName = getSheetFileName();
					loadSingleSheets(fileName);
				} else {
					alert(data.msg === "sheet_process_not_exist" ? "Sheet process does not exist!" : "Error deleting sheet process.");
				}
			})
			.catch(error => console.error("Error deleting sheet process:", error));
	}
}

/**
 * Deletes a step.
 * Stops propagation to prevent unwanted toggling of steps.
 * @param {Event} e - The click event.
 * @param {string} stepId - The ID of the step to delete.
 */
function deleteStep(e, stepId) {
	e.stopPropagation();
	if (confirm("Are you sure you want to delete this step?")) {
		const requestData = { step_id: stepId };
		const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/deleteStep";
		fetch(apiUrl, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(requestData)
		})
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					alert("Step deleted successfully!");
					const fileName = getSheetFileName();
					loadSingleSheets(fileName);
				} else {
					alert(data.msg === "step_not_exist" ? "Step does not exist!" : "Error deleting step.");
				}
			})
			.catch(error => console.error("Error deleting step:", error));
	}
}

/**
 * Shows a preview for the current sheet.
 */
function showSheetPreview() {
	showSheetPreviewModal(sheetData);
}

/**
 * Initiates download for the current sheet.
 */
function downloadSheet() {
	const row = document.querySelector("#sheets-table-body-single-sheet tr");
	if (row) {
		const sheetId = row.getAttribute("data-sheet-id");
		const fileName = row.getAttribute("data-file-name");
		const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/generateExcel";
		const requestData = JSON.stringify({ sheet_id: sheetId });

		fetch(apiUrl, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: requestData
		})
			.then(response => {
				if (!response.ok) {
					throw new Error("Failed to download the file.");
				}
				return response.blob();
			})
			.then(blob => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement("a");
				a.href = url;
				a.download = `${fileName}.xlsx`;
				document.body.appendChild(a);
				a.click();
				window.URL.revokeObjectURL(url);
				document.body.removeChild(a);
			})
			.catch(error => console.error("Error downloading sheet:", error));
	} else {
		alert("Sheet not found.");
	}
}

