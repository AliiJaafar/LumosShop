package com.dgpad.admin.business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractBusinessRecordService {

    protected DateFormat dateFormat;

    public List<BusinessRecord> getAnalysesForAWeek(RecordsGroup recordsGroup) {

        return getAnalysesForNDays(7, recordsGroup);
    }
    public List<BusinessRecord> getAnalysesFor15Days(RecordsGroup recordsGroup) {
        return getAnalysesForNDays(15, recordsGroup);
    }
    public List<BusinessRecord> getAnalysesFor29Days(RecordsGroup recordsGroup) {
        return getAnalysesForNDays(29, recordsGroup);
    }

    protected List<BusinessRecord> getAnalysesForNDays(int days, RecordsGroup recordsGroup) {


        Date lastDay = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date firstDay = calendar.getTime();

//        System.out.println("Get Analyses For: "+ days +" Days");
//
//        System.out.println("first Day: " + firstDay);
//        System.out.println("last Day: " + lastDay);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getRecordsDataWithinCertainTimeframe(firstDay, lastDay, recordsGroup);
    }
    protected List<BusinessRecord> getAnalysesForN_Month(int months, RecordsGroup recordsGroup) {

        Date lastDay = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -(months - 1));
        Date firstDay = calendar.getTime();

        dateFormat = new SimpleDateFormat("yyyy-MM");

        return getRecordsDataWithinCertainTimeframe(firstDay, lastDay, recordsGroup);
    }

    public List<BusinessRecord> getAnalysesForOneQuarter(RecordsGroup recordsGroup) {
        return getAnalysesForN_Month(3, recordsGroup);
    }
    public List<BusinessRecord> getAnalysesForTwoQuarter(RecordsGroup recordsGroup) {
        return getAnalysesForN_Month(6, recordsGroup);
    }
    public List<BusinessRecord> getAnalysesForOneYear(RecordsGroup recordsGroup) {
        return getAnalysesForN_Month(12, recordsGroup);
    }

    protected abstract List<BusinessRecord> getRecordsDataWithinCertainTimeframe(
            Date startDate, Date endDate, RecordsGroup group);

    public List<BusinessRecord> GetDataBetweenDates(Date firstDay, Date lastDay, RecordsGroup recordsGroup) {

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getRecordsDataWithinCertainTimeframe(firstDay, lastDay, recordsGroup);
    }
}
