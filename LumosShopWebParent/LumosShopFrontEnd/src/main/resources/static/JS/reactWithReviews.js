

$(document).ready(function () {
    $(".act").on("click", function (f) {
        f.preventDefault();
        likeTheReview($(this));
    });
});



function likeTheReview(Ref) {

    URL = Ref.attr("href");
    $.ajax({
        type: "POST",
        url: URL,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        console.log(response);

        if (response.success) {
            improveIconAndLikes(response,Ref);
        }
        showToastMessage("React with Review", response.content);
    }).fail(function() {
        // showToastMessage("fail to react with the review.");
        displayingNormalModalERROR("Error in acting with review.")
    });
}

function boldLikeIcon(dislikeRef, likeRef) {

    likeRef.removeClass("fa-thin").addClass("fa-solid");
    dislikeRef.attr("title", "unlike review");
    dislikeRef.removeClass("fa-solid").addClass("fa-thin");

}



function improveIconAndLikes(response, Ref) {
    reviewID = Ref.attr("revID");
    $("#likes-review-" + reviewID).text(response.likes);
    likeRef = $("#likeReview" + reviewID);
    dislikeRef = $("#dislikeReview" + reviewID);



    if (response.content.includes("Well,You've Like")) {
        boldLikeIcon(dislikeRef, Ref);
    }else if (response.content.includes("Well,You've Dislike")) {
        boldDislikeIcon(likeRef, Ref);
    }else if (response.content.includes("You've withdrawn your Like")) {
        unBoldDislikeIcon(dislikeRef, Ref);
    }else if (response.content.includes("You've withdrawn your Dislike")) {
        unBoldLikeIcon(likeRef, Ref);

    }
}
function unBoldLikeIcon(likeRef, Ref) {
    Ref.removeClass("fa-solid").addClass("fa-thin");
}
function unBoldDislikeIcon(dislikeRef, Ref) {
    Ref.removeClass("fa-solid").addClass("fa-thin");
}
function boldDislikeIcon(likeRef, Ref) {

    Ref.removeClass("fa-thin").addClass("fa-solid");
    likeRef.attr("title", "dislike review");
    likeRef.removeClass("fa-solid").addClass("fa-thin");

}
function showToastMessage(headline,message) {
    $("#toastHeadline").text(headline);
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}