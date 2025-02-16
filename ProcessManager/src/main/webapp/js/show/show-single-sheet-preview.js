// Select the modal
const showSheetPreviewModal1 = document.getElementById("show-single-sheet-preview-modal");

// Function to show the modal with process details
function showSheetPreviewModal(sheetData) {
    if (!sheetData || !sheetData.sheets || sheetData.sheets.length === 0) {
        alert("No sheet data available.");
        return;
    }

    // Extract sheet title
    const sheet = sheetData.sheets[0];
    document.getElementById("preview-sheet-title").textContent = `Sheet: ${sheet.file_name}`;

    // Populate process list
    const processListContainer = document.getElementById("process-list");
    processListContainer.innerHTML = ""; // Clear previous data

    if (sheetData.sheet_process && sheetData.sheet_process.length > 0) {
        sheetData.sheet_process.forEach((process) => {
            let totalTime = process.steps.reduce((sum, step) => sum + step.time_minutes, 0);

            // Process section
            const processSection = document.createElement("div");
            processSection.classList.add("process-section");
            processSection.innerHTML = `
                <h4>${process.process_name} (Total Min: ${totalTime})</h4>
                <table class="step-table">
                    <thead>
                        <tr>
                            <th>Step</th>
                            <th>Sub Process</th>
                            <th>Tool</th>
                            <th>Tool Spec</th>
                            <th>Special Instruction</th>
                            <th>Skill (SK)</th>
                            <th>Time (min)</th>
                            <th>Image</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${process.steps.length > 0 
                            ? process.steps.map(step => `
                                <tr>
                                    <td>${step.step_number}</td>
                                    <td>${step.subprocess_name}</td>
                                    <td>${step.tool_name}</td>
                                    <td>${step.tool_spec}</td>
                                    <td class="instruction-cell">${step.special_instruction}</td>
                                    <td>${step.skill}</td>
                                    <td>${step.time_minutes}</td>
                                    <td>
                                        ${step.image_url ? `<img src="${step.image_url}" class="step-image">` : "No Image"}
                                    </td>
                                </tr>
                            `).join("")
                            : `<tr><td colspan="8">No steps available.</td></tr>`
                        }
                    </tbody>
                </table>
            `;
            processListContainer.appendChild(processSection);
        });
    } else {
        processListContainer.innerHTML = "<p>No processes available for this sheet.</p>";
    }

    // Show the modal
    showSheetPreviewModal1.classList.add("show");
}

// Function to close the modal
function closeShowSheetPreviewModal() {
    showSheetPreviewModal1.classList.remove("show");
}
