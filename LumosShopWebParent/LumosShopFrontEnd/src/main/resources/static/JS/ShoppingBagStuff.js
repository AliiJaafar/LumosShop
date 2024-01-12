decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';

$(document).ready(function () {
    // Find the input element and plus/minus buttons
    const plusBtn = $(".plusBtn");
    const minusBtn = $(".minusBtn");
    const deleteBtn = $(".deleteBtn");

    deleteBtn.click(function () {
        productId = $(this).attr("product_id");
        deleteProduct($(this),productId);
    });

    plusBtn.click(function () {
        productId = $(this).attr("product_id");
        const quantityInput = $('#Qty' + productId);

        let currentQuantity = parseInt(quantityInput.val());
        if (currentQuantity < 6) {
            quantityInput.val(currentQuantity + 1);
            changeQtyEvent(currentQuantity + 1, productId);
        } else {
            showModalDialog("Alert <i class='fa-duotone fa-message-exclamation'></i> ", "You can't take more than 6 quantity.");
        }
    });

    minusBtn.click(function () {

        productId = $(this).attr("product_id");
        const quantityInput = $('#Qty' + productId);
        let currentQuantity = parseInt(quantityInput.val());

        if (currentQuantity > 1) {
            quantityInput.val(currentQuantity - 1);
            changeQtyEvent(currentQuantity - 1, productId);

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


function changeQtyEvent(qty, productId) {
    URL = contextPath + "cart/change/" + productId + "/" + qty;

    $.ajax({
        type: "POST",
        url: URL,
        beforeSend: setCsrfHeader
    }).done(function (LastInterSum) {
        changeInterSum(LastInterSum, productId);
        changeTotalPrice();
    }).fail(function () {
        showModalDialog("Error", "Something went wrong while changing the Qty");
    });
}

function changeInterSum(LastInterSum, productId) {
    $("#InterSum" + productId).text(LastInterSum);
}

function changeTotalPrice() {
    totalPrice = 0.0;
    count = 0;

    $(".InterSum").each(function (index, element) {
        count++;
        totalPrice += parseFloat(element.innerHTML);
    });

    if (count < 1) {
        window.location.reload();
        console.log("empty intermediate sum")
    } else {
        $(".totalPrice").text(totalPrice);
    }
}

function deleteProduct(ref,productId) {
    URL = contextPath + "bag/delete/" + productId;
    $.ajax({
        type: "DELETE",
        url: URL,
        beforeSend: setCsrfHeader
    }).done(function (response) {
        targetSeq = ref.attr("seq");
        removeProductHTML(targetSeq);
        changeTotalPrice()
        showModalDialog("Shopping <i class='fa-duotone fa-bag-shopping'></i>", response);

    }).fail(function () {
        showModalDialog("Error","Some thing wierd happened while removing the product.");
    });
    function removeProductHTML(targetSeq) {
        console.log("seq " + targetSeq);
        $("#seq" + targetSeq).remove();
    }
}

