

$(document).ready(function () {
    $(".like").on("click", function (f) {
        f.preventDefault();
        likeTheReview($(this));
    });
});
function likeTheReview(link) {

    URL = link.attr("href");
    $.ajax({
        type: "POST",
        url: URL,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(voteResult) {
        console.log(voteResult);
        showToastMessage("You've successfully liked the review");
    }).fail(function() {
        showToastMessage("fail to react with the review.");
    });
}



function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}