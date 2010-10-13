function submitPage(cmdString) {
	this.document.pageForm.cmd.value = cmdString;
	this.document.pageForm.submit();
}