var hour = 0;
var mins = 0;
var secs = 0;

function initializeExamTimer(remainingTimeInSeconds) {
	hour = Math.floor(Math.max(remainingTimeInSeconds/(60*60), 0));
	var secsLeft = remainingTimeInSeconds % (60*60);
	mins = Math.floor(Math.max(secsLeft/60, 0));
	secsLeft = secsLeft % 60;
	secs = secsLeft;
	displayRemainingTime();
	setTimeout("updateRemainingTime()", 1000);
}

function updateRemainingTime() {
	var examFinished = false;
	if (secs > 0) secs--;
	else {
		if (mins > 0) {
			secs = 59;
			mins--;
		}
		else {
			if (hour > 0) {
				secs = 59;
				mins = 59;
				hour--;
			}
			else {
				hour = min = sec = 0;
				examTimedOut();
				examFinished = true;
			}
		}
	}
	displayRemainingTime();
	if (!examFinished) setTimeout("updateRemainingTime()", 1000);
}

function displayRemainingTime() {
	var hourStr = hour < 10? "0" + hour: hour;
	var minsStr = mins < 10? "0" + mins: mins;
	var secsStr = secs < 10? "0" + secs: secs;
	var time = hourStr + ":" + minsStr + ":" + secsStr;
	var remainingTimeSpan = document.getElementById('remainingTime');
	remainingTimeSpan.innerHTML = time;
}

function examTimedOut() {
	this.document.examForm.submit();
}