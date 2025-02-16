<!-- New Sheet Modal -->
<div id="new-sheet-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="new-sheet-title">Add Sheet</div>

        <!-- Body -->
        <div class="new-sheet-body">
            <div class="input-field">
                <label for="sheet-file-name">File Name:</label>
                <input type="text" id="sheet-file-name" placeholder="Enter file name" oninput="validateSheetInputs()">
            </div>
            <div class="input-field">
                <label for="sheet-version">Version:</label>
                <input type="text" id="sheet-version" placeholder="Enter version" oninput="validateSheetInputs()">
            </div>
            <div class="input-field">
                <label for="sheet-design-no">Design No:</label>
                <input type="text" id="sheet-design-no" placeholder="Enter design number" oninput="validateSheetInputs()">
            </div>
            <div class="input-field">
                <label for="sheet-department">Department:</label>
                <input type="text" id="sheet-department" placeholder="Enter department" oninput="validateSheetInputs()">
            </div>
            <div class="input-field">
                <label for="sheet-floor">Floor:</label>
                <input type="text" id="sheet-floor" placeholder="Enter floor" oninput="validateSheetInputs()">
            </div>
            <div class="input-field">
                <label for="sheet-last-updated-by">Last Updated By:</label>
                <input type="text" id="sheet-last-updated-by" placeholder="Enter last updated by" oninput="validateSheetInputs()">
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-sheet-btn" onclick="saveSheetData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeSheetModal()">Cancel</button>
        </div>
    </div>
</div>