var data;
var chartOptions;
var FtotalTransactions;
var FnetRevenue;
var FtotalRevenue;



$(document).ready(function () {
    attachBtnResponse("ALL", uploadNewDataByTimeFrame);
});


function uploadNewDataByTimeFrame(TimeFrame) {

    if (TimeFrame === 'personalized') {
        first = $("#startDateALL").val();
        end = $("#endDateALL").val();
        URL = contextPath + "Analyses/sales-analytics/by-date/" + first + "/" + end;

    } else {
        URL = contextPath + "Analyses/sales-analytics/by-date/" + TimeFrame;
    }

    $.get(URL, function (responseJSON) {
        generateChartContent(responseJSON);
        adjustChart(TimeFrame);
        putValuesToTheAnalysesCard(TimeFrame, 'ALL','Number Of Transactions');

        organizeChart(data, 1, 2);
        plotChartTimeFrame(TimeFrame);

    }).fail(function (err) {
        console.error("Error:", err);
    });
}

function adjustChart(TimeFrame) {
    let ChartTitle = specifyTheChart(TimeFrame);
    chartOptions = {
        title: ChartTitle,
        width: '100%', height: '400',
        legend: {position: 'top'},
        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 0},
            2: {targetAxisIndex: 1},
        },
        vAxes: {
            0: {title: 'Sales', format: 'currency'},
            1: {title: 'Transactions'},
        },
        chartArea: {
            backgroundColor: {
                fill: '#FF0000',
                fillOpacity: 0.1
            },
        },
    };
}



function generateChartContent(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string', 'Date');
    data.addColumn('number', 'Revenue');
    data.addColumn('number', 'Net Revenue');
    data.addColumn('number', 'Transactions');

    FtotalRevenue = 0.0;
    FnetRevenue = 0.0;
    totalNumOfItem = 0;
    $.each(responseJSON, function (index, record) {
        data.addRows([[record.code, record.totalRevenue, record.netRevenue, record.totalTransactions]]);

        FtotalRevenue += parseFloat(record.totalRevenue);
        FnetRevenue += parseFloat(record.netRevenue);
        totalNumOfItem += parseFloat(record.totalTransactions);
    });

}
function plotChartTimeFrame() {
    var RevenueChart = new google.visualization.ColumnChart(document.getElementById("chart_SalesAnalyses_TimeframeALL"));
    RevenueChart.draw(data, chartOptions);
}



