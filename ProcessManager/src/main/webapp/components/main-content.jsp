<div class="main-content">
	<%-- Default content --%>
	<!-- div id="default-content">
        <h2>Process Management System</h2>
        <p>Select an option from the sidebar to view details.</p>
    </div-->

	<%-- Existing Sheets View --%>
	<div id="existing-sheets-content" style="display: none;">
		<%@ include file="/components/show/show-existing-sheet.jsp"%>
	</div>

	<%-- Single Sheet View --%>
	<div id="single-sheet-content" style="display: none;">
		<%@ include file="/components/show/show-single-sheet.jsp"%>
	</div>

	<%-- Show Process --%>
	<div id="show-process" style="display: none;">
		<%@ include file="/components/show/show-process.jsp"%>
	</div>

	<%-- Show Sub Process --%>
	<div id="show-sub-process" style="display: none;">
		<%@ include file="/components/show/show-sub-process.jsp"%>
	</div>

	<%-- Show Tools --%>
	<div id="show-tools" style="display: none;">
		<%@ include file="/components/show/show-tools.jsp"%>
	</div>

	<%-- Show Skills --%>
	<div id="show-skills" style="display: none;">
		<%@ include file="/components/show/show-skills.jsp"%>
	</div>
</div>
