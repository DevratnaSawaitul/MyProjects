<!-- Existing Sheets View -->
<div class="main-content-existing-sheet" id="main-content-existing-sheet">
  <div class="sheet-header">
    <h2>Sheets</h2>
    <div class="header-actions">
      <button class="add-sheet-btn" title="Add New Sheet" onclick="addSheet()">+ Add Sheet</button>
      <!-- Filter toggle icon positioned immediately to the right of the Add Sheet button -->
      <button class="filter-toggle-btn" title="Toggle Filter">&#128269;</button>
      <!-- Filter Panel -->
      <div class="filter-panel">
        <div class="filter-row">
          <div class="date-inputs">
            <label for="from">From:</label>
            <input type="text" id="from" name="from">
            <label for="to">To:</label>
            <input type="text" id="to" name="to">
          </div>
          <div class="search-input">
            <input type="text" id="search-param" placeholder="Search...">
          </div>
          <div class="filter-buttons">
            <button class="search-btn" title="Apply filters" onclick="applyFilters()">Apply</button>
            <button class="clear-filters-btn" title="Clear all filters" onclick="clearFilters()">Clear</button>
            <button class="close-filter-btn" title="Close filter" onclick="closeFilterPanel()">Close</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="table-container-existing-sheet">
    <table class="sheets-table-existing-sheet">
      <thead>
        <tr>
          <th>File Name</th>
          <th>Design No</th>
          <th>Department</th>
          <th>Floor</th>
          <th>Date Created</th>
          <th>Last Updated By</th>
          <th>Last Updated On</th>
          <th>Version</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody id="sheets-table-body-existing-sheet">
        <!-- Data rows will be dynamically inserted here -->
      </tbody>
    </table>
  </div>
</div>

<!-- Include jQuery and jQuery UI for datepicker functionality -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.11.4/themes/ui-lightness/jquery-ui.css">
