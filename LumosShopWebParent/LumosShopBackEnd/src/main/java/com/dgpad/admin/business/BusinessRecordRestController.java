package com.dgpad.admin.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class BusinessRecordRestController {
    @Autowired
    private BusinessRecordService businessRecordService;
    @Autowired
    OrderAnalysesService orderAnalysesService;

    @GetMapping("/Analyses/sales-analytics/by-date/{Timeframe}")
    public List<BusinessRecord> getDataByTimeFrame(@PathVariable("Timeframe") String timeframe) {

        if ("last_Week".equals(timeframe)) {
            return businessRecordService.getAnalysesForAWeek(RecordsGroup.DAY);
        } else if ("15Days".equals(timeframe)) {
            return businessRecordService.getAnalysesFor15Days(RecordsGroup.DAY);
        } else if ("29Days".equals(timeframe)) {
            return businessRecordService.getAnalysesFor29Days(RecordsGroup.DAY);
        } else if ("last3Month".equals(timeframe)) {
            return businessRecordService.getAnalysesForOneQuarter(RecordsGroup.MONTH);
        } else if ("last6Month".equals(timeframe)) {
            return businessRecordService.getAnalysesForTwoQuarter(RecordsGroup.MONTH);
        } else if ("last_Year".equals(timeframe)) {
            return businessRecordService.getAnalysesForOneYear(RecordsGroup.MONTH);
        } else {
            // Default case
            return businessRecordService.getAnalysesForAWeek(RecordsGroup.DAY);
        }

    }

    @GetMapping("/Analyses/sales-analytics/by-date/{firstDay}/{lastDay}")
    public List<BusinessRecord> getDataByTimeFrame(@PathVariable("firstDay") String firstDay,
                                                   @PathVariable("lastDay") String lastDay) throws ParseException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse(firstDay);
        Date endTime = dateFormatter.parse(lastDay);

        return businessRecordService.getRecordsDataWithinCertainTimeframe(startTime, endTime, RecordsGroup.DAY);

    }

    @GetMapping("/Analyses/sales-analytics/{Type}/{TimeFrame}")
    public List<BusinessRecord> getAnalysesUsingTypeName(@PathVariable("Type") String Type, @PathVariable("TimeFrame") String timeframe) {

        RecordsGroup group = RecordsGroup.valueOf(Type.toUpperCase());
        System.out.println(group);

        if ("last_Week".equals(timeframe)) {
            return orderAnalysesService.getAnalysesForAWeek(group);
        } else if ("15Days".equals(timeframe)) {
            return orderAnalysesService.getAnalysesFor15Days(group);
        } else if ("29Days".equals(timeframe)) {
            return orderAnalysesService.getAnalysesFor29Days(group);
        } else if ("last3Month".equals(timeframe)) {
            return orderAnalysesService.getAnalysesForOneQuarter(group);
        } else if ("last6Month".equals(timeframe)) {
            return orderAnalysesService.getAnalysesForTwoQuarter(group);
        } else if ("last_Year".equals(timeframe)) {
            return orderAnalysesService.getAnalysesForOneYear(group);
        } else {
            // Default case
            return orderAnalysesService.getAnalysesForAWeek(group);
        }
    }

    @GetMapping("/Analyses/sales-analytics/{Type}/{firstDay}/{lastDay}")
    public List<BusinessRecord> getAnalysesByType(@PathVariable("Type") String Type,
                                                  @PathVariable("firstDay") String firstDay,
                                                   @PathVariable("lastDay") String lastDay) throws ParseException {

        RecordsGroup recordsGroup = RecordsGroup.valueOf(Type.toUpperCase());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse(firstDay);
        Date endTime = dateFormatter.parse(lastDay);

        return orderAnalysesService.getRecordsDataWithinCertainTimeframe(startTime, endTime, recordsGroup);

    }
}
