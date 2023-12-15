package com.dgpad.admin.business;

import com.dgpad.admin.order.OrderSummaryRepository;
import com.lumosshop.common.entity.order.Order_Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderAnalysesService extends AbstractBusinessRecordService {

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;
    @Override
    protected List<BusinessRecord> getRecordsDataWithinCertainTimeframe(Date startDate, Date endDate, RecordsGroup group) {

        List<Order_Summary> order_summaryList = null;
        if (group.equals(RecordsGroup.PRODUCT)) {
            order_summaryList = orderSummaryRepository.findOrdersByProductAndDate(startDate, endDate);
            System.out.println("getRecordsDataWithinCertainTimeframe -- product");
        }else if (group.equals(RecordsGroup.CATEGORY)) {
            order_summaryList = orderSummaryRepository.findOrdersByCategoryAndDate(startDate, endDate);
            System.out.println("getRecordsDataWithinCertainTimeframe -- category");

        }
        displayOrder(order_summaryList);

        List<BusinessRecord> businessRecordList = new ArrayList<>();
        for (Order_Summary order : order_summaryList) {
            String code="";

            if (group.equals(RecordsGroup.PRODUCT)) {
                code = order.getProduct().getName();
            }
            else if (group.equals(RecordsGroup.CATEGORY)) {
                code = order.getProduct().getCategory().getName();
            }
            BusinessRecord record = new BusinessRecord(code);

            float revenue = order.getInterSum() + order.getShippingCharge();
            float netRevenue = order.getInterSum() - order.getProductCost();
            int productQty = order.getQty();
            int i = businessRecordList.indexOf(record);
            if (i >= 0) {
                record = businessRecordList.get(i);
                record.increaseTotalRevenue(revenue);
                record.increaseNetRevenue(netRevenue);
                record.raiseTotalProduct(productQty);

            } else {
                businessRecordList.add(new BusinessRecord(code, revenue, netRevenue, productQty));
            }
        }

        displayAnalyses(businessRecordList);
        
        return businessRecordList;
    }

    private void displayAnalyses(List<BusinessRecord> businessRecordList) {
        for (BusinessRecord record : businessRecordList) {
            System.out.printf("%-20s,%10.2f,%10.2f,%d \n",
                    record.getCode(), record.getTotalRevenue(), record.getNetRevenue(), record.getTotalProducts());
        }
    }

    private void displayOrder(List<Order_Summary> orderSummaryList) {
        for (Order_Summary order : orderSummaryList) {
            System.out.printf("%d,%-20s,%10.2f,%10.2f,%10.2f \n",
                    order.getQty(), order.getProduct().getName(), order.getInterSum(), order.getProductCost(), order.getShippingCharge());
        }
    }
}
