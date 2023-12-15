function attachBtnResponse(group,response) {
    firstDateF = document.getElementById('startDate'+group)
    lastDateF = document.getElementById('endDate'+group)
    $(".Btn-Analyses"+group).on("click", function () {

        $(".Btn-Analyses"+group).each(function (event) {
            $(this).removeClass('btn-success').addClass('btn-outline-success');
        });
        $(this).removeClass('btn-outline-success').addClass('btn-success');
        TimeFrame = $(this).attr("TimeFrame");

        if (TimeFrame) {
            response(TimeFrame);
            $("#Personalized-Range"+group).addClass("d-none");

        } else {
            $("#Personalized-Range"+group).removeClass("d-none");
        }
    });
    activatePersonalizedRange(group);
    $("#fetchingPersonalized-Data"+group).on("click", function () {
        validationOfTheRange(group,response);
    });

}
function validationOfTheRange(group,response) {
    range = determineNumberOfDays(group);
    firstDateF.setCustomValidity("");
    if (range >= 8 && range <= 28) {
        response('personalized');
    } else {
        firstDateF.setCustomValidity("You have to pick a range with Minimum 8 and Maximum 28 range of days");
        firstDateF.reportValidity();
    }
}

function determineNumberOfDays(group){
    firstDateF = document.getElementById('startDate' + group);
    lastDateF = document.getElementById('endDate' + group);

    firstDay = new Date(firstDateF.value);
    lastDay = new Date(lastDateF.value);

    const timeDifferenceInMilliseconds = Math.abs(lastDay - firstDay);
    return timeDifferenceInMilliseconds / (24 * 60 * 60 * 1000);
}


function activatePersonalizedRange(group) {

    toDay = new Date();
    lastDateF.valueAsDate = toDay;

    fromDay = new Date();
    fromDay.setDate(toDay.getDate() - 28);
    firstDateF.valueAsDate = fromDay;
}
function priceDisplay(price) {
    formatted = $.number(price, decimalPrec, currencyDisSep, currencyGrSep);
    return prefixPrice + formatted + suffixPrice;

}


function specifyTheChart(TimeFrame,group) {
    if (TimeFrame === "last_Week") {
        return "Analyses of sales last Week";
    }
    if (TimeFrame === "15Days") {
        return "Analyses of sales last 15 Days";
    }
    if (TimeFrame === "29Days") {
        return "Analyses of sales last 29 Days";
    }
    if (TimeFrame === "last3Month") {
        return "Analyses of sales last 3 Month";
    }
    if (TimeFrame === "last6Month") {
        return "Analyses of sales last 6 Month";
    }
    if (TimeFrame === "last_Year") {
        return "Analyses of sales last Year";
    }
    if (TimeFrame === "personalized") {
        return "Analyses of sales by you personalized";
    }
}
function specifyNumberOfDays(TimeFrame,group) {
    if (TimeFrame === "last_Week") {
        return 7;
    }
    if (TimeFrame === "15Days") {
        return 15;
    }
    if (TimeFrame === "29Days") {
        return 29;
    }
    if (TimeFrame === "last3Month") {
        return 3*30;
    }
    if (TimeFrame === "last6Month") {
        return 6*30;
    }
    if (TimeFrame === "last_Year") {
        return 365;
    }
    if (TimeFrame === "personalized") {
        return determineNumberOfDays(group);
    }
}


function putValuesToTheAnalysesCard(TimeFrame,group,totalNumText) {
    $("#TotalNumCard"+group).text(totalNumOfItem);

    dayCount = specifyNumberOfDays(TimeFrame,group)
    $("#CTotalRevenue"+group).text(priceDisplay(FtotalRevenue));
    $("#CNetRevenue"+group).text(priceDisplay(FnetRevenue));
    $("#CTotalRevenueAVG"+group).text(priceDisplay(FtotalRevenue/dayCount));
    $("#CNetRevenueAVG"+group).text(priceDisplay(FnetRevenue/dayCount));
    $("#TotalNumCardText"+group).text(totalNumText);
}

function organizeChart(data,col1,col2) {
    var formatter = new google.visualization.NumberFormat({
        prefix: prefixPrice,
        suffix: suffixPrice,
        decimalSymbol: currencyDisSep,
        groupingSymbol: currencyGrSep,

    });
    formatter.format(data, col1);
    formatter.format(data, col2);
}



