var fieldProductCost;
var fieldInterSum;
var fieldShippingCharge;
var fieldVAT;
var fieldTotal;

$(document).ready(function() {

	fieldProductCost = $("#productCost");
	fieldInterSum = $("#interSum");
	fieldShippingCharge = $("#shippingCharge");
	fieldVAT = $("#vatField");
	fieldTotal = $("#totalPriceField");



	formatOrderAmounts();
	formatProductAmounts();

	$("#productList").on("change", ".quantity-input", function(e) {
		updateInterSumWhenQuantityChanged($(this));
		RefreshOrder();
	});

	$("#productList").on("change", ".price-input", function(e) {
		updateInterSumWhenPriceChanged($(this));
		RefreshOrder();
	});

	$("#productList").on("change", ".cost-input, .ship-input", function (e) {
		RefreshOrder();
	});
});

function RefreshOrder() {
	totalCost = 0.0;

	$(".cost-input").each(function(event) {
		costInputField = $(this);
		rowNumber = costInputField.attr("rowNumber");
		qty = $("#quantity" + rowNumber).val();


		productCost = getNumberValueRemovedThousandSeparator(costInputField);
		totalCost += productCost * parseInt(qty);
	});

	setAndFormatNumberForField("productCost", totalCost);

	orderInterSum = 0.0;

	$(".interSum-output").each(function(event) {
		productInterSum = getNumberValueRemovedThousandSeparator($(this));
		orderInterSum += productInterSum;
	});

	setAndFormatNumberForField("interSum", orderInterSum);

	shippingCharge = 0.0;

	$(".ship-input").each(function(e) {
		productShip = getNumberValueRemovedThousandSeparator($(this));
		shippingCharge += productShip;
	});

	setAndFormatNumberForField("shippingCharge", shippingCharge);

	VAT = getNumberValueRemovedThousandSeparator(fieldVAT);
	orderTotal = orderInterSum + VAT + shippingCharge;
	setAndFormatNumberForField("totalPriceField", orderTotal);
}
function setAndFormatNumberForField(IdField, ValueField) {
	formattedValue = $.number(ValueField, 2);
	$("#" + IdField).val(formattedValue);
}

function getNumberValueRemovedThousandSeparator(fieldRef) {
	fieldValue = fieldRef.val().replace(",", "");
	return parseFloat(fieldValue);
}
function updateInterSumWhenPriceChanged(input) {
	priceValue = getNumberValueRemovedThousandSeparator(input);
	rowNumber = input.attr("rowNumber");

	quantityField = $("#quantity" + rowNumber);
	quantityValue = quantityField.val();
	newnterSum = parseFloat(quantityValue) * priceValue;

	setAndFormatNumberForField("interSum" + rowNumber, newnterSum);
}

function updateInterSumWhenQuantityChanged(input) {
	quantityValue = input.val();
	rowNumber = input.attr("rowNumber");
	priceValue = getNumberValueRemovedThousandSeparator($("#price" + rowNumber));
	newInterSum = parseFloat(quantityValue) * priceValue;

	setAndFormatNumberForField("interSum" + rowNumber, newInterSum);
}
function formatProductAmounts() {
	$(".cost-input, .price-input, .interSum-output, .ship-input").each(function () {
		formatNumberForField($(this));
	});
}

function formatOrderAmounts() {
	[fieldProductCost, fieldInterSum, fieldShippingCharge, fieldVAT, fieldTotal].forEach(function (fieldRef) {
		formatNumberForField(fieldRef);
	});
}
function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit() {

	removeThousandSeparatorForField(fieldProductCost);
	removeThousandSeparatorForField(fieldInterSum);
	removeThousandSeparatorForField(fieldShippingCharge);
	removeThousandSeparatorForField(fieldVAT);
	removeThousandSeparatorForField(fieldTotal);

	$(".cost-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	$(".price-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	$(".interSum-output").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	$(".ship-input").each(function(e) {
		removeThousandSeparatorForField($(this));
	});

	return true;
}

function removeThousandSeparatorForField(fieldRef) {
	fieldRef.val(fieldRef.val().replace(",", ""));
}
