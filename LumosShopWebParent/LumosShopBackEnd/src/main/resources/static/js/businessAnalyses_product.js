var data;
var chartOptions;
var FtotalTransactions;
var FnetRevenue;
var FtotalRevenue;



$(document).ready(function () {
    attachBtnResponse("product", uploadNewDataByTimeFrameForProduct);
});

function uploadNewDataByTimeFrameForProduct(TimeFrame) {

    if (TimeFrame === 'personalized') {
        first = $("#startDateproduct").val();
        end = $("#endDateproduct").val();
        URL = contextPath + "Analyses/sales-analytics/product/" + first + "/" + end;

    } else {
        URL = contextPath + "Analyses/sales-analytics/product/" + TimeFrame;
    }

    $.get(URL, function (responseJSON) {
        generateProduct_ChartContent(responseJSON);
        adjustProduct_Chart();
        putValuesToTheAnalysesCard(TimeFrame, 'product','Total Products');

        organizeChart(data, 2, 3);
        plotProduct_ChartTimeFrame(TimeFrame);

    }).fail(function (err) {
        console.error("Error:", err);
    });
}

function generateProduct_ChartContent(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string', 'Product');
    data.addColumn('number', 'Qty');
    data.addColumn('number', 'Revenue');
    data.addColumn('number', 'Net Revenue');

    FtotalRevenue = 0.0;
    FnetRevenue = 0.0;
    totalNumOfItem = 0;
    $.each(responseJSON, function (index, record) {
        data.addRows([[record.code,record.totalProducts, record.totalRevenue, record.netRevenue]]);

        FtotalRevenue += parseFloat(record.totalRevenue);
        FnetRevenue += parseFloat(record.netRevenue);
        totalNumOfItem += parseFloat(record.totalProducts);
    });

}


function adjustProduct_Chart() {
    chartOptions = {

        showRowNumber: true, width: '100%', height: '400',


    };
}


function plotProduct_ChartTimeFrame() {
    var RevenueChart = new google.visualization.Table(document.getElementById("chart_SalesAnalyses_Timeframeproduct"));
    RevenueChart.draw(data, chartOptions);
}








