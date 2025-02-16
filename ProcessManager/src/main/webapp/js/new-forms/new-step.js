// newStepModal.js

// Get modal and button elements
const addStepModal = document.getElementById("add-step-modal");
const saveStepButton = document.getElementById("save-step-btn");

/**
 * Validates that all required step fields are filled.
 * The following fields are mandatory: step-number, step-sub-process, step-tool (must not be "None"), 
 * step-tool-spec, step-special-instruction, step-skill (must not be "None"), and step-time.
 * The file input is not validated.
 */
function validateStepInputs() {
    const stepNumber = document.getElementById("step-number").value.trim();
    const subProcess = document.getElementById("step-sub-process").value.trim();
    const tool = document.getElementById("step-tool").value.trim();
    const toolSpec = document.getElementById("step-tool-spec").value.trim();
    const specialInstruction = document.getElementById("step-special-instruction").value.trim();
    const skill = document.getElementById("step-skill").value.trim();
    const timeMinutes = document.getElementById("step-time").value.trim();
    saveStepButton.disabled = (
        stepNumber === "" ||
        subProcess === "" ||
        tool === "" || toolSpec === "" ||
        specialInstruction === "" ||
        skill === "" || timeMinutes === ""
    );
}

/**
 * Listen for file selection and update the image preview.
 */
document.getElementById("step-file-upload").addEventListener("change", function () {
    const preview = document.getElementById("step-image-preview");
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = "block";
        };
        reader.readAsDataURL(file);
    } else {
        preview.src = "";
        preview.style.display = "none";
    }
});

// Attach change listener to sub-process dropdown to load tools.
document.getElementById("step-sub-process").addEventListener("change", function () {
    const selectedSubProcess = this.value;
    loadToolsForStepDropdown(selectedSubProcess);
    validateStepInputs();
});

/**
 * Opens the Step modal in "add" or "edit" mode.
 * @param {string} sheetProcessId - The sheet process ID.
 * @param {string} processName - The process name.
 * @param {string} fileName - The sheet file name.
 * @param {Object|null} stepData - Optional step data for editing.
 */
function openAddStepModal(sheetProcessId, processName, fileName, stepData = null) {
    // Clear any view mode flag and clear the file input.
    delete addStepModal.dataset.mode;
    document.getElementById("step-file-upload").value = "";
    
    // Clear the image preview.
    const preview = document.getElementById("step-image-preview");
    preview.src = "";
    preview.style.display = "none";
    
    // Ensure Save button is visible.
    saveStepButton.style.display = "";
	saveStepButton.disabled=false;
    
    // Populate read-only fields.
    document.getElementById("step-file-name").value = fileName;
    document.getElementById("step-process-name").value = processName;
    addStepModal.dataset.sheetProcessId = sheetProcessId;
    
    if (stepData) {
        // EDIT MODE:
        document.getElementById("step-number").value = stepData.step_number || "";
        document.getElementById("step-tool-spec").value = stepData.tool_spec || "";
        document.getElementById("step-special-instruction").value = stepData.special_instruction || "";
        document.getElementById("step-time").value = stepData.time_minutes || "";
        
        // For sub-process: load options and preselect current value.
        const subProcessValue = stepData.subprocess_name || "";
        loadSubProcessesForStepDropdown(subProcessValue, function () {
            const subProcessDropdown = document.getElementById("step-sub-process");
            setSelectedOption(subProcessDropdown, subProcessValue);
            subProcessDropdown.disabled = false;
        });
        
        // For tool: load options based on sub-process and preselect current tool.
        const preTool = (stepData.tool_name === "-" ? "None" : stepData.tool_name) || "";
        loadToolsForStepDropdown(subProcessValue, preTool, function () {
            const toolDropdown = document.getElementById("step-tool");
            setSelectedOption(toolDropdown, preTool);
            toolDropdown.disabled = false;
        });
        
        // For skill: load and preselect.
        const preSkill = (stepData.skill === "-" ? "None" : stepData.skill) || "";
        loadSkillsForStepDropdown(preSkill);
        
        // Set the image preview if an image URL is provided.
        if (stepData.image_url && stepData.image_url.trim() !== "") {
            preview.src = stepData.image_url;
            preview.style.display = "block";
            // Store the existing image URL for update if no new file is selected.
            addStepModal.dataset.imageUrl = stepData.image_url;
        } else {
            preview.src = "";
            preview.style.display = "none";
            addStepModal.dataset.imageUrl = "";
        }
        
        addStepModal.dataset.operation = "update";
        addStepModal.dataset.stepId = stepData.step_id;
        document.getElementById("add-step-title").textContent = "Edit Step";
    } else {
        // NEW MODE:
        document.getElementById("step-number").value = "";
        document.getElementById("step-tool-spec").value = "";
        document.getElementById("step-special-instruction").value = "";
        document.getElementById("step-time").value = "";
        
        const subProcessDropdown = document.getElementById("step-sub-process");
        subProcessDropdown.disabled = false;
        subProcessDropdown.value = "";
        loadSubProcessesForStepDropdown("", function () {
            document.getElementById("step-tool").innerHTML = `<option value="None">None</option>`;
        });
        
        const toolDropdown = document.getElementById("step-tool");
        toolDropdown.disabled = false;
        toolDropdown.innerHTML = `<option value="None">None</option>`;
        
        loadSkillsForStepDropdown();
        
        addStepModal.dataset.operation = "add";
        delete addStepModal.dataset.stepId;
        document.getElementById("add-step-title").textContent = "Add Step";
        
        // Clear file input and preview.
        document.getElementById("step-file-upload").value = "";
        preview.src = "";
        preview.style.display = "none";
        delete addStepModal.dataset.imageUrl;
    }
    
    validateStepInputs();
    addStepModal.classList.add("show");
    document.querySelector('.new-step-content').scrollTop = 0;
}

/**
 * Opens the Step modal in "view" mode.
 */
function openViewStepModal(sheetProcessId, processName, fileName, stepData) {
    openAddStepModal(sheetProcessId, processName, fileName, stepData);
    addStepModal.dataset.mode = "view";
    saveStepButton.style.display = "none";
    document.getElementById("step-number").readOnly = true;
    document.getElementById("step-tool-spec").readOnly = true;
    document.getElementById("step-special-instruction").readOnly = true;
    document.getElementById("step-time").readOnly = true;
    document.getElementById("step-sub-process").disabled = true;
    document.getElementById("step-tool").disabled = true;
    document.getElementById("step-skill").disabled = true;
    document.getElementById("step-file-upload").disabled = true;
    document.getElementById("add-step-title").textContent = "View Step";
}

/**
 * Closes the Step modal and resets file input/preview.
 */
function closeStepModal() {
    addStepModal.classList.remove("show");
    saveStepButton.style.display = "";
    document.getElementById("step-number").readOnly = false;
    document.getElementById("step-tool-spec").readOnly = false;
    document.getElementById("step-special-instruction").readOnly = false;
    document.getElementById("step-time").readOnly = false;
    document.getElementById("step-sub-process").disabled = false;
    document.getElementById("step-tool").disabled = false;
    document.getElementById("step-skill").disabled = false;
    document.getElementById("step-file-upload").disabled = false;
    // Clear file input and image preview.
    document.getElementById("step-file-upload").value = "";
    const preview = document.getElementById("step-image-preview");
    preview.src = "";
    preview.style.display = "none";
    delete addStepModal.dataset.imageUrl;
}

/**
 * Loads sub-processes into the step modal's dropdown using the process name.
 */
function loadSubProcessesForStepDropdown(selectedSubProcess = "", callback) {
    const processName = document.getElementById("step-process-name").value;
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_sub_process";
    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ load_type: "process_related", process: processName })
    })
    .then(response => response.json())
    .then(data => {
        const subProcessDropdown = document.getElementById("step-sub-process");
        subProcessDropdown.innerHTML = `<option value="">Select Sub-Process</option>`;
        if (data.success && data.sub_process.length > 0) {
            data.sub_process.forEach(subProcess => {
                const value = subProcess.subprocess_name || subProcess.sub_process || "";
                const option = document.createElement("option");
                option.value = value;
                option.textContent = value;
                subProcessDropdown.appendChild(option);
            });
            if (selectedSubProcess) {
                setSelectedOption(subProcessDropdown, selectedSubProcess);
            }
        }
        if (callback && typeof callback === "function") {
            callback();
        }
    })
    .catch(error => console.error("Error loading sub-processes:", error));
}

/**
 * Loads tools based on the selected sub-process.
 */
function loadToolsForStepDropdown(subProcess, selectedToolName = "", callback) {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_tools";
    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ load_type: "sub_process_related", sub_process: subProcess })
    })
    .then(response => response.json())
    .then(data => {
        const toolDropdown = document.getElementById("step-tool");
        toolDropdown.innerHTML = `<option value="None">None</option>`;
        if (data.success && data.tools && data.tools.length > 0) {
            data.tools.forEach(tool => {
                const option = document.createElement("option");
                option.value = tool.tool_name;
                option.textContent = tool.tool_name;
                toolDropdown.appendChild(option);
            });
            if (selectedToolName) {
                setSelectedOption(toolDropdown, selectedToolName);
            }
        }
        if (callback && typeof callback === "function") {
            callback();
        }
    })
    .catch(error => console.error("Error loading tools:", error));
}

/**
 * Loads skills into the step modal's dropdown.
 */
function loadSkillsForStepDropdown(selectedSkill = "") {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_skills";
    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ load_type: "active" })
    })
    .then(response => response.json())
    .then(data => {
        const skillDropdown = document.getElementById("step-skill");
        skillDropdown.innerHTML = `<option value="None">None</option>`;
        if (data.success && data.skills && data.skills.length > 0) {
            data.skills.forEach(skill => {
                const option = document.createElement("option");
                option.value = skill.skill_name;
                option.textContent = skill.skill_name;
                skillDropdown.appendChild(option);
            });
            if (selectedSkill) {
                setSelectedOption(skillDropdown, selectedSkill);
            }
        }
    })
    .catch(error => console.error("Error loading skills:", error));
}

/**
 * Helper function to set the selected option in a dropdown.
 */
function setSelectedOption(dropdown, valueToSelect) {
    let found = false;
    for (let i = 0; i < dropdown.options.length; i++) {
        if (dropdown.options[i].value.toLowerCase() === valueToSelect.toLowerCase()) {
            dropdown.selectedIndex = i;
            found = true;
            break;
        }
    }
    if (!found && valueToSelect) {
        const option = document.createElement("option");
        option.value = valueToSelect;
        option.textContent = valueToSelect;
        dropdown.appendChild(option);
        dropdown.selectedIndex = dropdown.options.length - 1;
    }
}

// Also attach a change listener for the skill dropdown.
document.getElementById("step-skill").addEventListener("change", validateStepInputs);

/**
 * Helper function to get the file name from the sheet details table.
 */
function getSheetFileName() {
    const row = document.querySelector("#sheets-table-body-single-sheet tr td:first-child");
    return row ? row.textContent.trim() : "";
}

/**
 * Saves step data by calling the API.
 * Uses FormData to support file upload.
 */
function saveStepData() {
	saveStepButton.disabled
    const formData = new FormData();
    formData.append("operation", addStepModal.dataset.operation || "add");
    formData.append("file_name", document.getElementById("step-file-name").value);
    formData.append("process_name", document.getElementById("step-process-name").value);
    formData.append("subprocess_name", document.getElementById("step-sub-process").value);
    formData.append("step_number", document.getElementById("step-number").value.trim());
    const toolVal = document.getElementById("step-tool").value;
    const skillVal = document.getElementById("step-skill").value;
    formData.append("tool_name", (toolVal === "None" ? "-" : toolVal));
    formData.append("tool_spec", document.getElementById("step-tool-spec").value.trim());
    formData.append("special_instruction", document.getElementById("step-special-instruction").value.trim());
    formData.append("skill", (skillVal === "None" ? "-" : skillVal));
    formData.append("time_minutes", document.getElementById("step-time").value.trim());
    formData.append("sheet_process_id", addStepModal.dataset.sheetProcessId);
    if (addStepModal.dataset.operation === "update" && addStepModal.dataset.stepId) {
        formData.append("step_id", addStepModal.dataset.stepId);
    }
    
    // Append file if selected; if not, in update mode, send the stored image URL.
    const fileInput = document.getElementById("step-file-upload");
    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
    } else {
        if (addStepModal.dataset.operation === "update" && addStepModal.dataset.imageUrl) {
            formData.append("image_url", addStepModal.dataset.imageUrl);
        }
    }
    
    const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/addStep";
    fetch(apiUrl, {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(`Step ${addStepModal.dataset.operation === "add" ? "added" : "updated"} successfully!`);
            const fileName = getSheetFileName();
            closeStepModal();
            loadSingleSheets(fileName);
        } else {
            if (data.msg === "step_id_not_exist") {
                alert("Step does not exist!");
            } else if (data.msg === "file_too_large") {
                alert("Selected File Size should not exceed 5MB");
            } else {
                alert("Error saving step.");
            }
        }
    })
    .catch(error => {
        console.error("Error saving step:", error);
        alert("An error occurred while saving the step.");
    });
}
