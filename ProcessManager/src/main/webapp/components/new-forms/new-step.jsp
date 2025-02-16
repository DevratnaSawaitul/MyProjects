<!-- New Step Modal -->
<div id="add-step-modal">
  <div class="new-step-content">
    <!-- Header -->
    <div class="new-step-header" id="add-step-title">Add Step</div>
    <!-- Body -->
    <div class="new-step-body">
      <!-- File Name (Read-only) -->
      <div class="input-field">
        <label for="step-file-name">File Name:</label>
        <input type="text" id="step-file-name" readonly>
      </div>
      <!-- Process Name (Read-only) -->
      <div class="input-field">
        <label for="step-process-name">Process Name:</label>
        <input type="text" id="step-process-name" readonly>
      </div>
      <!-- Step Number -->
      <div class="input-field">
        <label for="step-number">Step Number:</label>
        <input type="number" id="step-number" placeholder="Enter step number" oninput="validateStepInputs()">
      </div>
      <!-- Sub-Process Dropdown -->
      <div class="input-field">
        <label for="step-sub-process">Sub-Process:</label>
        <select id="step-sub-process" onchange="validateStepInputs()">
          <option value="">Select Sub-Process</option>
        </select>
      </div>
      <!-- Tool Dropdown -->
      <div class="input-field">
        <label for="step-tool">Tool:</label>
        <select id="step-tool" onchange="validateStepInputs()">
          <option value="None">None</option>
        </select>
      </div>
      <!-- Tool Spec -->
      <div class="input-field">
        <label for="step-tool-spec">Tool Spec:</label>
        <input type="text" id="step-tool-spec" placeholder="Enter tool specification" oninput="validateStepInputs()">
      </div>
      <!-- Special Instruction -->
      <div class="input-field">
        <label for="step-special-instruction">Special Instruction:</label>
        <textarea id="step-special-instruction" placeholder="Enter special instruction" oninput="validateStepInputs()"></textarea>
      </div>
      <!-- Skill Dropdown -->
      <div class="input-field">
        <label for="step-skill">Skill:</label>
        <select id="step-skill" onchange="validateStepInputs()">
          <option value="None">None</option>
        </select>
      </div>
      <!-- Time Minutes -->
      <div class="input-field">
        <label for="step-time">Time (Minutes):</label>
        <input type="number" id="step-time" placeholder="Enter time in minutes" oninput="validateStepInputs()">
      </div>
      <!-- File Upload Field (Optional) -->
      <div class="input-field">
        <label for="step-file-upload">Upload Image:</label>
        <input type="file" id="step-file-upload" accept="image/jpeg, image/jpg, image/png" oninput="validateStepInputs()">
      </div>
      <!-- Image Preview -->
      <div class="input-field">
        <label for="step-image-preview">Image Preview:</label>
        <img id="step-image-preview" src="" alt="Image Preview" style="display:none;">
      </div>
    </div>
    <!-- Footer -->
    <div class="new-step-footer">
      <button class="save-btn" id="save-step-btn" onclick="saveStepData()">Save</button>
      <button class="cancel-btn" onclick="closeStepModal()">Cancel</button>
    </div>
  </div>
</div>
