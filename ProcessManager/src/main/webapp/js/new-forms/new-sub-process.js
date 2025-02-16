// Select modal and related elements
const addSubProcessModal = document.getElementById("add-sub-process-modal");
const saveSubProcessButton = document.getElementById("save-sub-process-btn");

/**
 * Validate sub-process inputs
 */
function validateSubProcessInputs() {
    const subProcessName = document.getElementById("sub-process-name").value.trim();
    saveSubProcessButton.disabled = subProcessName === "";
}

/**
 * Open the Add Sub-Process modal (for adding new sub-process)
 */
function openAddSubProcessModal() {
    document.getElementById("sub-process-name").value = "";
    document.getElementById("sub-process-is-active").checked = false;
    document.getElementById("select-process").value = ""; // Reset process dropdown

    // Enable input fields for adding new sub-process
    document.getElementById("sub-process-name").removeAttribute("readonly");
    document.getElementById("select-process").removeAttribute("disabled");

    document.getElementById("sub-process-name").dataset.operation = "add"; // Set operation to add
    saveSubProcessButton.disabled = true;
    document.getElementById("add-sub-process-title").textContent = "Add Sub-Process"; // Set modal title
    addSubProcessModal.classList.add("show");

    loadProcessesForDropdown(); // Load processes into dropdown
}

/**
 * Open the Edit Sub-Process modal (with existing sub-process data)
 */
function openEditSubProcessModal(subProcess) {
    document.getElementById("sub-process-name").value = subProcess.subprocess_name;
    document.getElementById("sub-process-is-active").checked = subProcess.active;

    document.getElementById("sub-process-name").dataset.operation = "update";
    document.getElementById("sub-process-name").dataset.subProcessId = subProcess.subprocess_id;

    document.getElementById("add-sub-process-title").textContent = "Edit Sub-Process";
    addSubProcessModal.classList.add("show");

    // Load processes and preselect the correct one
    loadProcessesForDropdown(subProcess.process_name, true); 

    // ❌ Make fields read-only in edit mode
    document.getElementById("sub-process-name").setAttribute("readonly", "true"); 
    document.getElementById("select-process").setAttribute("disabled", "true"); 

	saveSubProcessButton.disabled = false;
	
    // ✅ Only active checkbox should be editable
    document.getElementById("sub-process-is-active").removeAttribute("disabled");
}

/**
 * Close the Add/Edit Sub-Process modal
 */
function closeSubProcessModal() {
    addSubProcessModal.classList.remove("show");
}

/**
 * Load processes for the dropdown
 */
function loadProcessesForDropdown(selectedProcess = "", isReadonly = false) {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_process";
    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ load_type: "active" })
    })
    .then(response => response.json())
    .then(data => {
        const processDropdown = document.getElementById("select-process");
        processDropdown.innerHTML = `<option value="">Select Process</option>`;
        
        if (data.success && data.processes.length > 0) {
            data.processes.forEach(process => {
                const option = document.createElement("option");
                option.value = process.process_name;
                option.textContent = process.process_name;
                if (process.process_name === selectedProcess) {
                    option.selected = true;
                }
                processDropdown.appendChild(option);
            });

            // ❌ Disable dropdown in edit mode
            if (isReadonly) {
                processDropdown.setAttribute("disabled", "true");
            } else {
                processDropdown.removeAttribute("disabled");
            }
        }
    })
    .catch(error => console.error("Error loading processes:", error));
}

/**
 * Save the new or edited sub-process
 */
function saveSubProcessData() {
    const processName = document.getElementById("select-process").value;
    const subProcessName = document.getElementById("sub-process-name").value.trim();
    const isActive = document.getElementById("sub-process-is-active").checked;
    const operation = document.getElementById("sub-process-name").dataset.operation || "add";
    const subProcessId = document.getElementById("sub-process-name").dataset.subProcessId || null;

    if (!processName) {
        alert("Please select a process.");
        return;
    }

    const requestData = {
        operation: operation,
        active: isActive,
        process_name: processName,
        subprocess_name: subProcessName,
        subprocess_id: subProcessId
    };

    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/add_sub_process";

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(`Sub-Process ${operation === "add" ? "added" : "updated"} successfully!`);
            loadSubProcesses(); // Refresh the sub-process list
        } else {
            alert(data.message === "already_exist" ? "Sub-Process already exists!" : "Error saving sub-process.");
        }
    })
    .catch(error => console.error("Error saving sub-process:", error));

    closeSubProcessModal();
}
