<!-- New Process Modal -->
<div id="add-process-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="add-process-title">Add Process</div>

        <!-- Body -->
        <div class="new-sheet-body" id="add-process-body">
            <!-- Input Fields for Process Data -->
            <div class="input-field">
                <label for="add-process-name">Process Name:</label>
                <input type="text" id="add-process-name" placeholder="Enter process name" oninput="validateProcessInputs()">
            </div>
            <div class="input-field">
                <label for="add-process-is-active">Active:</label>
                <input type="checkbox" id="add-process-is-active">
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-add-process-btn" onclick="saveProcessData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeAddProcessModal()">Cancel</button>
        </div>
    </div>
</div>
