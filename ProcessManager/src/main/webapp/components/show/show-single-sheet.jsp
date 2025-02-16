<!-- Sheet Header with Back, Preview, and Delete Sheet Icons -->
<div class="single-sheet-header">
	<span class="back-btn" title="Back"
		onclick="showView('existing-sheets-content')">&#8592;</span>
	<h2>Sheet Details</h2>
	<!-- Preview icon -->
	<span class="preview-sheet-icon" title="Preview"
		onclick="showSheetPreview()"> <font face="Arial">&#128065;</font>
	</span> <span class="download-sheet-icon" title="Download Sheet"
		onclick="downloadSheet()"> <font face="Arial">&#128462;</font>
	</span>
	<!-- Delete icon -->
	<span class="delete-sheet-icon" title="Delete Sheet"
		onclick="deleteSingleSheet()"> <font face="Arial">&#x1F5D1;</font>
	</span>
</div>

<!-- Sheet Details Table -->
<div class="table-container-single-sheet">
	<table class="sheets-table-single-sheet">
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
			</tr>
		</thead>
		<tbody id="sheets-table-body-single-sheet">
			<!-- Dynamically inserted rows; each <tr> should include data attributes for sheet_id and file_name -->
		</tbody>
	</table>
</div>

<!-- Sheet Process Section (Always Visible) -->
<div id="sheet-process-section" class="sheet-process-section">
	<div class="sheet-process-header">
		<h3>Processes</h3>
		<button class="add-btn right-btn" title="Add New Process in the Sheet"
			onclick="addProcessToSheet()">+ Add Process</button>
	</div>

	<div id="sheet-process-container">
		<!-- Dynamically inserted processes -->
	</div>
</div>
