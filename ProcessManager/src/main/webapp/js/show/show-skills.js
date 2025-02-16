document.addEventListener("DOMContentLoaded", () => {
    loadSkills();
});

/**
 * Fetches all skills from the API and displays them in the table.
 */
function loadSkills() {
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/show_skills";
    const jsonData = JSON.stringify({ load_type: "all" });

    const tableBody = document.getElementById("skills-table-body");
    tableBody.innerHTML = "<tr><td colspan='3'>Loading...</td></tr>";

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: jsonData
    })
    .then(response => response.json())
    .then(data => {
        tableBody.innerHTML = ""; // Clear table before inserting new rows

        if (data.success  && data.skills.length > 0) {
            populateSkillsTable(data.skills);
        } else {
            tableBody.innerHTML = "<tr><td colspan='3'>No skills available.</td></tr>";
        }
    })
    .catch(error => {
        console.error("Error fetching skills data:", error);
        tableBody.innerHTML = `<tr><td colspan='3'>Error: ${error.message}</td></tr>`;
    });
}

/**
 * Populates the table with the fetched skills data.
 * @param {Array} skills - Array of skill objects returned from the API.
 */
function populateSkillsTable(skills) {
    const tableBody = document.getElementById("skills-table-body");

    skills.forEach(skill => {
        const row = document.createElement("tr");
        row.dataset.skill = JSON.stringify(skill); // Store skill object in dataset

        row.innerHTML = `
            <td>${skill.skill_name}</td>
            <td>${skill.active ? "Yes" : "No"}</td>
            <td>
                <span class="action-btn edit-btn" title="Edit Skill" onclick="editSkill(this)">
                    <font face="Arial">&#x270E;</font> <!-- ✎ Edit Icon -->
                </span>
                <span class="action-btn delete-btn" title="Delete Skill" onclick="deleteSkill(this)">
                    <font face="Arial">&#x1F5D1;</font> <!-- 🗑 Delete Icon -->
                </span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function editSkill(element) {
    const skills = JSON.parse(element.closest("tr").dataset.skill);
    openEditSkillModal(skills); // Open modal for editing with process data
}

/**
 * Handles adding a process.
 */
function addSkill() {
    openAddSkillModal(); // Open modal for adding a new process
}

/**
 * Handles deleting a skill.
 */
function deleteSkill(element) {
    const skill = JSON.parse(element.closest("tr").dataset.skill);

    if (!confirm(`Are you sure you want to delete "${skill.skill_name}"?`)) {
        return; // User canceled the deletion
    }

    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/delete_skills";
    const requestData = JSON.stringify({ skill_id: skill.skill_id });

    fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: requestData,
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Skill deleted successfully!");
            loadSkills();
        } else {
            alert(`Failed to delete skill: ${data.msg || "Unknown error"}`);
        }
    })
    .catch(error => {
        console.error("Error deleting skill:", error);
        alert(`Error: ${error.message}`);
    });
}

