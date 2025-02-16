/**
 * Function to show a specific section and hide others in the main content area.
 * Also triggers specific actions like API calls when necessary.
 * @param {string} viewId - The ID of the section to show (e.g., "existing-sheets-content").
 */
function showView(viewId) {
	// IDs of all sections in main-content
	const sections = ["default-content", "existing-sheets-content", "single-sheet-content", "show-process", "show-sub-process", "show-tools", "show-skills"];

	// Loop through sections and toggle visibility
	sections.forEach((id) => {
		const section = document.getElementById(id);
		if (section) {
			section.style.display = id === viewId ? "block" : "none";
		}
	});

	// Perform specific actions when switching views
	if (viewId === "existing-sheets-content") {
		loadExistingSheets(); // Trigger the API call to fetch sheets data
	} else if (viewId === "show-process") {
		loadProcesses();
	} else if (viewId === "show-sub-process") {
		loadSubProcesses();
	} else if (viewId === "show-tools") {
		loadTools();
	} else if (viewId === "show-skills") {
		loadSkills();
	}
}

// Example: Show Existing Sheets view when the page loads
document.addEventListener("DOMContentLoaded", () => {
	// Show the default view initially
	showView("existing-sheets-content");
});
