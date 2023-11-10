
$(document).ready(function() {
    $("a[name='linkRemoveDetail']").each(function(index) {
        $(this).click(function() {
            removeDetailSectionByIndex(index);
        });
    });

});
function AddAnotherDetail() {

    allDivDetails = $("[id^='divDetail']"); /** first id="divDetail0" defined in product_details.html then id="divDetail1" id="divDetail2" ... */
    divDetailsCount = allDivDetails.length;

    let htmlDetailSection = `
    <div class="d-flex m-3" id="divDetail${divDetailsCount}">
        <input type="hidden" name="detailIDs" value="0" />
    
        <label class="m-3">Name:</label>
        <input type="text" class="w-25 form-control" name="detailNames" maxlength="255">
        <label class="m-3">Value:</label>
        <input type="text" class="w-25 form-control" name="detailValues" maxlength="255">
    </div>`;
    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailID = previousDivDetailSection.attr("id");



    let htmlLinkRemove = `<a class ="btn fa-duotone fa-times-circle text-dark fa-2x"
                                    title="remove this detail"
                                    href="javascript:removeDetailSectionById('${previousDivDetailID}')">
                                    </a>`

    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
    alert(id);

    $("#" + id).remove();
}
function removeDetailSectionByIndex(index) {
    $("#divDetail" + index).remove();
}