// Select modal and related elements for new skills
const addSkillModal = document.getElementById("add-skill-modal");
const saveSkillButton = document.getElementById("save-add-skill-btn");

// Validate skill inputs
function validateSkillInputs() {
    const skillName = document.getElementById("add-skill-name").value.trim();
    saveSkillButton.disabled = skillName === ""; // Disable if skill name is empty
}

// Open the Add Skill modal (no skill data)
function openAddSkillModal() {
    const skillNameField = document.getElementById("add-skill-name");
    skillNameField.value = "";
    document.getElementById("add-skill-is-active").checked = false;
    skillNameField.removeAttribute("readonly");  // Remove readonly for adding new skill
    skillNameField.dataset.operation = "add";  // Set operation to add
    saveSkillButton.disabled = false;
    document.getElementById("add-skill-title").textContent = "Add Skill"; // Set title for Add
    addSkillModal.classList.add("show");
}

// Open the Edit Skill modal (with existing skill data)
function openEditSkillModal(skill) {
    document.getElementById("add-skill-name").value = skill.skill_name;
    document.getElementById("add-skill-is-active").checked = skill.active;

    // Make skill_name readonly and set the operation to "update"
    const skillNameField = document.getElementById("add-skill-name");
    skillNameField.setAttribute("readonly", "true");  // Ensure it's readonly

    // Store operation and skill ID in the dataset
    skillNameField.dataset.operation = "update";
    skillNameField.dataset.skillId = skill.skill_id; // Store skill ID for update

    saveSkillButton.disabled = false;
    document.getElementById("add-skill-title").textContent = "Edit Skill"; // Set title for Edit
    addSkillModal.classList.add("show");
}

// Close the Add/Edit Skill modal
function closeAddSkillModal() {
    addSkillModal.classList.remove("show");
}

// Save the new or edited skill
function saveSkillData() {
    const skillName = document.getElementById("add-skill-name").value.trim();
    const isActive = document.getElementById("add-skill-is-active").checked;
    const operation = document.getElementById("add-skill-name").dataset.operation || "add"; // Default operation is "add"
    const skillId = document.getElementById("add-skill-name").dataset.skillId || null; // Skill ID for update

    // Prepare the request payload
    const requestData = {
        operation: operation,
        skill_name: skillName,
        active: isActive,
        skill_id: skillId // Include skill ID for update operation
    };

    // Set the URL dynamically in case it changes
    const apiUrl = window.location.origin + "/ProcessManager/webapi/org/add_skills"; // Update this URL if necessary

    // Make the API call to add or update the skill
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
            // Skill added or updated successfully
            alert(`Skill ${operation === "add" ? "added" : "updated"} successfully!`);
            loadSkills(); // Refresh the skills list
        } else {
            // Handle failure based on the response message
            if (data.message === "already_exist") {
                alert("Skill already exists!");
            } else if (data.message === "skill_name_not_exist") {
                alert("Skill name does not exist!");
            }
        }
    })
    .catch(error => {
        console.error("Error saving skill data:", error);
        alert("An error occurred while saving the skill.");
    });

    closeAddSkillModal(); // Close the modal after saving
}
