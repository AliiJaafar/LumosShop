function showNormalModal(title, message) {
    $("#normalTitle").text(title);
    $("#normalContent").text(message);
    $("#normalModal").modal("show");
}
function showStaticModal(title, message) {
    $("#staticTitle").text(title);
    $(".StaticModal-body").html(message);
    $("#staticModal").modal("show");
}

function displayErrorStaticModal(message) {
    showStaticModal("Error", message);
}

function displayWarningStaticModal(message) {
    showStaticModal("Warning", message);
}

function displayingNormalModal(message) {
    showNormalModal("Notice", message);
}
function displayingNormalModalERROR(message) {
    showNormalModal("Error", message);
}
