package com.dgpad.admin.business;

import com.dgpad.admin.order.OrderRepository;
import com.lumosshop.common.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BusinessRecordService extends AbstractBusinessRecordService {
    @Autowired
    private OrderRepository orderRepository;




    public List<BusinessRecord> getRecordsDataWithinCertainTimeframe(Date startIn, Date endIn,RecordsGroup group) {

        List<Order> orderList = orderRepository.findOrdersByDate(startIn, endIn);
        //OK
        List<BusinessRecord> businessRecordList = produceReadyToDisplayData(startIn, endIn, group);

        determineSalesBeforeSendingData(orderList, businessRecordList);

        return businessRecordList;
    }


    private void determineSalesBeforeSendingData(List<Order> listOrders, List<BusinessRecord> recordList) {

        for (Order order : listOrders) {
            String orderDay = dateFormat.format(order.getOrderDate());

            BusinessRecord businessRecord = new BusinessRecord(orderDay);

            int record_i = recordList.indexOf(businessRecord);
            if (record_i >= 0) {
                businessRecord = recordList.get(record_i);

                businessRecord.increaseTotalRevenue(order.getTotalPrice());
                businessRecord.increaseNetRevenue(order.getInterSum() - order.getProductCost());
                businessRecord.raiseTotalTransactions();

            }
        }

    }
    private List<BusinessRecord> produceReadyToDisplayData(Date startIn, Date endIn, RecordsGroup recordsGroup) {

        List<BusinessRecord> businessRecords = new ArrayList<>();

        Calendar firstDay = Calendar.getInstance();
        firstDay.setTime(startIn);

        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(endIn);

        Date DateAtHand = firstDay.getTime();
        String DateAtHandInString = dateFormat.format(DateAtHand);

        businessRecords.add(new BusinessRecord(DateAtHandInString));
        do {

            if (recordsGroup.equals(RecordsGroup.DAY)) {
                firstDay.add(Calendar.DAY_OF_MONTH, 1);

            } else if (recordsGroup.equals(RecordsGroup.MONTH)) {
                firstDay.add(Calendar.MONTH, 1);
            }

            DateAtHand = firstDay.getTime();
            DateAtHandInString = dateFormat.format(DateAtHand);

            businessRecords.add(new BusinessRecord(DateAtHandInString));
        } while (firstDay.before(lastDay));
        return businessRecords;
    }




}
