document.addEventListener("DOMContentLoaded", () => {
  // Initialize datepickers with dd-mm-yy format
  $("#from").datepicker({
    dateFormat: "dd-mm-yy",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function(selectedDate) {
      $("#to").datepicker("option", "minDate", selectedDate);
    }
  });
  $("#to").datepicker({
    dateFormat: "dd-mm-yy",
    changeMonth: true,
    numberOfMonths: 1,
    onClose: function(selectedDate) {
      $("#from").datepicker("option", "maxDate", selectedDate);
    }
  });

  // Set default dates: "From" = first day of current month, "To" = current date.
  const now = new Date();
  const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
  $("#from").datepicker("setDate", firstDay);
  $("#to").datepicker("setDate", now);

  // Clear the search input field
  document.getElementById("search-param").value = "";

  // Hide the filter panel initially.
  $('.filter-panel').hide();

  // Toggle filter panel when the filter-toggle button is clicked.
  $('.filter-toggle-btn').on('click', function(e) {
    e.stopPropagation();
    $('.filter-panel').toggle();
  });

  // Close filter panel when clicking outside.
  $(document).on('click', function(e) {
    if (!$(e.target).closest('.filter-panel, .filter-toggle-btn').length) {
      $('.filter-panel').hide();
    }
  });

  // Attach event listener to the filter panel close button.
  $('.close-filter-btn').on('click', function(e) {
    e.stopPropagation();
    $('.filter-panel').hide();
  });

  // Load sheets with default filter values.
  loadExistingSheets();
});

/**
 * Fetches all sheets from the API using filter parameters.
 */
function loadExistingSheets() {
  const fromDate = document.getElementById("from").value || "";
  const toDate = document.getElementById("to").value || "";
  const searchParam = document.getElementById("search-param").value || "";

  const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/showSheets";
  // Using keys "from_date" and "end_date"
  const jsonData = JSON.stringify({
    load_type: "all",
    from_date: fromDate,
    end_date: toDate,
    search_param: searchParam
  });

  const tableBody = document.getElementById("sheets-table-body-existing-sheet");
  tableBody.innerHTML = "<tr><td colspan='10'>Loading...</td></tr>";

  fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: jsonData,
  })
  .then(response => response.json())
  .then(data => {
    tableBody.innerHTML = "";
    if (data.success === true && data.sheets.length > 0) {
      populateSheetTable(data.sheets);
    } else {
      tableBody.innerHTML = "<tr><td colspan='10'>No sheets available.</td></tr>";
    }
  })
  .catch(error => {
    console.error("Error fetching sheets:", error);
    tableBody.innerHTML = `<tr><td colspan='10'>Error: ${error.message}</td></tr>`;
  });
}

/**
 * Populates the table with sheet data.
 */
function populateSheetTable(sheets) {
  const tableBody = document.getElementById("sheets-table-body-existing-sheet");
  tableBody.innerHTML = "";
  sheets.forEach(sheet => {
    const row = document.createElement("tr");
    // Store the sheet object in a data attribute.
    row.dataset.sheet = JSON.stringify(sheet);
    
    // Determine status text and class
    const statusText = (sheet.status === true || sheet.status === "true") ? "Completed" : "Pending";
    const statusClass = (sheet.status === true || sheet.status === "true") ? "status-completed" : "status-pending";

    row.innerHTML = `
      <td>${sheet.file_name}</td>
      <td>${sheet.design_no}</td>
      <td>${sheet.department}</td>
      <td>${sheet.floor}</td>
      <td>${sheet.date}</td>
      <td>${sheet.last_updated_by}</td>
      <td>${sheet.date_of_last_update}</td>
      <td>${sheet.version}</td>
      <td>
        <button class="status-btn ${statusClass}" title="Change Completion Status" onclick="setSheetStatus(this)">${statusText}</button>
      </td>
      <td>
          <span class="action-btn edit-btn" title="Edit Sheet" onclick="editSheet(this)">
              <font face="Arial">&#x270E;</font>
          </span>
          <span class="action-btn delete-btn" title="Delete Sheet" onclick="deleteSheet(this)">
              <font face="Arial">&#x1F5D1;</font>
          </span>
      </td>
    `;
	row.addEventListener("click", (event) => {
	  // If the click came from an action or status button, ignore it.
	  if (event.target.closest(".action-btn") || event.target.closest(".status-btn")) {
	    return;
	  }
	  showView('single-sheet-content');
	  loadSingleSheets(sheet.file_name);
	});
	tableBody.appendChild(row);
  });
}

/**
 * Toggles the status of a sheet.
 * When clicked, toggles between Completed and Pending.
 */
function setSheetStatus(element) {
  const row = element.closest("tr");
  const sheet = JSON.parse(row.dataset.sheet);
  // Toggle the status
  const newStatus = (sheet.status === true || sheet.status === "true") ? false : true;
/*  if (!confirm(`Set status of "${sheet.file_name}" to ${newStatus ? "Completed" : "Pending"}?`)) {
    return;
  }*/
  const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/setSheetStatus";
  const requestData = JSON.stringify({
    sheet_id: sheet.sheet_id,
    file_name: sheet.file_name,
    status: newStatus
  });
  fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: requestData,
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
     // alert("Status updated successfully!");
      loadExistingSheets();
    } else {
      alert(`Failed to update status: ${data.msg || "Unknown error"}`);
    }
  })
  .catch(error => {
    console.error("Error updating status:", error);
    alert(`Error: ${error.message}`);
  });
}

/**
 * Applies filters by reloading sheets and then closes the filter panel.
 */
function applyFilters() {
  loadExistingSheets();
  $('.filter-panel').hide();
}

/**
 * Clears filter fields, reloads sheets, and closes the filter panel.
 */
function clearFilters() {
  document.getElementById("from").value = "";
  document.getElementById("to").value = "";
  document.getElementById("search-param").value = "";
  loadExistingSheets();
  $('.filter-panel').hide();
}

/**
 * Handles deleting a sheet.
 */
function deleteSheet(element) {
  const sheet = JSON.parse(element.closest("tr").dataset.sheet);
  if (!confirm(`Are you sure you want to delete "${sheet.file_name}"?`)) return;
  const apiUrl = window.location.origin + "/ProcessManager/webapi/sheets/deleteSheet";
  const requestData = JSON.stringify({ sheet_id: sheet.sheet_id, file_name: sheet.file_name });
  fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: requestData,
  })
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      alert("Sheet deleted successfully!");
      loadExistingSheets();
    } else {
      alert(`Failed to delete sheet: ${data.msg || "Unknown error"}`);
    }
  })
  .catch(error => {
    console.error("Error deleting sheet:", error);
    alert(`Error: ${error.message}`);
  });
}

/**
 * Handles editing a sheet.
 */
function editSheet(element) {
  const sheet = JSON.parse(element.closest("tr").dataset.sheet);
  openEditSheetModal(sheet);
}

/**
 * Handles adding a new sheet.
 */
function addSheet() {
  openAddSheetModal();
}
