var totalStamps;

$(document).ready(function() {

    totalStamps = $(".followUpID").length;

    $("#FollowUpList").on("click", ".linkRemoveStamp", function(e) {
        e.preventDefault();
        removingStamp($(this));
        updateNumberOfStamps();
    });

    $("#FollowUp").on("click", "#AddStamp", function(e) {
        e.preventDefault();
        CreateNewStamp();
    });

    $("#FollowUpList").on("change", ".phases", function(e) {
        phasesList = $(this);
        rowNumber = phasesList.attr("rowNumber");
        normalRemark = $("option:selected", phasesList).attr("Preset").trim();
        $("#stampRemark" + rowNumber).html(normalRemark);
    });
});

function removingStamp(link) {
    rowNumber = link.attr('rowNumber');
    $("#Rstamp" + rowNumber).remove();
    $("#Gap" + rowNumber).remove();
}

function updateNumberOfStamps() {
    $(".stampPart").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function CreateNewStamp() {
    htmlCode = generateTrackCode();
    $("#FollowUpList").append(htmlCode);
}

function generateTrackCode() {
    Coming = totalStamps + 1;
    totalStamps++;
    rowId = "Rstamp" + Coming;
    GapId = "Gap" + Coming;
    stampRemarkId = "stampRemark" + Coming;
    currentDateTime = formatCurrentDateTime();

    htmlCode = `
			<div class="row border rounded p-1" id="${rowId}">
				<input hidden name="stampId" value="0" class="followUpID" />
				<div class="col-2">
					<div class="stampPart">${Coming}</div>
					<div class="mt-1"><a class="fas fa-trash icon-dark linkRemoveStamp" href="" rowNumber="${Coming}"></a></div>					
				</div>				
				
				<div class="col-10">
				  <div class="form-group row">
				    <label class="col-form-label">TimeStamp:</label>
				    <div class="col">
						<input type="datetime-local" name="TimeStamp" value="${currentDateTime}" class="form-control" required/>						
				    </div>
				  </div>					
				<div class="form-group row">  
				<label class="col-form-label">order phase</label>
				<div class="col">
					<select name="stampPhase" class="form-control phases" required rowNumber="${Coming}">
			`;

    htmlCode += $("#Choices").clone().html();

    htmlCode += `
				      </select>						
				    </div>
				  </div>
				  <div class="form-group row">
				    <label class="col-form-label">Notes:</label>
				    <div class="col">
						<textarea class="form-control" name="stampRemark" id="${stampRemarkId}" required></textarea>
				    </div>
				  </div>
				  
				</div>				
			</div>	
			<div id="${GapId}" class="row">&nbsp;</div>
	`;

    return htmlCode;
}

function formatCurrentDateTime() {
    const date = new Date();

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    const second = date.getSeconds().toString().padStart(2, '0');

    return `${year}-${month}-${day}T${hour}:${minute}:${second}`;
}
