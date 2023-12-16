function showNormalModal(title, message) {
    $("#normalTitle").text(title);
    $(".modal-body").text(message);
    $("#normalModal").modal("show");
}
function showStaticModal(title, message) {
    $("#staticTitle").text(title);
    $(".modal-body").text(message);
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
