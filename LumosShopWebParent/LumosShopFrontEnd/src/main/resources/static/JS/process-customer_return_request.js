var TheModal;
var HeaderTitle;
var CommentField;
var orderID;
var Justifications;
var Message;
var submitBtn;

$(document).ready(function() {
    TheModal = $("#RequestRevertedModal");
    HeaderTitle = $("#ReturnOrderTitle");
    CommentField = $("#comment");
    Justifications = $("#Justifications");
    Message = $("#messageSection");
    submitBtn = $("#RevertedTheOrder");

    processOrderReverted();
});

function displayDialog(link) {
    Message.hide();
    Justifications.show();
    submitBtn.show();
    CommentField.val("");

    orderID = link.attr("orderID");
    HeaderTitle.text("Request Reverted Order ID #" + orderID);
    TheModal.modal("show");
}

function hideRequestContent(message) {
    Justifications.hide();
    submitBtn.hide();
    $("#CommentField").hide();
    Message.text(message);

    Message.show();
}

function processOrderReverted() {
    $(".request-return-btn").on("click", function(e) {
        e.preventDefault();
        displayDialog($(this));
    });
}

function submitRequestRevertedForm() {
    justification = $("#justification").val();
    comment = CommentField.val();

    console.log(justification +" - "+ comment);
    sendRequest(justification, comment);

    return false;
}

function sendRequest(justification, comment) {
    URL = contextPath + "order/reverted";
    Body = {orderID: orderID, justification: justification, comment: comment};

    $.ajax({
        type: "POST",
        url: URL,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(Body),
        contentType: 'application/json'

    }).done(function(returnResponse) {
        console.log(returnResponse);
        hideRequestContent("Your request has been successfully transmitted and is now on its way to the designated recipient for review and consideration.");
        changePhase(returnResponse.orderID);
    }).fail(function(reportErr) {
        console.log(reportErr);
        hideRequestContent(reportErr.responseText);
    });

}

function changePhase(orderId) {
    $(".OrderPhase" + orderId).each(function(index) {
        $(this).text("CUSTOMER_REQUESTED_RETURN");
    })

    $(".Request-returnOrder" + orderId).each(function(index) {
        $(this).hide();
    })
}