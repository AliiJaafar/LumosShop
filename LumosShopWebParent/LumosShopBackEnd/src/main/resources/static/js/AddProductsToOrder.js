var productDetailCount;

$(document).ready(function() {

    productDetailCount = $(".ProductIdInStocks").length;

    $("#Stocks").on("click", "#linkAddProduct", function(e) {
        e.preventDefault();
        link = $(this);
        url = link.attr("href");

        $("#addProductModal").on("shown.bs.modal", function() {
            $(this).find("iframe").attr("src", url);
        });

        $("#addProductModal").modal("show");
    })


    $("#Stocks").on("click", ".removeProductTrash", function(e) {

        e.preventDefault();

        if (orderHasSoleProduct()) {
            displayWarningStaticModal("Product cannot be removed. Ensure the order has at least one product.");
        } else {
            eraseProduct($(this));
            RefreshOrder();
        }
    });
});

function addNewProductToThOrder(productID, productNAME) {

    $("#addProductModal").modal("hide");
    determineShippingCharge(productID)
}

function isInTheOrder(productID) {
    let InStockList = false;
    $(".ProductIdInStocks").each(function (event) {
        let productId = $(this).val();

        if (productId === productID) {
            InStockList = true;
            return; // exit the loop early since we found a match
        }
    });
    return InStockList;
}


function determineShippingCharge(productID) {

    choosenNation = $("#nation option:selected");
    nationID = choosenNation.val();
    city = $("#city").val();

    reqURL = contextPath + "get_shipping_charge";

    params = {productId: productID,nationID:nationID, city: city}

    $.ajax({
        type: 'POST',
        url: reqURL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: params
    }).done(function (shippingCharge) {
        retrieveProductDetails(productID, shippingCharge);
    }).fail(function (error) {
        shippingCharge = 0.0;
        retrieveProductDetails(productID, shippingCharge);
        displayWarningStaticModal(error.responseJSON.message); //always failed and give me this response: The given id must not be null
    });
}

function retrieveProductDetails(productId, shippingCharge) {
    requestURL = contextPath + "products/get/" + productId;
    $.get(requestURL, function(productJson) {
        console.log(productJson);
        productName = productJson.name;
        mainImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath;
        productCost = $.number(productJson.cost, 2);
        productPrice = $.number(productJson.price, 2);

        html = generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCharge);
        $("#productList").append(html);

        RefreshOrder();

    }).fail(function(error) {
        displayingNormalModal(error.responseJSON.message);
    });
}
function generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCharge) {
    let Coming = $(".ProductIdInStocks").length + 1;
    productDetailCount++;
    let rowId = "row" + Coming;
    let quantityId = "quantity" + Coming;
    let priceId = "price" + Coming;
    let InterSumId = "interSum" + Coming;
    let GapId = "Gap" + Coming;

    AppendCode=`<div class="border rounded p-1 my-2" id="${rowId}">
                <input value="${productId}" name="productID" class="ProductIdInStocks" hidden>
                <input hidden name="summaryID" value="0" />
                <div class="row">
                    <div class="col-md-1 d-flex align-items-start flex-column">
                        <div class="divCount ms-2 mt-2 mb-auto">${Coming}</div>
                        <div>
                            <a class="fa-regular fa-circle-trash text-danger fa-xl removeProductTrash" href="" rowNumber="${Coming}"></a>
                        </div>
                    </div>
                    <div class="col-md-5">
                        <img src="${mainImagePath}" class="img-fluid" alt="stockPhoto"/>
                    </div>
                    <div class="col-md-6">
                        <div class="row m-2">
                            <b>${productName}</b>
                        </div>
                        <div class="row m-2 p-4">
                            <div class="form-floating mb-3">
                                <input type="text" class="form-control cost-input"
                                       id="inputCost" value="${productCost}"
                                       name="productSummaryCost"
                                       rowNumber="${Coming}">
                                <label for="inputCost">Product Cost</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="text" class="form-control price-input"
                                       id="itemPrice" value="${productPrice}"
                                       name="productPrice"
                                       id="${priceId}"
                                       rowNumber="${Coming}">
                                <label for="itemPrice">Product Price</label>
                            </div>

                            <div class="form-floating mb-3">
                                <input type="number" step="1" min="1" max="6" class="form-control quantity-input"
                                       id="floatingInputValue" value="1"
                                       name="quantity"
                                       id="${quantityId}"
                                       rowNumber="${Coming}">
                                <label for="floatingInputValue">Qty</label>
                            </div>

                            <div class="form-floating mb-3">
                                <input type="text" class="form-control interSum-output"
                                       id="interSum" value="${productPrice}" readonly
                                       name="productInterSum"
                                       id="${InterSumId}" >
                                <label for="interSum">Sum</label>
                            </div>

                            <div class="form-floating">
                                <input type="text" class="form-control ship-input" id="shippingCharge"
                                       value="${shippingCharge}" required
                                       name="productShippingCharge">
                                <label for="shippingCharge">Shipping charge</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="${GapId}" class="row">&nbsp;</div>`
    return AppendCode;
}

function orderHasSoleProduct() {
    productCount = $(".ProductIdInStocks").length;
    return productCount === 1;
}

function eraseProduct(ref) {

    rowNumber = ref.attr("rowNumber");
    $("#row" + rowNumber).remove();
    $("#Gap" + rowNumber).remove();

    $(".divCount").each(function(index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

