<!-- New Tools Modal -->
<div id="add-tools-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="add-tools-title">Add Tool</div>

        <!-- Body -->
        <div class="new-sheet-body">
            <!-- Select Sub-Process -->
            <div class="input-field">
                <label for="select-sub-process">Sub-Process Name:</label>
                <select id="select-sub-process">
                    <!-- Options will be dynamically populated -->
                </select>
            </div>
            
            <!-- Input Field for Tool Name -->
            <div class="input-field">
                <label for="tool-name">Tool Name:</label>
                <input type="text" id="tool-name" placeholder="Enter tool name" oninput="validateToolInputs()">
            </div>

            <!-- Active Checkbox -->
            <div class="input-field">
                <label for="tool-is-active">Active:</label>
                <input type="checkbox" id="tool-is-active">
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-tools-btn" onclick="saveToolData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeToolsModal()">Cancel</button>
        </div>
    </div>
</div>
