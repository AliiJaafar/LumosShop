var data;
var chartOptions;
var FtotalTransactions;
var FnetRevenue;
var FtotalRevenue;



$(document).ready(function () {
    attachBtnResponse("category", uploadNewDataByTimeFrameForCategory);
});

function uploadNewDataByTimeFrameForCategory(TimeFrame) {

    if (TimeFrame === 'personalized') {
        first = $("#startDatecategory").val();
        end = $("#endDatecategory").val();
        URL = contextPath + "Analyses/sales-analytics/category/" + first + "/" + end;

    } else {
        URL = contextPath + "Analyses/sales-analytics/category/" + TimeFrame;
    }

    $.get(URL, function (responseJSON) {
        generateCategory_ChartContent(responseJSON);
        adjustCategory_Chart();
        putValuesToTheAnalysesCard(TimeFrame, 'category','Total Products');

        organizeChart(data, 1, 2);
        plotCategory_ChartTimeFrame(TimeFrame);

    }).fail(function (err) {
        console.error("Error:", err);
    });
}

function generateCategory_ChartContent(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string', 'Category');
    data.addColumn('number', 'Revenue');
    data.addColumn('number', 'Net Revenue');

    FtotalRevenue = 0.0;
    FnetRevenue = 0.0;
    totalNumOfItem = 0;
    $.each(responseJSON, function (index, record) {
        data.addRows([[record.code, record.totalRevenue, record.netRevenue]]);

        FtotalRevenue += parseFloat(record.totalRevenue);
        FnetRevenue += parseFloat(record.netRevenue);
        totalNumOfItem += parseFloat(record.totalProducts);
    });

}


function adjustCategory_Chart() {
    chartOptions = {
        width: '100%', height: '400',
        legend: {position: 'right'},
    };
}


function plotCategory_ChartTimeFrame() {
    var RevenueChart = new google.visualization.PieChart(document.getElementById("chart_SalesAnalyses_Timeframecategory"));
    RevenueChart.draw(data, chartOptions);
}








