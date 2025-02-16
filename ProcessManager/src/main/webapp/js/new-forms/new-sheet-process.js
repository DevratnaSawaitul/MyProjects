// Select modal and related elements
const addSheetProcessModal = document.getElementById("add-sheet-process-modal");
const saveSheetProcessButton = document.getElementById("save-sheet-process-btn");

/**
 * Open the Add Sheet Process modal
 * @param {string} fileName - The file name to be used
 */
function openAddSheetProcessModal(fileName) {
    // Set the file name in the input (read-only)
    document.getElementById("sheet-file-name").value = fileName;
    // Reset the process dropdown
    document.getElementById("select-sheet-process").value = "";
    // Disable save button until a process is selected
    saveSheetProcessButton.disabled = true;
    // Set modal title (if needed)
    document.getElementById("add-sheet-process-title").textContent = "Add Sheet Process";
    // Show the modal
    addSheetProcessModal.classList.add("show");

    // Load the process dropdown options
    loadProcessesForSheetDropdown();
}

/**
 * Close the Add Sheet Process modal
 */
function closeSheetProcessModal() {
    addSheetProcessModal.classList.remove("show");
}

/**
 * Load processes for the dropdown
 */
function loadProcessesForSheetDropdown() {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_process";
    
    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ load_type: "active" })
    })
    .then(response => response.json())
    .then(data => {
        const processDropdown = document.getElementById("select-sheet-process");
        // Reset dropdown options with a default option
        processDropdown.innerHTML = `<option value="">Select Process</option>`;

        if (data.success && data.processes.length > 0) {
            data.processes.forEach(process => {
                const option = document.createElement("option");
                option.value = process.process_name;
                option.textContent = process.process_name;
                processDropdown.appendChild(option);
            });
        }

        // Attach event listener to enable the save button when a process is selected
        processDropdown.addEventListener("change", validateSheetProcessInputs);
    })
    .catch(error => console.error("Error loading processes:", error));
}

/**
 * Validate inputs and enable the save button if a process is selected
 */
function validateSheetProcessInputs() {
    // Note: The ID now matches the HTML element: "select-sheet-process"
    const processName = document.getElementById("select-sheet-process").value;
    saveSheetProcessButton.disabled = processName === "";
}

/**
 * Save the new sheet process
 */
function saveSheetProcessData() {
    const fileName = document.getElementById("sheet-file-name").value;
    // Use the correct ID for the process dropdown
    const processName = document.getElementById("select-sheet-process").value;

    if (!processName) {
        alert("Please select a process.");
        return;
    }

    const requestData = {
        file_name: fileName,
        process_name: processName
    };

    const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/addSheetProcess";

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Sheet process added successfully!");
			loadSingleSheets(fileName);
        } else {
            alert(data.msg === "process_or_sheet_not_exist" 
                  ? "Process or Sheet does not exist!" 
                  : "Error adding sheet process.");
        }
    })
    .catch(error => console.error("Error saving sheet process:", error));
	closeSheetProcessModal(); 
}
