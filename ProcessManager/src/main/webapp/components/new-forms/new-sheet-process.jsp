<!-- New Sheet Process Modal -->
<div id="add-sheet-process-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="add-sheet-process-title">Add Sheet Process</div>

        <!-- Body -->
        <div class="new-sheet-body">
            <!-- File Name (Read-only) -->
            <div class="input-field" hidden>
                <label for="sheet-file-name">File Name:</label>
                <input type="text" id="sheet-file-name" readonly>
            </div>

            <!-- Select Process -->
            <div class="input-field">
                <label for="select-sheet-process">Process Name:</label>
                <select id="select-sheet-process">
                    <!-- Options will be dynamically populated -->
                    <option value="">Select Process</option>
                </select>
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-sheet-process-btn" onclick="saveSheetProcessData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeSheetProcessModal()">Cancel</button>
        </div>
    </div>
</div>
