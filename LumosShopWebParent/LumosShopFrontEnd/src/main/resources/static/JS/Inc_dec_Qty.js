$(document).ready(function () {
    // Find the input element and plus/minus buttons
    const quantityInput = $(".quantityInput");
    const plusBtn = $(".plusBtn");
    const minusBtn = $(".minusBtn");

    plusBtn.click(function () {
        let currentQuantity = parseInt(quantityInput.val());
        if (currentQuantity < 6) {
            quantityInput.val(currentQuantity + 1);
        } else {
            showModalDialog("Alert <i class='fa-duotone fa-message-exclamation'></i> ", "You can't take more than 6 quantity.");
        }
    });

    minusBtn.click(function () {
        let currentQuantity = parseInt(quantityInput.val());
        if (currentQuantity > 1) {
            quantityInput.val(currentQuantity - 1);
        } else {
            showModalDialog("Alert <i class='fa-duotone fa-message-exclamation'></i> ", "The minimum quantity is 1.");
        }
    });

    $("#btnAddToCart").on("click", function (event) {
        addToCartEvent();
    });
});
function setCsrfHeader(xhr) {
    xhr.setRequestHeader(csrfHeaderName, csrfValue);
}

function showModalDialog(title, message) {
    $("#modalTitle").html(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");

}

function addToCartEvent() {
    Qty = $("#Qty" + productId).val();
    URL = contextPath + "cart/add/" + productId + "/" + Qty;

    console.log("url " + URL);
    console.log("Qty " + Qty);

    $.ajax({
        type: "POST",
        url: URL,
        beforeSend: setCsrfHeader
    }).done(function (response) {
        showModalDialog("Shopping <i class='fa-duotone fa-bag-shopping'></i>", response);
    }).fail(function () {
        showModalDialog("Error", "Something went wrong while adding the products");
    });
}