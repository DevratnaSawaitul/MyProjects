<!-- New Skills Modal -->
<div id="add-skill-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="add-skill-title">Add Skill</div>

        <!-- Body -->
        <div class="new-sheet-body" id="add-skill-body">
            <!-- Input Fields for Skill Data -->
            <div class="input-field">
                <label for="add-skill-name">Skill Name:</label>
                <input type="text" id="add-skill-name" placeholder="Enter skill name" oninput="validateSkillInputs()">
            </div>
            <div class="input-field">
                <label for="add-skill-is-active">Active:</label>
                <input type="checkbox" id="add-skill-is-active">
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-add-skill-btn" onclick="saveSkillData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeAddSkillModal()">Cancel</button>
        </div>
    </div>
</div>
