<!-- New Sub-Process Modal -->
<div id="add-sub-process-modal">
    <div class="new-sheet-content">
        <!-- Header -->
        <div class="new-sheet-header" id="add-sub-process-title">Add Sub-Process</div>

        <!-- Body -->
        <div class="new-sheet-body">
            <!-- Select Process -->
            <div class="input-field">
                <label for="select-process">Process Name:</label>
                <select id="select-process">
                    <!-- Options will be dynamically populated -->
                </select>
            </div>
            
            <!-- Input Field for Sub-Process -->
            <div class="input-field">
                <label for="sub-process-name">Sub-Process Name:</label>
                <input type="text" id="sub-process-name" placeholder="Enter sub-process name" oninput="validateSubProcessInputs()">
            </div>

            <!-- Active Checkbox -->
            <div class="input-field">
                <label for="sub-process-is-active">Active:</label>
                <input type="checkbox" id="sub-process-is-active">
            </div>
        </div>

        <!-- Footer -->
        <div class="new-sheet-footer">
            <button class="save-btn" id="save-sub-process-btn" onclick="saveSubProcessData()" disabled>Save</button>
            <button class="cancel-btn" onclick="closeSubProcessModal()">Cancel</button>
        </div>
    </div>
</div>
