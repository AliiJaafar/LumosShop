// Define variables for DOM elements
const buttonLoad = $("#buttonLoadNations");
const buttonLoadNCity = $("#buttonLoadNationsForCity");
const dropDownN = $("#dropDownNations");
const dropDownNationRelatedToCity = $("#dropDownNationsForCity");
const dropDownCity = $("#dropDownCities");
const btnAddNation = $("#btn-AddNation");
const btnUpdateNation = $("#btn-UpdateNation");
const btnDeleteNation = $("#btn-DeleteNation");
const btnAddCity = $("#btn-AddCity");
const btnUpdateCity = $("#btn-UpdateCity");
const btnDeleteCity = $("#btn-DeleteCity");
const NameField = $("#Name-field");
const CodeField = $("#Code-field");
const labelNationName = $("#labelNationName");
const labelCityName = $("#labelCityName");
const CityName = $("#City-Name-field");

// Disable certain fields initially
NameField.prop("disabled", true);
CodeField.prop("disabled", true);
CityName.prop("disabled", true);

// Document ready function
$(document).ready(function () {
    // Load button click event
    buttonLoad.click(function () {
        loadNations();
    });

    // Load Nations for City button click event
    buttonLoadNCity.click(function () {
        loadNationsToCertainCity();
    });

    // Dropdown change events
    dropDownN.on("change", function () {
        updateFormToSelected();
    });

    dropDownNationRelatedToCity.on("change", function () {
        loadCitiesToCertainNation();
    });

    dropDownCity.on("change", function () {
        changeFormStateToSelectedCity();
    });

    // Add Nation button click event
    btnAddNation.click(function () {
        if (btnAddNation.val() === "Add") {
            addNation();
        } else {
            shiftFormToFreshState();
        }
    });

    // Update Nation button click event
    btnUpdateNation.click(function () {
        updateNation();
    });

    // Delete Nation button click event
    btnDeleteNation.click(function () {
        deleteNation();
    });

    // Add City button click event
    btnAddCity.click(function () {
        if (btnAddCity.val() === "Add") {
            addCity();
        } else {
            shiftFormToFreshStateCity();
        }
    });

    // Update City button click event
    btnUpdateCity.click(function () {
        updateCity();
    });

    // Delete City button click event
    btnDeleteCity.click(function () {
        deleteCity();
    });
});

function setCsrfHeader(xhr) {
    xhr.setRequestHeader(csrfHeaderName, csrfValue);
}
function handleAjaxError() {
    showToastMessage("Unfortunately, we couldn't establish a server connection, or the server encountered an error.");
}

function deleteNation() {
    const optionValue = dropDownN.val();
    const Id = optionValue.split("-")[0];

    const url = contextPath + "nations/delete/" + Id;

    console.log("WE want to delete this id : " + Id);
    console.log("WRL : " + url);
    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: setCsrfHeader
    }).done(function () {
        $("#dropDownNations option[value='" + optionValue + "']").remove();
        shiftFormToFreshState();
        showToastMessage("The deletion of the target nation is complete.");
    }).fail(handleAjaxError);
}

function updateNation() {
    if (!checkFormValidity()) return;

    const Id = dropDownN.val().split("-")[0];
    const Name = NameField.val();
    const Code = CodeField.val();

    const jsonData = {id: Id, name: Name, code: Code};

    const url = contextPath + "nations/store";
    console.log("UPDATE NATION id: " + Id);


    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: setCsrfHeader,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (Id) {
        updateDropdown(Id, Code, Name);
        showToastMessage("The Nation has been successfully modified.");
        shiftFormToFreshState();
    }).fail(handleAjaxError);
}

function updateDropdown(Id, Code, Name) {
    const selectedOption = $("#dropDownNations option:selected");
    selectedOption.val(Id + "-" + Code);
    selectedOption.text(Name);
}

function addNation() {

    if (!checkFormValidity()) return;

    let url = contextPath + "nations/store";
    let Name = NameField.val();
    let Code = CodeField.val();
    let jsonData = {name: Name, code: Code};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: setCsrfHeader,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (Id) {
        chooseRecentlyAdded(Id, Name, Code);
        showToastMessage("A brand-new nation is now part of the list.");
    }).fail(handleAjaxError);

}


function chooseRecentlyAdded(Id, Name, Code) {
    let optionValue = Id + "-" + Code;
    $("<option>").val(optionValue).text(Name).appendTo(dropDownN);

    $("#dropDownNations option[value='" + optionValue + "']").prop("selected", true);

    CodeField.val("");
    NameField.val("").focus();
}

function shiftFormToFreshState() {
    btnAddNation.val("Add");
    labelNationName.text("Nation Name:");

    NameField.prop("disabled", false);
    CodeField.prop("disabled", false);

    btnUpdateNation.prop("disabled", false);
    btnDeleteNation.prop("disabled", false);

    CodeField.val("");
    NameField.val("").focus();
}

function updateFormToSelected() {
    btnAddNation.prop("value", "New");


    NameField.prop("disabled", false);
    CodeField.prop("disabled", false);

    btnUpdateNation.prop("disabled", false);
    btnDeleteNation.prop("disabled", false);

    labelNationName.text("Chosen one:");

    let selectedName = $("#dropDownNations option:selected").text();
    NameField.val(selectedName);

    let Code = dropDownN.val().split("-")[1];
    CodeField.val(Code);

}

function loadNations() {
    const url = contextPath + "nations/list";
    $.get(url, function (responseJSON) {
        dropDownN.empty();

        $.each(responseJSON, function (index, nation) {
            const optionValue = nation.id + "-" + nation.code;
            $("<option>").val(optionValue).text(nation.name).appendTo(dropDownN);
        });

    }).done(function () {
        const iconHTML = '<i class="fa-light fa-arrows-rotate"></i>';
        buttonLoad.html(iconHTML);
        showToastMessage("We've successfully retrieved data on all nations from the database.");
    }).fail(handleAjaxError);
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

function checkFormValidity() {
    const form = document.getElementById("formNation");
    if (!form.checkValidity()) {
        form.reportValidity();
        return false;
    }

    return true;
}


function deleteCity() {
    const Id = dropDownCity.val();

    const url = contextPath + "city/delete/" + Id;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: setCsrfHeader
    }).done(function () {
        $("#dropDownCities option[value='" + Id + "']").remove();
        shiftFormToFreshStateCity();
        showToastMessage("The city has been deleted");
    }).fail(handleAjaxError);
}

function updateCity() {

    if (!validateFormCity()) return;

    const url = contextPath + "city/store";
    const CId = dropDownCity.val();
    const CName = CityName.val();

    const selectedNation = $("#dropDownNationsForCity option:selected");
    const NationId = selectedNation.val();
    const NationName = selectedNation.text();

    const jsonData = {id: CId, name: CName, nation: {id: NationId, name: NationName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: setCsrfHeader,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (Id) {
        $("#dropDownCities option:selected").text(CName);
        showToastMessage("The City has been updated");
        shiftFormToFreshStateCity();
    }).fail(handleAjaxError);
}

function addCity() {

    if (!validateFormCity()) return;

    const url = contextPath + "city/store";
    const CName = CityName.val();

    const selectedNation = $("#dropDownNationsForCity option:selected");
    const NationId = selectedNation.val();
    const NationName = selectedNation.text();
    console.log("ADD City URL -->: " + url)
    console.log("NationID: " + NationId)
    console.log("NationName: " + NationName)

    const jsonData = {name: CName, nation: {id: NationId, name: NationName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: setCsrfHeader,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (Id) {
        selectRecentlyAddedCity(Id, CName);
        showToastMessage("The new city has been added");
    }).fail(handleAjaxError);

}

function selectRecentlyAddedCity(cityId, cityName) {
    $("<option>").val(cityId).text(cityName).appendTo(dropDownCity);

    $("#dropDownCities option[value='" + cityId + "']").prop("selected", true);

    CityName.val("").focus();
}

function shiftFormToFreshStateCity() {
    btnAddCity.val("Add");
    labelCityName.text("City/Village Name:");

    btnUpdateCity.prop("disabled", true);
    btnDeleteCity.prop("disabled", true);

    CityName.prop("disabled", false);

    CityName.val("").focus();
}

function changeFormStateToSelectedCity() {
    btnAddCity.prop("value", "New");
    btnUpdateCity.prop("disabled", false);
    btnDeleteCity.prop("disabled", false);

    CityName.prop("disabled", false);

    labelCityName.text("Selected State/Province:");

    let selectedCity = $("#dropDownCities option:selected").text();
    CityName.val(selectedCity);

}

function loadCitiesToCertainNation() {
    selectedNation = $("#dropDownNationsForCity option:selected");
    NationId = selectedNation.val();
    url = contextPath + "city/list/" + NationId;

    $.get(url, function (responseJSON) {
        dropDownCity.empty();

        $.each(responseJSON, function (index, city) {
            $("<option>").val(city.id).text(city.name).appendTo(dropDownCity);
        });

    }).done(function () {
        shiftFormToFreshStateCity();
        showToastMessage("All cities have been loaded for country " + selectedNation.text());
    }).fail(handleAjaxError);
}

function loadNationsToCertainCity() {
    url = contextPath + "nations/list";
    $.get(url, function (responseJSON) {
        dropDownNationRelatedToCity.empty();

        $.each(responseJSON, function (index, nation) {
            $("<option>").val(nation.id).text(nation.name).appendTo(dropDownNationRelatedToCity);
        });

    }).done(function () {
        buttonLoadNCity.val("Refresh Nation List");
        showToastMessage("All nations have been loaded");
    }).fail(handleAjaxError);
}

function validateFormCity() {
    formCity = document.getElementById("formCity");
    if (!formCity.checkValidity()) {
        formCity.reportValidity();
        return false;
    }

    return true;
}

